# Sistema Voluntariado

Este es el archivo README para el software "Sistema Voluntariado". Aquí encontrarás instrucciones detalladas sobre cómo configurar y ejecutar el software en tu entorno local.

## Requisitos previos

- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [Visual Studio Code](https://code.visualstudio.com/)
- [PostgreSQL](https://www.postgresql.org/)
- [pgAdmin4](https://www.pgadmin.org/)
- [Yarn](https://yarnpkg.com/)
- [Node.js](https://nodejs.org/)
- [Vue.js](https://vuejs.org/)

## Configuración del entorno

### Backend

1. Clona el repositorio en tu máquina local.

2. Navega a la carpeta `backend_gestion_voluntarios` y abre el proyecto en IntelliJ IDEA.

3. Configura tu base de datos PostgreSQL. Abre pgAdmin4 y crea una nueva base de datos llamada `voluntariodb`.

4. Realiza un RESTORE de la base de datos utilizando el archivo `voluntariosdb_backup.sql` que se encuentra en la carpeta del backend.

5. Puebla la tabla "División Regional" utilizando el script SQL llamado `division_regional.sql` que se encuentra en la misma carpeta.

6. Asegúrate de que las credenciales de la base de datos en el archivo de configuración del proyecto coincidan con las de tu base de datos PostgreSQL.

7. Ejecuta un build del proyecto utilizando Gradle. Esto se puede hacer desde la interfaz de IntelliJ IDEA.

8. Finalmente, ejecuta la aplicación presionando el botón de play en la clase `AppVoluntarioApplication`.

### Frontend

1. Navega a la carpeta `frontend_gestion_voluntarios_vuetify` dentro del repositorio clonado.

2. Abre el proyecto en Visual Studio Code.

3. Abre una consola en la carpeta del proyecto.

4. Instala Yarn si aún no lo has hecho. Puedes hacerlo con el siguiente comando:

    ```
    npm install --global yarn
    ```

5. Ejecuta el siguiente comando para instalar las dependencias del proyecto:

    ```
    yarn install
    ```

6. Finalmente, inicia la aplicación con el siguiente comando:

    ```
    yarn dev
    ```

Ahora deberías poder acceder a la aplicación web en la dirección predeterminada [http://localhost:3000/](http://localhost:3000/).

## Uso

Una vez que la aplicación esté en funcionamiento, puedes navegar a la dirección web proporcionada para comenzar a usar el "Sistema Voluntariado".
