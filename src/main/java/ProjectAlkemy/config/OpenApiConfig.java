package ProjectAlkemy.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(info = @Info(title = "ONG - Somos Más  ", version = "v1", description =
				 "# API desarrollada por Exception Five \r\n"
				+ "## Bienvenidos a nuestra API \r\n"
				+ "#### A continuación listamos una serie de instrucciones para facilitar su uso: \r\n"
				+ "1. Ir a auth/login e ingresar como usuario Administrador con las siguientes credenciales \r\n"
				+ "\r\n"
				+ "		  username: alkemy.grupo60@gmail.com \r\n"
				+ "\r\n"
				+ "		  password: alkemy123 \r\n"
				+ "2. También puede crear su propio usuario desde auth/register, completando los campos requeridos. _**Recuerde que en este caso no tendrá acceso total a aplicación.**_ \r\n"
				+ "\r\n"
				+ "3. Una vez registrado correctamente, recibirá un mail de bienvenida de nuestra parte y el correspondiente “token”, el que deberá copiar y utilizar en el botón “Authorize”.\r\n"
				+ "\r\n"
				+ "4. A partir de este momento tendrá libertad para navegar por la interfaz de Open Api, según la jerarquía de su usuario."))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP , scheme = "bearer",in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {
}
