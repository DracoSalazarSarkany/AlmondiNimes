package com.example.almondinimes.activitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import android.widget.TextView
import com.example.almondinimes.R
import com.example.almondinimes.data.AuthManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity_Principal : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private val authManager = AuthManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_principal)

        // 1. Inicializar componentes con tus IDs exactos
        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar_principal)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // Actualizar el nombre en el menú lateral
        actualizarNombreMenu(navView)

        // 2. Configurar NavController usando nav_host_fragment_principal
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_principal) as NavHostFragment
        navController = navHostFragment.navController

        // 3. Configurar la Toolbar para que funcione con Navigation
        // Definimos qué destinos son "principales" (muestran hamburguesa en vez de flecha atrás)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_novedades, R.id.nav_anime, R.id.nav_manga, R.id.nav_comunidad,
                R.id.nav_perfil, R.id.nav_tus_animes, R.id.nav_amigos
            ),
            drawerLayout
        )

        // Vincula la toolbar con el controlador y la configuración de arriba
        setSupportActionBar(toolbar)
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)

        // 4. Vincular menús (BottomNav y Drawer)
        bottomNav.setupWithNavController(navController)
        navView.setupWithNavController(navController)

        // Configurar el logo central flotante
        val ivLogoCentral: android.widget.ImageView = findViewById(R.id.iv_logo_central)
        ivLogoCentral.setOnClickListener {
            navController.navigate(R.id.nav_novedades)
        }

        // 5. Gestión manual para acciones especiales (Logout)
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_logout -> {
                    cerrarSesion()
                    true
                }
                else -> {
                    // Para los demás, navegación automática y cerrar el drawer
                    val handled = NavigationUI.onNavDestinationSelected(item, navController)
                    if (handled) drawerLayout.closeDrawer(GravityCompat.START)
                    handled
                }
            }
        }

        // Si el usuario pulsa atrás y el menú lateral está abierto, lo cerramos primero
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    // Si el menú está abierto, lo cerramos
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    // Si está cerrado, dejamos que el sistema haga el "atrás" normal
                    isEnabled = false // Desactivamos este callback temporalmente
                    onBackPressedDispatcher.onBackPressed() // Llama al atrás del sistema
                    isEnabled = true // Lo reactivamos
                }
            }
        })
    }

    private fun actualizarNombreMenu(navView: NavigationView) {
        val uid = authManager.getCurrentUserUid()
        if (uid != null) {
            // Escuchar cambios en tiempo real
            authManager.listenToUserData(uid) { usuario ->
                if (usuario != null) {
                    val headerView = navView.getHeaderView(0)
                    val tvUsername = headerView.findViewById<TextView>(R.id.tv_username)
                    tvUsername.text = usuario.nick
                }
            }
        }
    }

    private fun cerrarSesion() {
        // Logout real de Firebase
        com.google.firebase.auth.FirebaseAuth.getInstance().signOut()

        // Al cerrar sesión, desactivamos el "Recordar contraseña" para que no entre solo la próxima vez
        val sharedPref = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("remember", false)
        editor.apply()

        val intent = Intent(this, MainActivity::class.java)
        // Limpiamos el stack para que no pueda volver atrás al área privada
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}