# :hamburger: El Rincón Gaditano - Backend API :pizza:

Esta API REST ha sido desarrollada utilizando **Java y Spring Boot**, implementando una arquitectura robusta, segura y escalable para gestionar el catálogo de productos, clientes y pedidos.

Esta API REST se encuentra desplegada en Render, por lo que para utilizarla únicamente habría que usar esta url: `https://rincongaditano-backend.onrender.com` y llamar a los endpoints que se indican en la sección inferior.

---

## :book: Documentación de la API

A continuación, se detallan todos los endpoints disponibles en el sistema, estructurados por módulos. Todas las peticiones al entorno de producción deben realizarse a la URL base especificada en cada sección.

## :key: 1. Autenticación (`/auth`)

Manejo de sesiones, registro de clientes, reenvío de credenciales y activaciones de cuenta a través de correo electrónico.

* **URL base:** `https://rincongaditano-backend.onrender.com/auth`

---

#### :inbox_tray: Iniciar Sesión
`POST /login`

Permite a un usuario autenticarse en el sistema utilizando sus credenciales (email y contraseña) para obtener un token JWT de acceso.

##### Cuerpo de la Petición (JSON)
| Campo | Tipo | Requerido | Descripción |
| :--- | :--- | :---: | :--- |
| `email` | `String` | Sí | Correo electrónico del usuario. |
| `password` | `String` | Sí | Contraseña del usuario. |

##### Respuestas

* **`200 OK` (Autenticación exitosa):**
    ```json
    {
      "success": true,
      "data": {
        "id": 1,
        "email": "user@gmail.com",
        "role": "ROLE_USER",
        "token": "tokenjwt"
      },
      "message": "Login successful"
    }
    ```

* **`401 Unauthorized` (Credenciales incorrectas o usuario desactivado/sin verificar):**
    ```json
    {
      "success": false,
      "data": null,
      "message": "Error message"
    }
    ```

---

#### :memo: Registro de Usuario
`POST /register`

Registra un nuevo usuario en el sistema. Al registrarse, el usuario queda en estado sin verificar hasta que verifique su email.

##### Cuerpo de la Petición (JSON)
| Campo | Tipo | Requerido | Descripción |
| :--- | :--- | :---: | :--- |
| `name` | `String` | Sí | Nombre del usuario. |
| `secondName` | `String` | Sí | Apellidos del usuario. |
| `email` | `String` | Sí | Correo electrónico único. |
| `password` | `String` | Sí | Contraseña para la cuenta. |
| `address` | `String` | Sí | Dirección del usuario. |

##### Respuestas

* **`200 OK` (Registro guardado correctamente):**
    ```json
    {
      "success": true,
      "data": {
        "id": 1,
        "email": "user@gmail.com",
        "name": "Pepe",
        "secondName": "Pérez"
      },
      "message": "User registered successfully"
    }
    ```

* **`400 Bad Request` (El email ya existe o hay datos inválidos):**
    ```json
    {
      "success": false,
      "data": null,
      "message": "Error message"
    }
    ```

---

#### :email: Enviar Correo de Verificación
`POST /send-verification`

Solicita el reenvío manual del correo electrónico con el enlace de activación para un usuario existente.

##### Cuerpo de la Petición (JSON)
| Campo | Tipo | Requerido | Descripción |
| :--- | :--- | :---: | :--- |
| `email` | `String` | Sí | Correo electrónico del usuario que necesita activar la cuenta. |

##### Respuestas

* **`200 OK` (Correo enviado con éxito):**
    ```json
    {
      "success": true,
      "data": null,
      "message": "Verification email sent successfully"
    }
    ```

* **`500 Internal Server Error` (Fallo en el servicio de mensajería):**
    ```json
    {
      "success": false,
      "data": null,
      "message": "Error sending email: Mail server connection failed"
    }
    ```

---

#### :globe_with_meridians: Verificar Cuenta (Enlace del Email)
`GET /verify`

Este endpoint se consume directamente por el navegador web cuando el usuario pulsa el enlace recibido en su email.

##### Parámetros de la URL
| Parámetro | Tipo | Requerido | Descripción |
| :--- | :--- | :---: | :--- |
| `token` | `String` | Sí | Código de verificación único generado durante el registro. |

