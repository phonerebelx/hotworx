package com.example.passiomodulenew.ui.activity


import com.example.passiomodulenew.ui.menu.MainMenuDialog
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.passiomodulenew.data.Repository
import com.example.passiomodulenew.data.SharedPrefsPassioConnector
import com.example.passiomodulenew.NutritionUIModule
import com.passio.passiomodulenew.R
import com.passio.passiomodulenew.databinding.ActivityPassioUiModuleBinding

internal class PassioUiModuleActivity : AppCompatActivity() {

    private var _binding: ActivityPassioUiModuleBinding? = null
    private val binding: ActivityPassioUiModuleBinding get() = _binding!!
    private var hidePersonalInfo: Boolean = false

    private val sharedViewModel: SharedViewModel by viewModels()
    private val navigationIds = listOf(
        R.id.dashboard,
        R.id.diary,
        R.id.mealplan,
        R.id.progress
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val connector =
            NutritionUIModule.getConnector() ?: SharedPrefsPassioConnector(applicationContext)
        Repository.create(applicationContext, connector)

        _binding = ActivityPassioUiModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the value of the "hide_personal_info" extra
        hidePersonalInfo = intent.getBooleanExtra("hide_personal_info", false)

        sharedViewModel.userProfileCacheEvent.observe(this){
            setupNav()
        }
    }

    private fun setupNav()
    {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Determine the start destination based on the intent extra
        val startDestination = when (intent.getStringExtra("start_destination")) {
            "profile" -> R.id.my_profile
            else -> R.id.dashboard
        }

        // Inflate the navGraph and set the start destination
        val navGraph = navController.navInflater.inflate(R.navigation.main_nav_graph)
        navGraph.setStartDestination(startDestination)

        // Pass the "hidePersonalInfo" flag as an argument if starting with "my_profile"
        if (startDestination == R.id.my_profile) {
            val bundle = Bundle().apply {
                putBoolean("hide_personal_info", intent.getBooleanExtra("hide_personal_info", false))
            }
            navController.setGraph(navGraph, bundle)
        } else {
            navController.graph = navGraph
        }

        setupWithNavController(binding.bottomNavigation, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in navigationIds) {
                binding.bottomNavigation.visibility = View.VISIBLE
                binding.buttonAdd.visibility = View.VISIBLE
            } else {
                binding.bottomNavigation.visibility = View.GONE
                binding.buttonAdd.visibility = View.GONE
            }
        }

        binding.viewLoading.isVisible = false
        binding.buttonAdd.setOnClickListener {
//            navController.navigate(R.id.add_food)
            MainMenuDialog(this@PassioUiModuleActivity).show()
        }
    }

}