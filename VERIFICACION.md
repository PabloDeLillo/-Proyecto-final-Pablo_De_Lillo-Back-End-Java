# Verificación de la entrega

- Se revisó la estructura de paquetes, modelos, repositorios, servicios, controladores y DTO.
- `frontend/js/app.js`: comprobación de sintaxis con `node --check` (correcta).
- `backend/pom.xml`: validación XML (correcta).
- `backend/src/main/resources/static`: frontend copiado para servirlo desde Spring Boot.

**Limitación del entorno de preparación:** Java 21 está disponible, pero Maven y las dependencias de Spring Boot no están instaladas y el entorno no puede acceder a Maven Central; por tanto **no fue posible compilar ni probar la conexión real con MySQL** aquí. Debe ejecutarse `mvn clean package` y `mvn spring-boot:run` en un equipo con Maven y MySQL según el README antes de entregar.

**Importante:** Los usuarios pueden registrarse pero la aplicación no implementa inicio de sesión, Spring Security ni roles. No está destinada a desplegarse públicamente como tienda de producción.