---

## :busts_in_silhouette: 2. Usuarios (`/users`)

Gestión de los usuarios existentes en el sistema.

*   **URL base:** `https://rincongaditano-backend.onrender.com/users`

---

#### :clipboard: Obtener Todos los Usuarios
`GET /users`

Recupera un listado completo con todos los usuarios registrados en el sistema.

##### Headers
| Header | Descripción |
| :--- |:--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** `ROLE_ADMIN`

##### Respuestas

*   **`200 OK` (Consulta exitosa):**
    ```json
    {
      "success": true,
      "data": [
        {
          "id": 1,
          "name": "Pepe",
          "secondName": "Pérez",
          "email": "user@gmail.com",
          "role": "ROLE_USER",
          "address": "Calle Ancha 12, Cádiz",
          "verified": true,
          "activated": true,
          "deleted": false,
          "points": 10
        }
      ],
      "message": "Users retrieved successfully."
    }
*   **`403 Forbidden` (Usuario no autorizado):**

---

#### :clipboard: Obtener Usuario por ID
`GET /users/{id}`

Recupera los detalles del perfil de un usuario usando su id.

##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** El usuario autenticado tiene que ser el propietario del perfil.

##### Respuestas

*   **`200 OK` (Perfil obtenido con éxito):**
    ```json
    {
      "success": true,
      "data": {
          "id": 1,
          "name": "Pepe",
          "secondName": "Pérez",
          "email": "user@gmail.com",
          "role": "ROLE_USER",
          "address": "Calle Ancha 12, Cádiz",
          "verified": true,
          "activated": true,
          "deleted": false,
          "points": 10
        },
      "message": "User retrieved successfully."
    }
*   **`403 Forbidden` (Usuario no autorizado):** El token pertenece a otro usuario.
*   **`404 Not Found` (Usuario no existe):**
    ```json
    {
      "success": false,
      "data": null,
      "message": "User not found with id: 5"
    }
---

#### :memo: Editar Usuario
`PUT /users/{id}`

Permite al usuario modificar sus datos personales.

##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** El usuario autenticado tiene que ser el propietario del perfil.

##### Cuerpo de la Petición (JSON)
| Campo | Tipo | Requerido | Descripción |
| :--- | :--- | :---: | :--- |
| `name` | `String` | Sí | Nombre del usuario. |
| `secondName` | `String` | Sí | Apellidos del usuario. |
| `address` | `String` | Sí | Dirección del usuario. |

##### Respuestas

*   **`200 OK` (Perfil editado con éxito):**
    ```json
    {
      "success": true,
      "data": {
          "id": 1,
          "name": "Pepe",
          "secondName": "Pérez",
          "email": "user@gmail.com",
          "role": "ROLE_USER",
          "address": "Calle Ancha 12, Cádiz",
          "verified": true,
          "activated": true,
          "deleted": false,
          "points": 10
        },
      "message": "User updated successfully."
    }
*   **`403 Forbidden` (Usuario no autorizado):** El token pertenece a otro usuario.
*   **`404 Not Found` (Usuario no existe):**
    ```json
    {
      "success": false,
      "data": null,
      "message": "User not found with id: 5"
    }
--- 

#### :wastebasket: Eliminar Usuario
`DELETE /users/{id}`

Eliminar un usuario del sistema.

##### Cabeceras (Headers)
##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** Estar autenticado en la plataforma y ser el propietario de la cuenta a borrar o tener el rol `ROLE_ADMIN`.

##### Respuestas

*   **`200 OK` (Usuario eliminado con éxito):**
    ```json
    {
      "success": true,
      "data": null,
      "message": "User deleted successfully"
    }
*   **`403 Forbidden` (Usuario no autorizado)**
*   **`404 Not Found` (Usuario no existe)**

--- 


#### :green_circle: Activar Usuario
`PUT /users/{id}/activate`

Activa un usuario desactivado.

##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** `ROLE_ADMIN`.

##### Respuestas

