package com.example.rakhimovakp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rakhimovakp.auth.AuthManager
import com.example.rakhimovakp.auth.UserRole
import com.example.rakhimovakp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var authManager: AuthManager

    private val bottomNavDestinations = setOf(
        R.id.catalogFragment,
        R.id.cartFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.bottomNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.isVisible = destination.id in bottomNavDestinations
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val menu = bottomNavigation.menu
        authManager.currentUser.observe(this) { user ->
            Log.e("Uer", "User is $user")

            val addCarItem = menu.findItem(R.id.addCarFragment)
            addCarItem.isVisible = user?.role == UserRole.DEALER_MANAGER

            val analyticsItem = menu.findItem(R.id.analyticsFragment)
            analyticsItem.isVisible = user?.role == UserRole.ADMIN
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
