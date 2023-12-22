import { API_URL } from "@/globals";
import { useAuth } from "@/store/auth"

const getUsuario = async () => {
    const auth = useAuth();
    const rawResponse = await fetch(`${API_URL}/usuarios/${auth.tokenPayload.idUsuario}`,{
        method: 'GET',
        headers: {
            'Accept': 'Application/json',
            'Content-type': 'Application/json',
            'Authorization': `Bearer ${auth.token}`
        }
    })
    const response = await rawResponse.json();
    return response;
}

export { getUsuario }