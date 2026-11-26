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
| DELETE | `/api/productos/{id}` | Eliminar del catálogo; las líneas del pedido conservan el nombre, precio y cantidad históricos |
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

Pantallas principal

<img width="1320" height="2225" alt="localhost_8080_" src="https://github.com/user-attachments/assets/464df595-cc52-4095-b271-033299bd1a2f" />

Agregar usuarios

<img width="1314" height="765" alt="image" src="https://github.com/user-attachments/assets/7d34f0bd-2147-44ef-9b18-84eedb890c72" />

Carrito de compras

<img width="1326" height="633" alt="image" src="https://github.com/user-attachments/assets/b0575d64-c2fd-425a-ba2a-09e4e2120acb" />

Historial de pedidos

<img width="1331" height="601" alt="image" src="https://github.com/user-attachments/assets/09079733-d782-4c55-95f7-32f98292dcb4" />

Pedido cancelado 

<img width="1327" height="568" alt="image" src="https://github.com/user-attachments/assets/2c66b10b-0ef8-43d2-bccc-1fab1c70bf87" />

Pedido confirmado

<img width="1297" height="831" alt="image" src="https://github.com/user-attachments/assets/c5e4f929-5721-4103-9b34-ea180a05967c" />

Pedido enviado

<img width="1306" height="833" alt="image" src="https://github.com/user-attachments/assets/cdb9fb92-280b-4f09-9381-d87b55ce6c03" />

Pedido entregado

<img width="1319" height="796" alt="image" src="https://github.com/user-attachments/assets/0ec4c768-414f-4fde-8282-ce83083e2047" />

Categorias

<img width="1321" height="738" alt="image" src="https://github.com/user-attachments/assets/46a2abf7-ef72-4273-b97a-1b1e89c42a04" />

Administración

<img width="1320" height="1724" alt="localhost_8080_ (1)" src="https://github.com/user-attachments/assets/667c5899-504d-47c2-895f-387c46509777" />



**Autor: Pablo De Lillo.**
