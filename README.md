# TechLab | Proyecto Final — Backend Java

**Desarrollador:** Pablo De Lillo  
**Tecnologías:** Java 21, Spring Boot 3.5, Maven, MySQL 8+, Spring Data JPA, HTML, CSS y JavaScript.

E-commerce web con catálogo, CRUD de productos y categorías, carrito, usuarios, historial, pedidos con estados y alertas de stock mínimo. La web es servida por Spring Boot desde `/`.

## Requisitos previos
- JDK 21 instalado (`java -version`).
- Maven 3.9+ instalado (`mvn -version`).
- MySQL Server 8+ en ejecución.

## Base de datos
1. Abrir MySQL Workbench y ejecutar `database/01_crear_base.sql` como administrador.
2. **Modificar** `CAMBIAR_ESTA_CLAVE` antes de ejecutar el script, y utilizar la misma clave en `DB_PASSWORD`.
3. Ejecutar el backend con las variables `DB_USER`, `DB_PASSWORD` y opcionalmente `DB_URL`.
4. Hibernate crea/actualiza las tablas al arrancar (`spring.jpa.hibernate.ddl-auto=update`). Para producción corresponde utilizar migraciones Flyway y no `update`.

###  Windows PowerShell
```powershell
cd backend
$env:DB_USER="techlab"
$env:DB_PASSWORD="TU_CLAVE_LOCAL"
mvn spring-boot:run
```

Abrir **http://localhost:8080/**. Para generar un JAR, `mvn clean package` y luego `java -jar target/techlab-backend-1.0.0.jar` con las mismas variables de entorno.

## Endpoints principales
| Método | Ruta | Acción |
|---|---|---|
| GET | `/api/productos` | Lista o filtra usando `?nombre=mouse` |
| GET | `/api/productos/{id}` | Detalle |
| GET | `/api/productos/buscar?nombre=...` | Buscar por nombre |
| POST | `/api/productos` | Crear |
| PUT | `/api/productos/{id}` | Editar |
| DELETE | `/api/productos/{id}` | Eliminar (si no tiene historial vinculado) |
| GET, POST | `/api/categorias` | Listar / crear |
| GET, PUT, DELETE | `/api/categorias/{id}` | Detalle / editar / eliminar |
| GET, POST | `/api/usuarios` | Listar / registrar |
| GET | `/api/usuarios/{id}/pedidos` | Historial por usuario |
| POST, GET | `/api/pedidos` | Registrar pendiente / listar |
| GET | `/api/pedidos/{id}` | Consultar pedido |
| PATCH | `/api/pedidos/{id}/estado` | Cambiar estado |
| GET | `/api/productos/stock-bajo` | Alertas de inventario |

### JSON
Crear producto:
```json
{"nombre":"Teclado mecánico","descripcion":"RGB compacto","precio":72500,"stock":8,"stockMinimo":2,"categoriaId":2,"imagenUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSTpm0o97U3m2mafdY4zTFIOW_nSXg-tqW_yQMKmsm6uw&s=10"}
```
Crear usuario:
```json
{"nombre":"Cliente Demo","email":"cliente@example.com"}
```
Crear pedido pendiente:
```json
{"usuarioId":1,"lineas":[{"productoId":1,"cantidad":2}]}
```
Confirmar pedido:
```http
PATCH /api/pedidos/1/estado
Content-Type: application/json

{"estado":"CONFIRMADO"}
```

## Reglas de pedidos y stock
- Crear el pedido lo deja `PENDIENTE` y **no descuenta stock**.
- Al pasar de `PENDIENTE` a `CONFIRMADO`, se valida y descuenta stock atómicamente usando bloqueos de base de datos.
- `CONFIRMADO` puede pasar a `ENVIADO` o `CANCELADO`. Cancelar desde `CONFIRMADO` repone stock.
- `ENVIADO` solo puede pasar a `ENTREGADO`. `ENTREGADO` y `CANCELADO` son estados finales.
- Cada línea guarda el precio que tenía el producto al crear el pedido.
- La eliminación de productos vinculados a pedidos históricos está restringida por la base de datos para conservar el historial.


**Autor: Pablo De Lillo.**
