package com.korotaev.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class SplashFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val prefs = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            delay(1500) // имитация загрузки

            val isRegistered = prefs.getBoolean("isRegistered", false)
            val isLoggedIn = prefs.getBoolean("isLoggedIn", false)
            val isAutoLogin = prefs.getBoolean("isAutoLogin", false)

            val navController = findNavController()

            when {
                !isRegistered -> {
                    // Нет регистрации → регистрация
                    navController.navigate(R.id.action_splash_to_register)
                }
                !isLoggedIn -> {
                    // Зарегистрирован, но не вошёл → логин
                    navController.navigate(R.id.action_splash_to_login)
                }
                isAutoLogin -> {
                    // Включён автологин → сразу авторизация в Firebase
                    auth.currentUser?.let { user ->
                        // Firebase уже хранит сессию, проверяем
                        navController.navigate(R.id.action_splash_to_one)
                    } ?: run {
                        // Пользователь не авторизован в Firebase → переходим на login
                        navController.navigate(R.id.action_splash_to_login)
                    }
                }
                else -> {
                    // В противном случае → просто логин
                    navController.navigate(R.id.action_splash_to_login)
                }
            }
        }
    }
}