package com.example.eventia

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cart -> {
                val intent = Intent(this, CarrinhoActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_logout -> {
                SessionManager.logout(this)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    // ---------------------------------------

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