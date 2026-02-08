package com.korotaev.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class RegistrationActivity : AppCompatActivity() {

    private lateinit var buttonByNumber: Button
    private lateinit var buttonByEmail: Button
    private lateinit var editInput: EditText
    private lateinit var editPassword: EditText
    private lateinit var editRepeatPassword: EditText
    private lateinit var buttonRegister: Button
    private lateinit var sharedPreferences: SharedPreferences

    private var isByNumber = false // Текущий режим


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // findViewById ТОЛЬКО после setContentView
        buttonByNumber = findViewById(R.id.button_byNumber)
        buttonByEmail = findViewById(R.id.button_byEmail)
        editInput = findViewById(R.id.edit_input)
        editPassword = findViewById(R.id.edit_password)
        editRepeatPassword = findViewById(R.id.edit_repeatPassword)
        buttonRegister = findViewById(R.id.button_Register)
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)

        selectEmailMode()

        buttonByNumber.setOnClickListener {
            selectNumberMode()
        }
        buttonByEmail.setOnClickListener {
            selectEmailMode()
        }
        buttonRegister.setOnClickListener {
            validateInputs()
        }
    }

    private fun selectNumberMode() {
        isByNumber = true
        editInput.text.clear()
        buttonByNumber.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        buttonByNumber.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_light))
        buttonByEmail.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        buttonByEmail.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        editInput.hint = "Введите номер"
        editInput.inputType = InputType.TYPE_CLASS_PHONE
    }

    private fun selectEmailMode() {
        isByNumber = false
        editInput.text.clear()
        buttonByEmail.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        buttonByEmail.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_light))
        buttonByNumber.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        buttonByNumber.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        editInput.hint = "Введите email"
        editInput.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }

    private fun save(login: String, password: String) {
        sharedPreferences.edit().putString("login", login).apply()
        sharedPreferences.edit().putString("password", password).apply()
    }

    private fun validateInputs() {
        val inputText = editInput.text.toString()
        val password = editPassword.text.toString()
        val repeatPassword = editRepeatPassword.text.toString()

        if (isByNumber) {
            if (!inputText.startsWith("+")) {
                Toast.makeText(this, "Телефон должен начинаться с +", Toast.LENGTH_SHORT).show()
                return
            }
        } else {
            if (!inputText.contains("@")) {
                Toast.makeText(this, "Некорректный email", Toast.LENGTH_SHORT).show()
                return
            }
        }
        if (password.length < 8) {
            Toast.makeText(this, "Пароль должен быть минимум 8 символов", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != repeatPassword) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            return
        }

        save(inputText, password)

        startActivity(Intent(this, ContentActivity::class.java))

        finish()
    }
}