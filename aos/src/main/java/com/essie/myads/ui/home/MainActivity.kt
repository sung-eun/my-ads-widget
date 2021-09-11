package com.essie.myads.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.essie.myads.common.GoogleSignInClientUtils
import com.essie.myads.databinding.ActivityMainBinding
import com.essie.myads.domain.entity.DashboardData
import com.essie.myads.domain.entity.DateRange
import com.essie.myads.ui.component.CustomTabRow
import com.essie.myads.ui.home.overview.OverviewBody
import com.essie.myads.ui.theme.AppTheme


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.composeView.setContent {
            AppMain()
        }

        silentSignIn()
    }

    private fun silentSignIn() {
        GoogleSignInClientUtils.getGoogleSignInClient(this).silentSignIn()
    }

    @Composable
    fun AppMain() {
        AppTheme {
            val allScreens = HomeScreen.values().toList()
            val navController = rememberNavController()
            val backstackEntry = navController.currentBackStackEntryAsState()
            val currentScreen = HomeScreen.fromRoute(backstackEntry.value?.destination?.route)

            Scaffold(
                topBar = {
                    CustomTabRow(
                        allScreens = allScreens,
                        onTabSelected = { navController.navigate(it.name) },
                        currentScreen = currentScreen
                    )
                }
            ) { innerPadding ->
                HomeNavHost(navController, modifier = Modifier.padding(innerPadding))
            }
        }
    }

    @Composable
    fun HomeNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
        NavHost(
            navController = navController,
            startDestination = HomeScreen.OVERVIEW.name,
            modifier = modifier
        ) {
            composable(HomeScreen.OVERVIEW.name) {
                OverviewBody(DashboardData(0L, 0L, "$0.00", DateRange.LAST_7DAYS, "$0.00"))
            }
            composable(HomeScreen.SETTINGS.name) {

            }
        }
    }
}

