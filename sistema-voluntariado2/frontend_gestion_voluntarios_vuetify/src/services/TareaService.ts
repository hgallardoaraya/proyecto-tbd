import { API_URL } from "@/globals"
import { useAuth } from '@/store/auth';
import router from "@/router";
import Tarea from "@/interfaces/Tarea";

const createTask = async ({nombre, descripcion, voluntariosRequeridos, fechaInicio, fechaFin, latit, longit}:Tarea, idEmergencia:number, idsHabilidades:number[], token:string) => {    
    const response = await fetch(`${API_URL}/tareas`,{
        method: 'POST',
        headers: {
            'Accept': 'Application/json',
            'Content-type': 'Application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
            nombre,
            descripcion,
            voluntariosRequeridos,
            fechaInicio,
            fechaFin,
            idEmergencia,
            idsHabilidades,
            latit,
            longit
        })
    })
    const rawResponse = await response.json();    
    return rawResponse;
}

const getTasks = async (token:string) => {
    const response = await fetch(`${API_URL}/tareas`,{
        method: 'GET',
        headers: {
            'Accept': 'Application/json',        
            'Authorization': `Bearer ${token}`
        }        
    })
    const rawResponse = await response.json();
    return rawResponse;
}

const getTask = async (token:string, id:number) => {
    const response = await fetch(`${API_URL}/tareas/${id}`,{
        method: 'GET',
        headers: {
            'Accept': 'Application/json',        
            'Authorization': `Bearer ${token}`
        }
    })
    const rawResponse = await response.json();    
    return rawResponse;
}

const updateTask = async ({id, nombre, descripcion, voluntariosInscritos, voluntariosRequeridos, fechaInicio, fechaFin, latit, longit}:Tarea, idEmergencia:number, idEstado:number, token:string, idsHabilidades:number[]) => {
    console.log(latit, longit)
    const response = await fetch(`${API_URL}/tareas/${id}`,{
        method: 'PUT',
        headers: {
            'Accept': 'Application/json',
            'Content-type': 'Application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
            nombre,
            descripcion,
            voluntariosInscritos,
            voluntariosRequeridos,
            fechaInicio,
            fechaFin,
            idEstado,
            idEmergencia,
            idsHabilidades,
            latit,
            longit
        })
    })
    const rawResponse = await response.json();    
    return rawResponse;
}

const deleteTask = async (id: number) => {
    console.log(id);
    const auth = useAuth();
    const token = auth.token;
    console.log(token);
  
    const response = await fetch(`${API_URL}/tareas/${id}`, {
      method: 'DELETE',
      headers: {
        'Accept': 'Application/json',
        'Content-type': 'Application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    const rawResponse = await response.json();
    if(rawResponse.status == 200) alert("La tarea ha sido eliminada con éxito");
    router.push("/tasks")
    return rawResponse;
}

const getTasksRegion = async (nombreRegion:string) => {
    const auth = useAuth();
    const token = auth.token;   

    const response = await fetch(`${API_URL}/tareas/region`, {
        method: 'POST',
        headers: {
            'Accept': 'Application/json',
            'Content-type': 'Application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
            nombreRegion
        })
    });

    const rawResponse = await response.json();
    return rawResponse;
}

export { createTask, getTasks, getTask, updateTask, deleteTask, getTasksRegion }