*   **`200 OK` (Usuario activado con éxito):**
    ```json
    {
      "success": true,
      "data": null,
      "message": "User activated successfully"
    }
*   **`403 Forbidden` (Usuario no autorizado)**
*   **`404 Not Found` (Usuario no existe)**

---

#### :red_circle: Activar Usuario
`PUT /users/{id}/deactivate`

Desactiva un usuario activado.

##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** `ROLE_ADMIN`.

##### Respuestas

*   **`200 OK` (Usuario desactivado con éxito):**
    ```json
    {
      "success": true,
      "data": null,
      "message": "User deactivated successfully"
    }
*   **`403 Forbidden` (Usuario no autorizado)**
*   **`404 Not Found` (Usuario no existe)**

---

## :file_folder: 3. Categorías (`/categories`)

Gestión de las categorías existentes en el sistema.

*   **URL base:** `https://rincongaditano-backend.onrender.com/categories`

---

#### :bar_chart: Obtener Todas las Categorías
`GET /categories`

Recupera el listado de todas las categorías existentes.

##### Headers
| Header | Descripción |
| :--- |:--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |

##### Permisos
*   **Requerido:** Público

##### Respuestas

*   **`200 OK` (Consulta exitosa):**
    ```json
    {
      "success": true,
      "data": [
        {
          "id": 1,
          "name": "Bocadillos",
          "image": "https://urlimage.com"
        }
      ],
      "message": "Categories retrieved successfully."
    }
---

#### :mag: Obtener  Categoría por ID
`GET /categories`

Recupera los detalles de una categoría usando su id.

##### Headers
| Header | Descripción |
| :--- |:--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |

##### Permisos
*   **Requerido:** Público

##### Respuestas

*   **`200 OK` (Consulta exitosa):**
    ```json
    {
      "success": true,
      "data": {
          "id": 1,
          "name": "Bocadillos",
          "image": "https://urlimage.com"
        },
      "message": "Categorie retrieved successfully."
    }
*   **`404 Not Found` (Categoría no existe)**
---

#### :memo: Crear Categoría
`POST /categories`

Registra una nueva categoría en el sistema

##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** `ROLE_ADMIN`.

##### Cuerpo de la Petición (JSON)
| Campo | Tipo | Requerido | Descripción |
| :--- | :--- | :---: | :--- |
| `name` | `String` | Sí | Nombre de la categoría. |
| `image` | `String` | No | Imagen de la categoría. |

##### Respuestas

* **`201 OK` (Registro guardado correctamente):**
    ```json
    {
      "success": true,
      "data": {
        "id": 1,
        "name": "Bocadillos",
        "image": "https://urlimage.com",
      },
      "message": "Category successfully"
    }
* **`400 Bad Request` (Datos inválidos):**
    ```json
    {
      "success": false,
      "data": null,
      "message": "Error message"
    }
---

#### :memo: Editar Categoría
`PUT /categories/{id}`

Modifica una categoría existente.

##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** `ROLE_ADMIN`.

##### Cuerpo de la Petición (JSON)
| Campo | Tipo | Requerido | Descripción |
| :--- | :--- | :---: | :--- |
| `name` | `String` | Sí | Nombre de la categoría. |
| `image` | `String` | No | Imagen de la categoría. |

##### Respuestas

* **`201 OK` (Registro guardado correctamente):**
    ```json
    {
      "success": true,
      "data": {
        "id": 1,
        "name": "Bocadillos",
        "image": "https://urlimage.com",
      },
      "message": "Category successfully"
    }
* **`400 Bad Request` (Datos inválidos):**
    ```json
    {
      "success": false,
      "data": null,
      "message": "Error message"
    }
*   **`403 Forbidden` (Usuario no autorizado)**
*   **`404 Not Found` (Categoría no existe)**
---

#### :wastebasket: Eliminar Categoría
`DELETE /categories/{id}`

Elimina una categoría existente.

##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** `ROLE_ADMIN`.

##### Respuestas

* **`200 OK` (Registro eliminado con éxito):**
    ```json
    {
      "success": true,
      "data": null,
      "message": "Category deleted successfully"
    }
*   **`403 Forbidden` (Usuario no autorizado)**
*   **`404 Not Found` (Categoría no existe)**
---

## :hamburger: 4. Productos (`/products`)

Gestión de los productos existentes en el sistema.

*   **URL base:** `https://rincongaditano-backend.onrender.com/products`

