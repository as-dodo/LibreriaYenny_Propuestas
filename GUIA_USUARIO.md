# Guía rápida de uso (Yenny Propuestas)

## 1. Inicio de sesión y registro
- Abre la app y elige **Iniciar sesión** o **Registrarse**.
- Las cuentas nuevas se crean como *Escritor*. Ingresa nombre, email y contraseña (≥ 6).
- Los roles existentes:
  - **Escritor**: envía propuestas.
  - **Editor**: revisa, comenta, aprueba/rechaza, define condiciones.
  - **Admin**: gestiona usuarios y precios de libros aprobados.

## 2. Escritor
- **Nueva propuesta**: botón *Nueva Propuesta*.
  - Completa *Título* y *Resumen*.
  - Adjunta archivo (doc/pdf, opcional): pulsa **Adjuntar…**, se copia a la carpeta local `files/`.
  - Guarda. El estado inicial es *ENVIADA*.
- **Mis propuestas**: lista con estado y fecha para seguimiento.

## 3. Editor
- Pestaña **Propuestas**:
  - *Bandeja* muestra enviadas/en revisión. Asignar con **Asignar Editor**.
  - Abrir detalle con **Revisar**: ver datos, descargar archivo, comentar, aprobar o rechazar.
- **Definir condiciones** (solo aprobadas):
  - En la lista, selecciona y pulsa **Definir Condiciones**.
  - Dialogo: tirada inicial, % autor, precio por ejemplar, observaciones (opcional).
- **Títulos**: crear título desde propuesta aprobada, transferir a Marketing o actualizar estado de comercialización.
- **Reportes**: estadísticas generales, propuestas filtradas por estado, top escritores.

## 4. Admin
- **Usuarios**: crear, editar datos/rol, eliminar.
- **Libros aprobados**: tabla con título, autor, precio, tirada, estados. Selecciona y usa **Editar precio** para actualizar.

## 5. Archivos
- Se almacenan en `files/` dentro de la carpeta del proyecto (ruta relativa guardada en BD).
- Editor puede descargarlos desde el diálogo de revisión eligiendo dónde guardarlos.

## 6. Requisitos técnicos
- Base de datos MySQL con esquema de `sql/*.sql'
- El formulario usa Swing sobre Java (sin Maven/Gradle); ejecutar `Main` desde `app/src/Main.java`.
