package com.korotaev.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private lateinit var editInput: EditText
    private lateinit var editPassword: EditText
    private lateinit var buttonSignIn: Button
    private lateinit var checkBox: CheckBox
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // findViewById ТОЛЬКО после setContentView
        editInput = findViewById(R.id.edit_input)
        editPassword = findViewById(R.id.edit_password)
        buttonSignIn = findViewById(R.id.button_signIn)
        checkBox = findViewById(R.id.check_isAutomatically)
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)

        buttonSignIn.setOnClickListener() {
            validateInputs()
        }

    }

    private fun saveAutoLogin(isChecked: Boolean) {
        sharedPreferences.edit().putBoolean("isChecked", isChecked).apply()
    }

    private fun validateInputs() {
        val login = editInput.text.toString()
        val password = editPassword.text.toString()

        if (login.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        if (login == sharedPreferences.getString("login", null) && password == sharedPreferences.getString("password", null)) {
            saveAutoLogin(checkBox.isChecked)
            startActivity(Intent(this, ContentActivity::class.java))
            finish()

        } else {
            Toast.makeText(this, "Данные неверны", Toast.LENGTH_SHORT).show()
            return
        }
    }
}