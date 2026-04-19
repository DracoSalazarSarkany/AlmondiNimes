# AlmondiNimes 🌙

AlmondiNimes es una aplicación Android moderna diseñada para que los entusiastas del anime y manga puedan gestionar sus listas, descubrir novedades y conectar con amigos. El proyecto utiliza una arquitectura robusta basada en MVVM y aprovecha el ecosistema de Firebase y la API de Jikan.

## 🚀 Tecnologías y Arquitectura

- **Arquitectura:** MVVM (Model-View-ViewModel).
- **Lenguaje:** Kotlin con Coroutines para operaciones asíncronas.
- **Base de Datos y Auth:** Firebase Authentication y Cloud Firestore.
- **Red:** Retrofit para el consumo de la API de Jikan (MyAnimeList).
- **UI/UX:** 
    - Diseño Oscuro (Dark Theme) coherente.
    - Jetpack Navigation para un flujo de pantallas fluido.
    - Componentes personalizados (Spinners optimizados para modo oscuro).
    - Glide para la carga eficiente de imágenes.

## 📁 Estructura Completa del Proyecto

A continuación se detalla la organización del proyecto y el propósito de cada archivo:

```text
AlmondiNimes/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/almondinimes/
│   │   │   │   ├── activitys/
│   │   │   │   │   ├── MainActivity.kt              # Flujo de autenticación (Login/Registro).
│   │   │   │   │   ├── MainActivity_Principal.kt    # Núcleo de la app (Drawer, BottomNav, Navegación).
│   │   │   │   │   └── SplashActivity.kt            # Pantalla de carga y redirección inicial.
│   │   │   │   ├── adapters/
│   │   │   │   │   ├── AmigosAdapter.kt             # Renderizado de listas de usuarios.
│   │   │   │   │   ├── BusquedaAmigosAdapter.kt     # Especializado en resultados de búsqueda.
│   │   │   │   │   ├── MyObrasAdapter.kt            # Adaptador principal para Anime/Manga.
│   │   │   │   │   ├── NotificacionesAdapter.kt     # Gestión visual del sistema de avisos.
│   │   │   │   │   ├── NovedadesAdapter.kt          # Adaptador de noticias de la industria.
│   │   │   │   │   └── ObrasBusquedaAdapter.kt      # Resultados de búsqueda global de la API.
│   │   │   │   ├── fragments/
│   │   │   │   │   ├── Fragment_AboutUs.kt          # Información sobre el proyecto.
│   │   │   │   │   ├── Fragment_Advertencias.kt     # Avisos legales y de uso.
│   │   │   │   │   ├── Fragment_Amigos.kt           # Lista de amigos y buscador dinámico (Nick/ID).
│   │   │   │   │   ├── Fragment_Amigos_De_Amigo.kt  # Exploración de la red social de contactos.
│   │   │   │   │   ├── Fragment_Anime.kt            # Buscador global de Anime.
│   │   │   │   │   ├── Fragment_Ayuda.kt            # Soporte técnico y guías.
│   │   │   │   │   ├── Fragment_Comunidad.kt        # Espacio de interacción social.
│   │   │   │   │   ├── Fragment_Detalle_Obra.kt     # Ficha técnica de Jikan y añadir a lista.
│   │   │   │   │   ├── Fragment_Editar_Perfil.kt    # Modificación de datos personales.
│   │   │   │   │   ├── Fragment_FAQ.kt              # Preguntas frecuentes.
│   │   │   │   │   ├── Fragment_Lista_Amigo.kt      # Colección de un amigo (Modo lectura).
│   │   │   │   │   ├── Fragment_Login.kt            # Gestión de acceso y seguridad de sesión.
│   │   │   │   │   ├── Fragment_Manga.kt            # Buscador global de Manga.
│   │   │   │   │   ├── Fragment_MisListas.kt        # Gestión de colección personal (CRUD).
│   │   │   │   │   ├── Fragment_Novedades.kt        # Feed de noticias de la industria.
│   │   │   │   │   ├── Fragment_Perfil.kt           # Vista de perfil del usuario actual.
│   │   │   │   │   ├── Fragment_Perfil_Amigo.kt     # Ficha pública de otros usuarios.
│   │   │   │   │   ├── Fragment_Register.kt         # Registro de nuevos usuarios y perfiles.
│   │   │   │   │   └── NavegacionUtil.kt            # Funciones auxiliares de navegación.
│   │   │   │   ├── models/
│   │   │   │   │   ├── Usuario.kt                   # Perfil de usuario en Firestore.
│   │   │   │   │   ├── ObraGuardada.kt              # Datos de obras persistidas.
│   │   │   │   │   ├── Notificacion.kt              # Modelo para avisos internos.
│   │   │   │   │   └── (Data Classes API)           # Anime, Genre, JikanResponse, Noticia, etc.
│   │   │   │   ├── network/
│   │   │   │   │   ├── JikanApiService.kt           # Endpoints de la API Jikan.
│   │   │   │   │   └── RetrofitClient.kt            # Configuración de red y Retrofit.
│   │   │   │   ├── utils/
│   │   │   │   │   └── EndlessScrollListener.kt     # Paginación automática para listas.
│   │   │   │   ├── viewmodels/
│   │   │   │   │   ├── AmigosViewModel.kt           # Lógica social y búsquedas en Firestore.
│   │   │   │   │   ├── NotificacionesViewModel.kt   # Lógica del sistema de notificaciones.
│   │   │   │   │   └── ObrasViewModel.kt            # Gestión de la base de datos de obras.
│   │   │   │   └── AlmondiApplication.kt            # Clase de aplicación principal.
│   │   │   ├── res/
│   │   │   │   ├── color/                           # Estados de color para BottomNav.
│   │   │   │   ├── drawable/                        # Recursos gráficos y backgrounds.
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_*.xml               # Layouts de actividades.
│   │   │   │   │   ├── fragment_*.xml               # Layouts de pantallas.
│   │   │   │   │   ├── item_*.xml                   # Diseños de fila para listas.
│   │   │   │   │   ├── dialog_*.xml                 # Ventanas emergentes de acción.
│   │   │   │   │   ├── spinner_item_layout.xml      # Layout para Spinner (Modo Oscuro).
│   │   │   │   │   └── layout_empty_state.xml       # Estado vacío para listas.
│   │   │   │   ├── menu/                            # Configuración de menús de navegación.
│   │   │   │   ├── navigation/                      # Grafos de navegación (Login/Principal).
│   │   │   │   └── values/                          # Strings, colores y temas (Light/Night).
│   │   │   └── AndroidManifest.xml                  # Manifiesto del sistema.
│   │   ├── androidTest/                             # Tests instrumentados.
│   │   └── test/                                    # Tests unitarios.
│   ├── build.gradle.kts                             # Build script del módulo app.
│   └── .gitignore
├── build.gradle.kts                                 # Build script del proyecto.
├── settings.gradle.kts                              # Configuración de módulos.
└── ...
```

## 🛠️ Detalles de Implementación Destacados

- **Seguridad y Persistencia:** Lógica en `Fragment_Login` para validar la sesión de Firebase contra las preferencias del usuario ("Recordar"), forzando el cierre de sesión si es necesario para mayor privacidad.
- **Búsqueda Multi-Filtro:** `AmigosViewModel` permite localizar usuarios mediante Nick (prefijo), ID numérico directo (#ID) o el tag completo (Nick#ID).
- **UI Adaptativa:** Uso de layouts personalizados para Spinners para garantizar que el texto sea blanco y legible en el tema oscuro de la aplicación.

---
© 2026 Lucas Ruiz