---

#### :bar_chart: Obtener Todos los Productos
`GET /products`

Recupera el listado de todos los productos existentes.

##### Headers
| Header | Descripción |
| :--- |:--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** `ROLE_ADMIN`.

##### Respuestas

*   **`200 OK` (Consulta exitosa):**
    ```json
    {
      "success": true,
      "data": [
        {
          "id": 1,
          "name": "Bocadillo de tortilla",
          "description": "Descripción",
          "price": 5.50,
          "available": true,
          "category": {"id":1, "name":"Bocadillos", "image":"https://urlimage.com"},
          "image": "https://urlimage.com"
        }
      ],
      "message": "Products retrieved successfully."
    }
*   **`403 Forbidden` (Usuario no autorizado)**
---

#### :white_check_mark: Obtener Productos Disponibles
`GET /products/available`

Recupera el listado de todos los productos que estén disponibles.

##### Headers
| Header | Descripción |
| :--- |:--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |

##### Permisos
*   **Requerido:** Público.

##### Respuestas

*   **`200 OK` (Consulta exitosa):**
    ```json
    {
      "success": true,
      "data": [
        {
          "id": 1,
          "name": "Bocadillo de tortilla",
          "description": "Descripción",
          "price": 5.50,
          "available": true,
          "category": {"id":1, "name":"Bocadillos", "image":"https://urlimage.com"},
          "image": "https://urlimage.com"
        }
      ],
      "message": "Products retrieved successfully."
    }
---

#### :file_folder: Obtener Productos Disponibles por Categoría
`GET /products/category/{category}`

Recupera el listado de todos los productos que estén disponibles de una categoría.

##### Headers
| Header | Descripción |
| :--- |:--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |

##### Permisos
*   **Requerido:** Público.

##### Respuestas

*   **`200 OK` (Consulta exitosa):**
    ```json
    {
      "success": true,
      "data": [
        {
          "id": 1,
          "name": "Bocadillo de tortilla",
          "description": "Descripción",
          "price": 5.50,
          "available": true,
          "category": {"id":1, "name":"Bocadillos", "image":"https://urlimage.com"},
          "image": "https://urlimage.com"
        }
      ],
      "message": "Products retrieved successfully."
    }
---

#### :file_folder: Obtener Productos por Categoría
`GET /products/admin/category/{category}`

Recupera el listado de todos los productos de una categoría.

##### Headers
| Header | Descripción |
| :--- |:--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** `ROLE_ADMIN`.

##### Respuestas

*   **`200 OK` (Consulta exitosa):**
    ```json
    {
      "success": true,
      "data": [
        {
          "id": 1,
          "name": "Bocadillo de tortilla",
          "description": "Descripción",
          "price": 5.50,
          "available": true,
          "category": {"id":1, "name":"Bocadillos", "image":"https://urlimage.com"},
          "image": "https://urlimage.com"
        }
      ],
      "message": "Products retrieved successfully."
    }
*   **`403 Forbidden` (Usuario no autorizado)**
---

#### :memo: Crear Producto
`POST /products`

Registra un nuevo producto en el sistema

##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** `ROLE_ADMIN`.

##### Cuerpo de la Petición (JSON)
| Campo | Tipo | Requerido | Descripción |
| :--- | :--- | :---: | :--- |
| `name` | `String` | Sí | Nombre del producto. |
| `description` | `String` | Sí | Descripción del producto. |
| `price` | `Double` | Sí | Precio del producto. |
| `available` | `Boolean` | Sí | Estado inicial del producto. |
| `categoryId` | `Long` | Sí | ID de la categoría a la que pertenece el producto. |
| `image` | `String` | No | Imagen del producto. |

##### Respuestas

