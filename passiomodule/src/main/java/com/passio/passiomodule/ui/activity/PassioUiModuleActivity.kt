package com.passio.passiomodule.ui.activity



import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.passio.passiomodule.NutritionUIModule
import com.passio.passiomodule.R
import com.passio.passiomodule.data.Repository
import com.passio.passiomodule.data.SharedPrefsPassioConnector
import com.passio.passiomodule.databinding.ActivityPassioUiModuleBinding

public class PassioUiModuleActivity : AppCompatActivity() {

    private var _binding: ActivityPassioUiModuleBinding? = null
    private val binding: ActivityPassioUiModuleBinding get() = _binding!!

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

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

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

        sharedViewModel.userProfileCacheEvent.observe(this){
            binding.viewLoading.isVisible = false
            binding.buttonAdd.setOnClickListener {
                navController.navigate(R.id.add_food)
            }
        }
    }

}