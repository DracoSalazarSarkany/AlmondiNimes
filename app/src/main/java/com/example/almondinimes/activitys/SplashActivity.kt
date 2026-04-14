package com.example.almondinimes.activitys

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.almondinimes.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Ocultar la barra de estado para una experiencia full screen
        window.decorView.systemUiVisibility = android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        // Esperar 2 segundos y pasar a la MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cerramos SplashActivity para que no puedan volver atrás
        }, 2000)
    }
}