* **`201 Created` (Registro guardado correctamente):**
    ```json
    {
      "success": true,
      "data": {
        "id": 1,
          "name": "Bocadillo de tortilla",
          "description": "Descripción",
          "price": 5.50,
          "available": true,
          "category": {"id":1, "name":"Bocadillos", "image":"https://urlimage.com"},
          "image": "https://urlimage.com"
      },
      "message": "Product created successfully"
    }
* **`400 Bad Request` (Datos inválidos):**
*   **`403 Forbidden` (Usuario no autorizado)**
---

#### :memo: Editar Producto
`PUT /products/{id}`

Edita un producto existente.

##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** `ROLE_ADMIN`.

##### Cuerpo de la Petición (JSON)
| Campo | Tipo | Requerido | Descripción |
| :--- | :--- | :---: | :--- |
| `name` | `String` | Sí | Nombre del producto. |
| `description` | `String` | Sí | Descripción del producto. |
| `price` | `Double` | Sí | Precio del producto. |
| `available` | `Boolean` | Sí | Estado inicial del producto. |
| `categoryId` | `Long` | Sí | ID de la categoría a la que pertenece el producto. |
| `image` | `String` | No | Imagen del producto. |

##### Respuestas

* **`200 OK` (Registro guardado correctamente):**
    ```json
    {
      "success": true,
      "data": {
        "id": 1,
          "name": "Bocadillo de tortilla",
          "description": "Descripción",
          "price": 5.50,
          "available": true,
          "category": {"id":1, "name":"Bocadillos", "image":"https://urlimage.com"},
          "image": "https://urlimage.com"
      },
      "message": "Product created successfully"
    }
* **`400 Bad Request` (Datos inválidos):**
*   **`403 Forbidden` (Usuario no autorizado)**
*   **`404 Not Found` (Producto no existe)**
---

#### :wastebasket: Eliminar Producto
`DELETE /products/{id}`

Elimina un producto existente.

##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** `ROLE_ADMIN`.

##### Respuestas

* **`200 OK` (Registro eliminado con éxito):**
    ```json
    {
      "success": true,
      "data": null,
      "message": "Product deleted successfully"
    }
*   **`403 Forbidden` (Usuario no autorizado)**
*   **`404 Not Found` (Producto no existe)**
---

#### :wastebasket: Editar disponibilidad
`PUT /products/{id}/switchAvailable`

Cambia la disponibilidad de un producto existente.

##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** `ROLE_ADMIN`.

##### Respuestas

* **`200 OK` (Registro editado con éxito):**
    ```json
    {
      "success": true,
      "data": null,
      "message": "Product availability changed successfully"
    }
*   **`403 Forbidden` (Usuario no autorizado)**
*   **`404 Not Found` (Producto no existe)**
---

AAAAAAAAAAAAAAAAAAAAAAAAAAA
## :package: 5. Pedidos (`/orders`)

Gestión de los pedidos del sistema.

* **URL base:** `https://rincongaditano-backend.onrender.com/orders`

---

##### :clipboard: Obtener Todos los Pedidos
`GET /orders`

Recupera el listado de todos los pedidos existentes.

##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** `ROLE_ADMIN`.

##### Respuestas
* **`200 OK` (Consulta exitosa):**
    ```json
    {
      "success": true,
      "data": [
        {
          "id": 1,
          "date": "2026-05-17T21:00:00",
          "status": "PENDING",
          "totalPrice": 11.50,
          "user": { "id": 1, "email": "user@gmail.com" },
          "lines": [
            {
              "id": 1,
              "product": { "id": 1, "name": "producto", "price": 8.50 },
              "amount": 1,
              "unitPrice": 8.50
            }
          ]
        }
      ],
      "message": "Orders retrieved successfully."
    }
---

##### :bust_in_silhouette: Obtener Todos los Pedidos de un Usuario
`GET /orders/user/{userId}`

Recupera el listado de todos los pedidos de un usuario.

##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** Propietario del perfil o usuario con el rol `ROLE_ADMIN`.

##### Respuestas
* **`200 OK` (Consulta exitosa):**
    ```json
    {
      "success": true,
      "data": [
        {
          "id": 1,
          "date": "2026-05-17T21:00:00",
          "status": "PENDING",
          "totalPrice": 11.50,
          "user": { "id": 1, "email": "user@gmail.com" },
          "lines": [
            {
              "id": 1,
              "product": { "id": 1, "name": "producto", "price": 8.50 },
              "amount": 1,
              "unitPrice": 8.50
            }
          ]
        }
      ],
      "message": "Orders retrieved successfully."
    }
