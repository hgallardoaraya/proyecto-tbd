import time
import psycopg2
import json
import random
import time 
import datetime

###############################################################################
current_milli_time = lambda: int(round(time.time() * 1000))


###############################################################################
#formato AAAA-MM-DD
def rand_date(iniDate, endDate):

  initstp = time.mktime(datetime.datetime.strptime(iniDate, "%Y-%m-%d").timetuple())
  endtstp = time.mktime(datetime.datetime.strptime(endDate, "%Y-%m-%d").timetuple())
  newDate = random.randrange( initstp, endtstp )
  newDateTimestamp = datetime.datetime.fromtimestamp(newDate)
  
  return newDateTimestamp.strftime("%Y-%m-%d")


###############################################################################
def get_connection_db(dbName):
	return psycopg2.connect(user="postgres", password="postgres", host="localhost", port="5432", database=dbName)


###############################################################################

def insert_table(table,dbName):
  try:	
    connection = get_connection_db(dbName)
    cursor = connection.cursor()

    for i in range(0, len(table["record_to_insert"]) ):
        cursor.execute(table["postgres_insert_query"], table["record_to_insert"][i])

    connection.commit()
    print ("se insertaron "+ str( len(table["record_to_insert"]) ) +" registros en la tabla "+ table["tableName"] )


  except (Exception, psycopg2.Error) as error :
    if(connection):
      print("Failed to insert record into "+table["tableName"]+" table", error)

  finally:
    #closing database connection.
    if(connection):
      cursor.close()
      connection.close()
      print("PostgreSQL connection is closed")

  print ("=================================================================")
  return tables



###############################################################################
def delete_table(tableName,dbName):
  try:  
    connection = get_connection_db(dbName)
    cursor = connection.cursor()
    #borrar todo para la prueba
    cursor.execute("delete from "+tableName)
    connection.commit()

    print ("se eliminaron los datos de la tabla "+ tableName )

  except (Exception, psycopg2.Error) as error :
    if(connection):
      print("Failed delete => "+tableName+" table", error)

  finally:
    #closing database connection.
    if(connection):
      cursor.close()
      connection.close()
      print("PostgreSQL connection is closed")

  print ("=================================================================")


###############################################################################
def create_simple_insert_instruction(obj):
	
	columnName = ""
	columnValues = ""
	for col in obj["columns"]:
		columnName =  columnName + col + ","
		columnValues =  columnValues + "%s,"

	postgres_insert_query = "INSERT INTO "+obj["tableName"]+" ("+columnName[:(len(columnName)-1)]+") VALUES ("+columnValues[:(len(columnValues)-1)]+")"
	return postgres_insert_query

###############################################################################
def create_simple_record(obj,tablesId,tables):
	
	record_to_insert=[]
	for id in range(0, obj["amount"]):
		record=[]
		for i in range(0, len(obj["columns"])):
			ctype = obj["columnsType"][i] 
			col = obj["columns"][i]

			if(ctype=="txt"):	
				record.append( get_column_value(ctype,obj["data"][col],str(id) ) )
			elif(ctype=="dep"):
				tableName = obj["data"][col]["tableName"]
				record.append( get_column_value(ctype,obj["data"][col], tables[ tablesId[tableName] ]["record_to_insert"]) )
			else:
				record.append( get_column_value(ctype,obj["data"][col], "") )	

		record_to_insert.append(record)	

	return record_to_insert


###############################################################################
def get_column_value(ctype,currentValue,auxValue):
	if(ctype=="txt"):		
		return currentValue.replace("N",auxValue)
	elif(ctype=="date"):
		return rand_date(currentValue[0],currentValue[1])
	elif(ctype=="rnd"):
		return random.randrange(currentValue[0],currentValue[1])
	elif(ctype=="dep"):
		tableDep = auxValue
		idx = random.randrange(0,len(tableDep))			
		return auxValue[idx][ currentValue["columnId"] ]
	else:
		return "EMPTY"


###############################################################################
def create_simple_insert(tablesId,tables,tableName):
	tables[ tablesId[tableName] ]["postgres_insert_query"] = create_simple_insert_instruction(tables[ tablesId[tableName] ] )
	tables[ tablesId[tableName] ]["record_to_insert"] = create_simple_record(tables[ tablesId[tableName] ],tablesId,tables)
	return tables[ tablesId[tableName] ]


