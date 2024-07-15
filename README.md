
Crear una aplicación móvil tipo Master-Detail para Android con Kotlin y API mínima 23 en un repositorio.

La aplicación debe contar con una actividad principal y vistas (composables) para mostrar la funcionalidad pedida.

Utilizar control de versiones.

Agregar un commit al inicio de la prueba y un commit adicional al cierre de cada día de actividades con los últimos cambios realizados.

Actividades

1.

Crear un composable que permita mostrar una imagen circular a partir de una URL, las letras iniciales de una cadena compuesta por una o varias palabras o un placeholder.

Sólo considerar las primeras dos palabras de la cadena.

1.1.

Si la url está vacía o no se puede recuperar la imagen, mostrar las iniciales.

1.2.

Si no se puede mostrar la imagen y la cadena está vacía mostrar un placeholder.

1.3.

Si sólo se tiene una palabra, mostrar sólo una inicial.

1.4.

Si no se puede mostrar la imagen y la primera palabra comienza con número o un carácter fuera del alfabeto, mostrar un placeholder.

1.5.

Las iniciales se deben mostrar en mayúsculas.

1.6.

Permitir definir un background y color para las iniciales.

1.7.

Permitir definir un placeholder.

2.

Utilizando la API pokeapi v2 consumir el endpoint pokemon/ con paginación de 25

elementos.





2.1.

Con la misma API obtener el detalle de cada pokémon y persistir al menos su id, nombre, imagen, sprite frontal por defecto, altura, peso, nombre de los tipos del pokémon.

[detalle](detalle.png)
2.2.

En una pantalla mostrar un listado vertical con el nombre y el sprite del pokémon.
[catalogo](pantallaInicio.png)
2.3.

Utilizar el componente de la actividad 1 para mostrar la imagen, la inicial del nombre o el placeholder.

2.4.

Al llegar al final del listado obtener los siguientes 25 elementos.

2.5.

La información debe estar disponible cuando el dispositivo no tenga conexión a internet.

2.6.

Al dar clic sobre un item de la lista abrir la pantalla detalle (ver actividad 3).

2.7.

Permitir marcar/desmarcar un pokémon como favorito desde la pantalla principal.

3.

Crear una pantalla para mostrar el detalle con la información del pokémon.

3.1.

Agregar un botón que permita marcar/desmarcar un favorito.



4.

Agregar un módulo a la aplicación con el nombre: mylocations.

[menu](menu_abbre_mapa.png)
[permisos](solicitaPermisosParaVerUbicacion.png)
[listado](geopunto_con_tiempo.png)
[mapa](mapa.png)
[console](Cloud_Firestore.png)

4.1.

En este módulo agregar una pantalla donde se solicite la ubicación del dispositivo y -utilizando un proyecto de Firebase- persistirla en Cloud Firestore cada 2 minutos.

4.2.

Mantener el envío de información mientras la aplicación se encuentra en segundo plano o es finalizada por el usuario (deseable).

4.3.

En un mapa mostrar las ubicaciones enviadas.

4.4.

En una lista mostrar la fecha y hora de envío de la ubicación así como sus coordenadas.

4.5.

Permitir abrir la pantalla de la actividad 4 desde la pantalla principal de la actividad 1.

Considerar:

● Buenas prácticas de desarrollo

● Arquitectura

● Patrones de diseño

● Prácticas y componentes Android recomendados

● Guías de diseño de Material Design

● Interfaz de usuario amigable e intuitiva

● Flujos alternos

● Evitar procesos bloqueantes

● Control de errores

● Inyección de dependencias

● Programación reactiva

● Modularización

● Control de versiones

● Testing

● Preview de compostables








