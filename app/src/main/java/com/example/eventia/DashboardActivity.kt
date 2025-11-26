package com.example.eventia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.eventia.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavView.setupWithNavController(navController)

        NavigationUI.setupActionBarWithNavController(this, navController)

        val userRole = SessionManager.getRole(this)
        val cleanedRole = userRole?.trim()?.replace("'", "")
        if (cleanedRole != "admin") {
            binding.bottomNavView.menu.findItem(R.id.nav_admin_hub).isVisible = false
        }

        handleIntentNavigation()
    }

    private fun handleIntentNavigation() {
        val navigateTo = intent.getStringExtra("NAVIGATE_TO")
        if (navigateTo == "PROFILE") {
            binding.bottomNavView.selectedItemId = R.id.nav_profile
        }
    }

    fun navigateToTab(tabIndex: Int) {
        binding.bottomNavView.selectedItemId = binding.bottomNavView.menu.getItem(tabIndex).itemId
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}