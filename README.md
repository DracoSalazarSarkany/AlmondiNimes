# AlmondiNimes 🌙

AlmondiNimes es una aplicación Android moderna diseñada para que los entusiastas del anime y manga puedan gestionar sus listas, descubrir novedades y conectar con amigos. El proyecto está enfocado en el rendimiento, la escalabilidad y una experiencia de usuario fluida con un diseño oscuro elegante.

## 🚀 Tecnologías y Arquitectura

- **Arquitectura:** MVVM (Model-View-ViewModel).
- **Concurrencia:** Kotlin Coroutines para operaciones asíncronas y de red.
- **Red:** Retrofit con caché de 10MB e interceptor para soporte offline.
- **UI:** 
    - `DiffUtil` en todos los adaptadores para actualizaciones eficientes.
    - Fragmentos unificados para reducir redundancia.
    - Scroll infinito centralizado.
- **Navegación:** Jetpack Navigation Component con gestión centralizada en `NavegacionUtil`.
- **API:** Integración con Jikan API (MyAnimeList).

## 📁 Estructura Completa del Proyecto

A continuación se detalla la estructura exhaustiva de todos los archivos del proyecto:

```text
AlmondiNimes/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/almondinimes/
│   │   │   │   ├── activitys/
│   │   │   │   │   ├── MainActivity.kt
│   │   │   │   │   ├── MainActivity_Principal.kt
│   │   │   │   │   └── SplashActivity.kt
│   │   │   │   ├── adapters/
│   │   │   │   │   ├── AmigosAdapter.kt
│   │   │   │   │   ├── BusquedaAmigosAdapter.kt
│   │   │   │   │   ├── MyObrasAdapter.kt
│   │   │   │   │   ├── NotificacionesAdapter.kt
│   │   │   │   │   ├── NovedadesAdapter.kt
│   │   │   │   │   └── ObrasBusquedaAdapter.kt
│   │   │   │   ├── fragments/
│   │   │   │   │   ├── Fragment_AboutUs.kt
│   │   │   │   │   ├── Fragment_Advertencias.kt
│   │   │   │   │   ├── Fragment_Amigos.kt
│   │   │   │   │   ├── Fragment_Amigos_De_Amigo.kt
│   │   │   │   │   ├── Fragment_Anime.kt
│   │   │   │   │   ├── Fragment_Ayuda.kt
│   │   │   │   │   ├── Fragment_Comunidad.kt
│   │   │   │   │   ├── Fragment_Detalle_Obra.kt
│   │   │   │   │   ├── Fragment_Editar_Perfil.kt
│   │   │   │   │   ├── Fragment_FAQ.kt
│   │   │   │   │   ├── Fragment_Lista_Amigo.kt
│   │   │   │   │   ├── Fragment_Login.kt
│   │   │   │   │   ├── Fragment_Manga.kt
│   │   │   │   │   ├── Fragment_MisListas.kt
│   │   │   │   │   ├── Fragment_Novedades.kt
│   │   │   │   │   ├── Fragment_Perfil.kt
│   │   │   │   │   ├── Fragment_Perfil_Amigo.kt
│   │   │   │   │   ├── Fragment_Register.kt
│   │   │   │   │   └── NavegacionUtil.kt
│   │   │   │   ├── models/
│   │   │   │   │   ├── Aired.kt
│   │   │   │   │   ├── Anime.kt
│   │   │   │   │   ├── Genre.kt
│   │   │   │   │   ├── JikanFullResponse.kt
│   │   │   │   │   ├── JikanImages.kt
│   │   │   │   │   ├── JikanJpg.kt
│   │   │   │   │   ├── JikanResponse.kt
│   │   │   │   │   ├── NewsImages.kt
│   │   │   │   │   ├── NewsJpg.kt
│   │   │   │   │   ├── Noticia.kt
│   │   │   │   │   ├── Notificacion.kt
│   │   │   │   │   ├── ObraGuardada.kt
│   │   │   │   │   ├── TipoNotificacion.kt
│   │   │   │   │   └── Usuario.kt
│   │   │   │   ├── network/
│   │   │   │   │   ├── JikanApiService.kt
│   │   │   │   │   └── RetrofitClient.kt
│   │   │   │   ├── utils/
│   │   │   │   │   └── EndlessScrollListener.kt
│   │   │   │   ├── viewmodels/
│   │   │   │   │   ├── AmigosViewModel.kt
│   │   │   │   │   ├── NotificacionesViewModel.kt
│   │   │   │   │   └── ObrasViewModel.kt
│   │   │   │   └── AlmondiApplication.kt
│   │   │   ├── res/
│   │   │   │   ├── color/
│   │   │   │   │   └── bottom_nav_colors.xml
│   │   │   │   ├── drawable/
│   │   │   │   │   ├── bg_floating_dark.xml
│   │   │   │   │   ├── bg_main_gradient.xml
│   │   │   │   │   ├── bg_toolbar_floating.xml
│   │   │   │   │   ├── bg_unread_dot.xml
│   │   │   │   │   ├── gradient_black_to_transparent.xml
│   │   │   │   │   ├── ic_almondinimes_background.xml
│   │   │   │   │   ├── ic_launcher_background.xml
│   │   │   │   │   ├── ic_launcher_foreground.xml
│   │   │   │   │   ├── ic_menu_vector.xml
│   │   │   │   │   └── splash_icon_wrapper.xml
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   ├── activity_main_principal.xml
│   │   │   │   │   ├── activity_splash.xml
│   │   │   │   │   ├── dialog_add_friend.xml
│   │   │   │   │   ├── dialog_edit_item.xml
│   │   │   │   │   ├── fragment_about_us.xml
│   │   │   │   │   ├── fragment_advertencias.xml
│   │   │   │   │   ├── fragment_amigos.xml
│   │   │   │   │   ├── fragment_anime.xml
│   │   │   │   │   ├── fragment_ayuda.xml
│   │   │   │   │   ├── fragment_comunidad.xml
│   │   │   │   │   ├── fragment_detalle_obra.xml
│   │   │   │   │   ├── fragment_editar_perfil.xml
│   │   │   │   │   ├── fragment_friend_item_list.xml
│   │   │   │   │   ├── fragment_f_a_q.xml
│   │   │   │   │   ├── fragment_login.xml
│   │   │   │   │   ├── fragment_manga.xml
│   │   │   │   │   ├── fragment_novedades.xml
│   │   │   │   │   ├── fragment_perfil.xml
│   │   │   │   │   ├── fragment_perfil_amigo.xml
│   │   │   │   │   ├── fragment_register.xml
│   │   │   │   │   ├── item_advertencias.xml
│   │   │   │   │   ├── item_amigo.xml
│   │   │   │   │   ├── item_anime_manga.xml
│   │   │   │   │   ├── item_foro.xml
│   │   │   │   │   ├── item_friend_search_result.xml
│   │   │   │   │   ├── item_noticias.xml
│   │   │   │   │   ├── item_notificacion.xml
│   │   │   │   │   ├── item_obra.xml
│   │   │   │   │   ├── layout_empty_state.xml
│   │   │   │   │   ├── layout_lista_obras_usuario.xml
│   │   │   │   │   ├── nav_header_principal.xml
│   │   │   │   │   └── spinner_item_layout.xml
│   │   │   │   ├── menu/
│   │   │   │   │   ├── bottom_nav_menu.xml
│   │   │   │   │   └── drawer_menu.xml
│   │   │   │   ├── mipmap-*/ (Iconos de la aplicación)
│   │   │   │   ├── navigation/
│   │   │   │   │   ├── nav_graph_login.xml
│   │   │   │   │   └── nav_graph_principal.xml
│   │   │   │   ├── values/
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   ├── values-night/
│   │   │   │   │   └── themes.xml
│   │   │   │   └── xml/
│   │   │   │       ├── backup_rules.xml
│   │   │   │       └── data_extraction_rules.xml
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/java/com/example/almondinimes/ExampleInstrumentedTest.kt
│   │   └── test/java/com/example/almondinimes/ExampleUnitTest.kt
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── .gitignore
├── gradle/wrapper/
│   ├── gradle-wrapper.jar
│   └── gradle-wrapper.properties
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── local.properties
```

## 🛠️ Próximos Pasos

1. **Integración con Firebase:**
    - Implementar Firebase Authentication para el registro seguro de usuarios.
    - Migrar los ViewModels para persistir datos en Cloud Firestore.
2. **Optimización de Perfil:**
    - Permitir la edición de la imagen de perfil y sincronización en tiempo real.
3. **Mejoras en Comunidad:**
    - Implementar sistema de chat o comentarios en obras.

---
© 2024 AlmondiNimes Dev Team
