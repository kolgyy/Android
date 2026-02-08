package com.korotaev.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)

        // Имитация загрузки
        Handler(Looper.getMainLooper()).postDelayed({
            checkUserState()
        }, 1500)
    }

    private fun checkUserState() {
        val login = sharedPreferences.getString("login", null)
        val password = sharedPreferences.getString("password", null)
        val isAutoLogin = sharedPreferences.getBoolean("isChecked", false)

        when {
            // Нет данных -> регистрация
            login == null || password == null -> {
                startActivity(Intent(this, RegistrationActivity::class.java))
            }

            // Есть данные, но автологин выключен -> логин
            !isAutoLogin -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }

            // Есть данные и включён автологин -> контент
            else -> {
                startActivity(Intent(this, ContentActivity::class.java))
            }
        }

        finish()
    }
}