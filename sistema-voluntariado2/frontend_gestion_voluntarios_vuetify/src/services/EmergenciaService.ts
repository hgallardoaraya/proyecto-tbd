import { API_URL } from "@/globals"
import Emergencia from '@/interfaces/Emergencia'
import Usuario from "@/interfaces/Usuario";
import { useAuth } from "@/store/auth";

const getEmergencias = async (token:string) => {
    if(token.length == 0) return [];
    const rawResponse = await fetch(`${API_URL}/emergencias`, {
      method: 'GET',
      headers: {
        'Accept': 'Application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    const emergencias:Emergencia[] = await rawResponse.json();    
    return emergencias;
}

const getVoluntariosEmergencia = async (idEmergencia:number) => {
	const auth = useAuth();
	if(auth.token == null) return [];
	const rawResponse = await fetch(`${API_URL}/emergencias/${idEmergencia}/voluntarios`, {
		method: 'GET',
		headers: {
			'Accept': 'Application/json',
			'Content-type': 'Application/json',
			'Authorization': `Bearer ${auth.token}`
		}		
	});
	const response = await rawResponse.json();
	console.log("emergencia" + response);
	return response;
}

export { getEmergencias, getVoluntariosEmergencia };