*   **`403 Forbidden` (Usuario no autorizado)**
*   **`404 Not Found` (Usuario no existe)**

---

##### :bike: Obtener Todos los Pedidos con un estado
`GET /orders/status/{status}`

Recupera el listado de todos los pedidos que tengan un estado.

##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** `ROLE_ADMIN`.

##### Respuestas
* **`200 OK` (Consulta exitosa):**
    ```json
    {
      "success": true,
      "data": [
        {
          "id": 1,
          "date": "2026-05-17T21:00:00",
          "status": "PENDING",
          "totalPrice": 11.50,
          "user": { "id": 1, "email": "user@gmail.com" },
          "lines": [
            {
              "id": 1,
              "product": { "id": 1, "name": "producto", "price": 8.50 },
              "amount": 1,
              "unitPrice": 8.50
            }
          ]
        }
      ],
      "message": "Orders retrieved successfully."
    }
*   **`403 Forbidden` (Usuario no autorizado)**

---

#### :shopping_cart: Crear Pedido
`POST /orders`

Registra un nuevo pedido en el sistema.

##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** Usuario autenticado.

##### Cuerpo de la Petición (JSON)
| Campo | Tipo | Requerido | Descripción |
| :--- | :--- | :---: | :--- |
| `userId` | `Long` | Sí | Id del usuario que hace el pedido. |
| `items` | `List` | Sí | Lista de productos y cantidades. |
| `deliveryType` | `String` | Sí | Tipo de entrega. |


##### Respuestas

* **`201 Created` (Registro guardado correctamente):**
    ```json
    {
      "success": true,
      "data": [
        {
          "id": 1,
          "date": "2026-05-17T21:00:00",
          "status": "PENDING",
          "totalPrice": 11.50,
          "lines": [
            {
              "id": 1,
              "product": { "id": 1, "name": "producto", "price": 8.50 },
              "amount": 1,
              "unitPrice": 8.50
            }
          ]
        }
      ],
      "message": "Order created successfully."
    }
* **`400 Bad Request` (Datos inválidos):**
*   **`403 Forbidden` (Usuario no autorizado)**
---

#### :clipboard: Cambiar estado de un Pedido
`PUT /orders/{id}/status`

Cambia el estado de un pedido en el sistema.

##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** `ROLE_ADMIN`.

##### Cuerpo de la Petición (JSON)
| Campo | Tipo | Requerido | Descripción |
| :--- | :--- | :---: | :--- |
| `status` | `String` | Sí | Estado al que se cambia el pedido. |

##### Respuestas

* **`200 Ok` (Registro editado correctamente):**
    ```json
    {
      "success": true,
      "data": [
        {
          "id": 1,
          "date": "2026-05-17T21:00:00",
          "status": "ACCEPTED",
          "totalPrice": 11.50,
          "lines": [
            {
              "id": 1,
              "product": { "id": 1, "name": "producto", "price": 8.50 },
              "amount": 1,
              "unitPrice": 8.50
            }
          ]
        }
      ],
      "message": "Order status updated successfully."
    }
*   **`400 Bad Request` (Datos inválidos):**
*   **`403 Forbidden` (Usuario no autorizado)**
*   **`404 Not Found` (Pedido no existe)**
---

#### :no_entry_sign: Cancelar un Pedido
`PUT /orders/{id}/cancel`

Cancelar un pedido en el sistema.

##### Headers
| Header | Descripción |
| :--- | :--- |
| `Accept` | `application/json` |
| `Content-Type` | `application/json` |
| `Authorization` | `Bearer token` |

##### Permisos
*   **Requerido:** Propietario del pedido o usuario con el rol `ROLE_ADMIN`.

##### Respuestas

* **`200 Ok` (Registro editado correctamente):**
    ```json
    {
      "success": true,
      "data": null,
      "message": "Order cancelled successfully."
    }
*   **`400 Bad Request` (Datos inválidos):**
*   **`403 Forbidden` (Usuario no autorizado)**
*   **`404 Not Found` (Pedido no existe)**
---
