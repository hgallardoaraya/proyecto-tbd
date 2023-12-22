import { API_URL } from "@/globals"
import Estado from "@/interfaces/Estado"

const getStateColor = (descripcion:string) => {
    if(descripcion == 'Pendiente') return 'default'
    if(descripcion == 'En desarrollo') return 'green'
    if(descripcion == 'En espera') return 'orange'
    if(descripcion == 'Cancelada') return 'red'
    if(descripcion == 'Terminada') return 'primary'
}

const getEstados = async (token:string) => {
    if(token.length == 0) return [];
    const rawResponse = await fetch(`${API_URL}/estados`, {
        method: 'GET',
        headers: {
            'Accept': 'Application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    const estados:Estado[] = await rawResponse.json();
    return estados;
}

export { getStateColor, getEstados };