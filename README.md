<div align="center">
  <h1>Aceleración Alkemy - Proyecto para Ong "Somos Mas".</h1>
</div>

A continuación se presenta el proyecto elaborado para la fundación "Somos Más". El mismo consiste en el desarrollo BackEnd en el lenguaje de JAVA mediante el framework de SpringBoot. 
El objetivo del mismo consiste en nutrir el FrontEnd a fin de que la entidad pueda presentar sus actividades, compartir las novedades con su publico, obtener comentarios para mejorar su contenido como así tambien donaciones de aquellos interesados.

## Tutorial 📖

Podrás encontrar información de como usar la api desde la swagger UI, la cual puede ser accedida una vez levantada, en el path /api/doc. Si no sabes como levantar la api te dejamos un archivo en el root del proyecto, se llama [Tutorial.txt](Tutorial.txt) .

### Ingresando como Administrador

Para poder llevar a cabo el ingreso como "Administrador", deberá dirigirse al endpoint auth/login" e ingresar las siguientes credenciales:

* username: alkemy.grupo60@gmail.com
* password: alkemy123

Una vez registrado correctamente, recibirá un mail de bienvenida de nuestra parte y el correspondiente “token”, el que deberá copiar y utilizar en el botón “Authorize”.

A partir de este momento tendrá libertad para navegar por la interfaz de Open Api.

### Ingresando como Usuarios Regular.

También puede ingresar como usuario regular con alguno de los 10 diferentes usuarios que se crean por defecto al iniciar la api y que encontrará sus credenciales dentro del proyecto.

O si así lo desea puede crear su propio usuario desde auth/register, completando los campos requeridos. 

Cualquiera sea la opción elegida, deberá ingresar el Token proporcionado por la aplicación a fin de autorizar su acceso.

**Recuerde que en este caso no tendrá acceso total a aplicación.**


## Herramientas y Tecnologías

Está API utiliza los siguientes frameworks, librerías y herramientas de desarrollo:

* [SpringBoot](https://spring.io/) - El framework web utilizado
* [Maven](https://maven.apache.org/) - Gestionador de dependencias
* [GIT](https://git-scm.com/) - Versionador del projecto
* [MySql](https://www.mysql.com/) - Base de Datos.
* [Swagger](https://swagger.io/) - Documentacion del projecto
* [JUnit](https://junit.org/junit5/) - Testeo a la api
* [Mockito](https://site.mockito.org/) - Testeo a la api
* [Sendgrid](https://sendgrid.com/) - Envio de mails automatizado
* [AWS](https://aws.amazon.com/es/) - Almacenamiento de contenido


## Versionado

Para el versionado del proyecto decidimos utilizar la herramienta BitBucket. Esto debido a que se complementa muy bien con JIRA, lo cual permite realizar metodologías ágiles tales como scrum. Pero hemos traido este repositorio a Github para poder presentarlo publicamente.

## Autores 

* [Agustín Ezequiel Falabella](https://github.com/AgustinFalabella).
* [Enzo Julian Ruiz Díaz](https://github.com/enzoruizdiaz).
* [Juan Marcos Sandoval](https://github.com/juanmarcossandoval).
* [Patricio Daniel Diaz](https://github.com/patriciodanielDiaz).
* [Jean Pierre Crespin](https://github.com/Jpierre98).