###############################################################################
def main(tables,dbName,swDelete,swInsert):

	tablesId={}
	for i in range(len(tables)):
		 tablesId[ tables[i]["tableName"] ] = i

	#para ejecutar el main de esta forma, debe respetar las dependencias de las tablas
	#es decir, las tablas sin dependencias van primero en el arreglo tablas, y a medida
	#luego van las tablas con dependencias de tablas ya creadas (existentes en el arreglo)
	for table in tables:
		tableName = table["tableName"]
		tables[ tablesId[tableName] ] = create_simple_insert(tablesId,tables,tableName)

	#en este punto ya contamos con todas las tablas y los registros a crear por tabla
	print(json.dumps(tables, indent=2, sort_keys=True))  

	if(swDelete=='y'):
		for i in reversed(range(len(tables))):
			delete_table(tables[i]["tableName"],dbName)

	if(swInsert=='y'):
		for table in tables:
			insert_table(table,dbName)




###############################################################################

tables = [
	{	    
	"tableName":"institucion",
	"amount" : 6 ,
    "columns": ["id","nombre","descrip"],
    "columnsType": ["txt","txt","txt"],
    "data" : {"id":"N","nombre":"institucion_N" ,"descrip":"descrip_N"}
	},
	{	    
	"tableName":"voluntario",
	"amount" : 1000 ,
    "columns": ["id","nombre","fnacimiento"],
    "columnsType": ["txt","txt","date"],
    "data" : { "id":"N","nombre":"Voluntario_N" ,"fnacimiento":["1970-01-01","2002-01-01"] }
	},
	{	    
	"tableName":"habilidad",
	"amount" : 20 ,
    "columns": ["id","descrip"],
    "columnsType": ["txt","txt"],
    "data" : {"id":"N","descrip":"Habilidad_N" }
	},
	{	    
	"tableName":"estado_tarea",
	"amount" : 3 ,
    "columns": ["id","descrip"],
    "columnsType": ["txt","txt"],
    "data" : {"id":"N","descrip":"estado_N" }
	},	
	{	    
	"tableName":"emergencia",
	"amount" : 6,
    "columns": ["id","nombre","descrip","finicio","ffin","id_institucion"],
    "columnsType": ["txt","txt","txt","date","date","dep"],
    "data" : {"id":"N", "nombre":"emergencia_N","descrip":"descrip_N","finicio":["1990-01-01","2000-01-01"],"ffin":["2000-01-01","2020-01-01"],
    		"id_institucion":{"tableName":"institucion","columnId":0}
    	},
	},	
	{	    
	"tableName":"eme_habilidad",
	"amount" : 20,
    "columns": ["id","id_emergencia","id_habilidad"],
    "columnsType": ["txt","dep","dep"],
    "data" : {"id":"N", "id_emergencia":{"tableName":"emergencia","columnId":0}, "id_habilidad":{"tableName":"habilidad","columnId":0}},
	},	
	{	    
	"tableName":"vol_habilidad",
	"amount" : 20000,
    "columns": ["id","id_voluntario","id_habilidad"],
    "columnsType": ["txt","dep","dep"],
    "data" : {"id":"N", "id_voluntario":{"tableName":"voluntario","columnId":0}, "id_habilidad":{"tableName":"habilidad","columnId":0}},
	},
	{	    
	"tableName":"tarea",
	"amount" : 100,
    "columns": ["id","nombre","descrip","cant_vol_requeridos","cant_vol_inscritos","id_emergencia","finicio","ffin","id_estado"],
    "columnsType": ["txt","txt","txt","rnd","rnd","dep","date","date","dep"],
    "data" : {
    	"id":"N",
    	"nombre":"tarea_N",
    	"descrip":"descrip_N", 
    	"cant_vol_requeridos":[16,20],
    	"cant_vol_inscritos":[10,16],
    	"id_emergencia":{"tableName":"emergencia","columnId":0}, 
    	"finicio":["1990-01-01","2000-01-01"],
    	"ffin":["2000-01-01","2020-01-01"],
    	"id_estado":{"tableName":"estado_tarea","columnId":0}
    	}
	},
	{	    
	"tableName":"tarea_habilidad",
	"amount" : 200,
    "columns": ["id","id_emehab","id_tarea"],
    "columnsType": ["txt","dep","dep"],
    "data" : {
    	"id":"N",
    	"id_emehab":{"tableName":"eme_habilidad","columnId":0}, 
    	"id_tarea":{"tableName":"tarea","columnId":0}
    	}
	},
	{	    
	"tableName":"ranking",
	"amount" : 2000,
    "columns": ["id","id_voluntario","id_tarea","puntaje","flg_invitado","flg_participa"],
    "columnsType": ["txt","dep","dep","rnd","rnd","rnd"],
    "data" : {
    	"id":"N",
    	"id_voluntario":{"tableName":"voluntario","columnId":0}, 
    	"id_tarea":{"tableName":"tarea","columnId":0},
    	"puntaje":[0,100],
    	"flg_invitado":[1,2],
    	"flg_participa":[1,2]
    	}
	}
]

dbName = "voluntariadodb"
swDelete = 'n'  
swInsert = 'y' 
main(tables, dbName, swDelete, swInsert)
