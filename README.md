# AlmondiNimes 🌰

AlmondiNimes es una aplicación Android dedicada al seguimiento de Anime y Manga, permitiendo a los usuarios descubrir novedades, gestionar sus listas personales y conectar con amigos.

## 🚀 Características Principales

- **Novedades en Tiempo Real**: Sección central que combina los próximos estrenos de Anime y Manga actualmente en publicación, utilizando la API de Jikan (MyAnimeList).
- **Detalle de Obras**: Información exhaustiva sobre cada título, incluyendo sinopsis, géneros, puntuación y fechas de emisión.
- **Gestión de Listas**: (En progreso) Seguimiento personalizado de lo que estás viendo o leyendo.
- **Social**: Sistema de amigos, perfiles y notificaciones.
- **Persistencia de Sesión**: Inicio de sesión persistente para una mejor experiencia de usuario.
- **Diseño Moderno**: Interfaz basada en Fragments con navegación centralizada y Splash Screen animada.

## 🛠️ Stack Tecnológico

- **Lenguaje**: Kotlin
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Networking**: Retrofit 2 & OkHttp
- **Imágenes**: Glide
- **API**: [Jikan API](https://jikan.moe/) (v4)
- **UI**: Material Design, XML Layouts

## 📂 Estructura del Proyecto

```text
app/src/main/java/com/example/almondinimes/
├── activitys/
│   ├── MainActivity.kt
│   ├── MainActivity_Principal.kt
│   └── SplashActivity.kt
├── adapters/
│   ├── AmigosAdapter.kt
│   ├── BusquedaAmigosAdapter.kt
│   ├── HomeNoticiasAdapter.kt
│   ├── MyObrasAdapter.kt
│   ├── NoticiasAdapter.kt
│   └── NotificacionesAdapter.kt
├── fragments/
│   ├── Fragment_AboutUs.kt
│   ├── Fragment_Advertencias.kt
│   ├── Fragment_Amigos.kt
│   ├── Fragment_Amigos_De_Amigo.kt
│   ├── Fragment_Anime.kt
│   ├── Fragment_Ayuda.kt
│   ├── Fragment_Comunidad.kt
│   ├── Fragment_Detalle_Obra.kt
│   ├── Fragment_Editar_Perfil.kt
│   ├── Fragment_FAQ.kt
│   ├── Fragment_Lista_Amigo.kt
│   ├── Fragment_Login.kt
│   ├── Fragment_Manga.kt
│   ├── Fragment_Novedades.kt
│   ├── Fragment_Perfil.kt
│   ├── Fragment_Perfil_Amigo.kt
│   ├── Fragment_Register.kt
│   ├── Fragment_Tus_Animes.kt
│   └── Fragment_Tus_Mangas.kt
├── models/
│   ├── Aired.kt
│   ├── Anime.kt
│   ├── Genre.kt
│   ├── JikanFullResponse.kt
│   ├── JikanImages.kt
│   ├── JikanJpg.kt
│   ├── JikanNewsResponse.kt
│   ├── JikanResponse.kt
│   ├── NewsImages.kt
│   ├── NewsItem.kt
│   ├── NewsJpg.kt
│   ├── Noticia.kt
│   ├── Notificacion.kt
│   ├── ObraGuardada.kt
│   ├── TipoNotificacion.kt
│   └── Usuario.kt
├── network/
│   ├── JikanApiService.kt
│   └── RetrofitClient.kt
└── viewmodels/
    ├── AmigosViewModel.kt
    ├── NotificacionesViewModel.kt
    └── ObrasViewModel.kt

app/src/main/res/
├── layout/
│   ├── activity_main.xml
│   ├── activity_main_principal.xml
│   ├── activity_splash.xml
│   ├── dialog_add_friend.xml
│   ├── dialog_edit_item.xml
│   ├── fragment_about_us.xml
│   ├── fragment_advertencias.xml
│   ├── fragment_amigos.xml
│   ├── fragment_anime.xml
│   ├── fragment_ayuda.xml
│   ├── fragment_comunidad.xml
│   ├── fragment_detalle_obra.xml
│   ├── fragment_editar_perfil.xml
│   ├── fragment_f_a_q.xml
│   ├── fragment_friend_item_list.xml
│   ├── fragment_login.xml
│   ├── fragment_manga.xml
│   ├── fragment_novedades.xml
│   ├── fragment_perfil.xml
│   ├── fragment_perfil_amigo.xml
│   ├── fragment_register.xml
│   ├── fragment_tus_animes.xml
│   ├── fragment_tus_mangas.xml
│   ├── item_advertencias.xml
│   ├── item_amigo.xml
│   ├── item_anime_manga.xml
│   ├── item_foro.xml
│   ├── item_friend_search_result.xml
│   ├── item_noticias.xml
│   ├── item_notificacion.xml
│   ├── layout_empty_state.xml
│   ├── nav_header_principal.xml
│   └── spinner_item_layout.xml
├── navigation/
│   ├── nav_graph_login.xml
│   └── nav_graph_principal.xml
└── values/
    ├── colors.xml
    ├── strings.xml
    └── themes.xml
```

## ⚙️ Instalación

1. Clona el repositorio.
2. Abre el proyecto en **Android Studio (Ladybug o superior)**.
3. Sincroniza el proyecto con Gradle.
4. Ejecuta la aplicación en un emulador o dispositivo físico.

---
*Desarrollado por Lucas Ruiz Martínez*
