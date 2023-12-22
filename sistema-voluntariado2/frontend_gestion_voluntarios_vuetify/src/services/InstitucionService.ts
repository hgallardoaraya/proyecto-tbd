import { API_URL } from "@/globals";
import Institucion from "@/interfaces/Institucion";

const getInstituciones = async () => {
    const rawResponse = await fetch(`${API_URL}/instituciones`, {
      method: 'GET',
      headers: {
        'Accept': 'Application/json'
      }
    });
    const instituciones:Institucion[] = await rawResponse.json();
    return instituciones;
}

export { getInstituciones };
  