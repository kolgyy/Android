package com.korotaev.myapplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class ContentActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        bottomNav = findViewById(R.id.bottom_nav)

        // Настраиваем navController с bottomNav
        bottomNav.setupWithNavController(navController)

        // Скрытие/показ нижнего меню в зависимости от фрагмента
        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNav.visibility = when (destination.id) {
                R.id.oneFragment, R.id.twoFragment -> View.VISIBLE
                else -> View.GONE
            }
        }
    }
}