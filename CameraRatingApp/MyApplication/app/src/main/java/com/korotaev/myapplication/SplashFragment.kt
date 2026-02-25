package com.korotaev.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Эмуляция загрузки (аналог Handler в старой Activity)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            delay(1500) // 1.5 сек

            checkUserState()
        }
    }

    private fun checkUserState() {
        val sharedPreferences = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)

        val login = sharedPreferences.getString("login", null)
        val password = sharedPreferences.getString("password", null)
        val isAutoLogin = sharedPreferences.getBoolean("isChecked", false)

        val navController = findNavController()

        when {
            // Нет данных -> регистрация
            login == null || password == null -> {
                navController.navigate(R.id.action_splash_to_register)
            }

            // Есть данные, но автологин выключен -> логин
            !isAutoLogin -> {
                navController.navigate(R.id.action_splash_to_login)
            }

            // Есть данные и включён автологин -> контент
            else -> {
                navController.navigate(R.id.action_splash_to_one)
            }
        }
    }
}