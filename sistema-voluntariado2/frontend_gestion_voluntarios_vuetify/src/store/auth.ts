import { API_URL } from '@/globals';
import TokenPayload from '@/interfaces/TokenPayload';
import { parseJwt } from '@/services/JwtService';
import {defineStore} from 'pinia';
import router from '@/router';

const tokenPayload:TokenPayload = {}

export const useAuth = defineStore('auth', {
    state: () => {
        return {
            token: null,
            tokenPayload
        }
    },
    actions: {
        async register(nombre:string, apellido:string, email:string, password:string, idInstitucion:number, voluntario:boolean, coordinador:boolean, latit:number, longit:number){
            const uri = `${API_URL}/register`            
            const rawResponse = await fetch(uri, {
                method: 'POST',
                headers: {
                    'Content-Type': 'Application/json',
                    'Accept': 'Application/json'
                },
                body: JSON.stringify({
                    nombre,
                    apellido,
                    email,
                    password,
                    idInstitucion,
                    voluntario,
                    coordinador,
                    latit,
                    longit

                })
            })
            const response = await rawResponse.json();            
            return response;

        },
        async login(email:string, password:string, idRol:number){
            const uri = `${API_URL}/login`
            const rawResponse = await fetch(uri, {
                method: 'POST',
                headers: {
                    'Content-Type': 'Application/json',
                    'Accept': 'Application/json'
                },
                body: JSON.stringify({
                    email, password, idRol
                })
            })            
            const response = await rawResponse.json();
                        
            if(response.status == 200){
                this.token = response.data.token;
                this.tokenPayload = this.token ? parseJwt(this.token) : null;            
            }            

            return response;
            //TO DO MANAGE RESPONSE
        },
        logout(){
            this.token = null;
            this.tokenPayload = {};
            router.push('/login');            
        }
    },
    persist: {
        enabled: true
    }
})