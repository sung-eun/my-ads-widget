package com.essie.myads.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.essie.myads.BuildConfig
import com.essie.myads.common.GoogleSignInClientUtils
import com.essie.myads.common.ui.component.CustomTabRow
import com.essie.myads.common.ui.theme.AppTheme
import com.essie.myads.databinding.ActivityMainBinding
import com.essie.myads.domain.usecase.AccountUseCase
import com.essie.myads.domain.usecase.DashboardDataUseCase
import com.essie.myads.ui.home.overview.OverviewBody
import com.essie.myads.ui.home.overview.OverviewViewModel
import com.essie.myads.ui.home.overview.OverviewViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.myads.adsense.data.LocalDataSourceDelegate
import com.myads.adsense.data.datasource.remote.AdSenseRemoteDataSource
import com.myads.adsense.data.datasource.remote.GoogleAuthRemoteDataSource
import com.myads.adsense.data.repository.AccountRepository
import com.myads.adsense.data.repository.AdsRepository
import com.myads.adsense.data.repository.AuthRepository
import kotlinx.coroutines.FlowPreview

private const val REQUEST_GOOGLE_SIGN_IN = 111

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //TODO DI
    private val authLocalDataSource by lazy { LocalDataSourceDelegate.getAuthLocalDataSource(this) }
    private val authUseCase by lazy {
        AccountUseCase(
            AuthRepository(
                authLocalDataSource,
                GoogleAuthRemoteDataSource(BuildConfig.DEBUG)
            ),
            AccountRepository(
                AdSenseRemoteDataSource(
                    authLocalDataSource,
                    BuildConfig.DEBUG
                ),
                LocalDataSourceDelegate.getAdSenseLocalDataSource(this)
            )
        )
    }

    private val overviewViewModel by lazy {
        ViewModelProvider(
            this,
            OverviewViewModelFactory(
                AccountUseCase(
                    AuthRepository(
                        authLocalDataSource,
                        GoogleAuthRemoteDataSource(BuildConfig.DEBUG)
                    ),
                    AccountRepository(
                        AdSenseRemoteDataSource(
                            authLocalDataSource,
                            BuildConfig.DEBUG
                        ),
                        LocalDataSourceDelegate.getAdSenseLocalDataSource(this)
                    )
                ),
                DashboardDataUseCase(
                    AdsRepository(
                        AdSenseRemoteDataSource(
                            authLocalDataSource,
                            BuildConfig.DEBUG
                        )
                    )
                )
            )
        ).get(OverviewViewModel::class.java)
    }

    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        silentSignIn()

        binding.composeView.setContent {
            AppMain()
        }

        overviewViewModel.fetchInitData(GoogleSignIn.getLastSignedInAccount(this)?.serverAuthCode)
    }

    private fun silentSignIn() {
        GoogleSignInClientUtils.getGoogleSignInClient(this).silentSignIn()
    }

    @FlowPreview
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

    @FlowPreview
    @Composable
    fun HomeNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
        NavHost(
            navController = navController,
            startDestination = HomeScreen.OVERVIEW.name,
            modifier = modifier
        ) {
            composable(HomeScreen.OVERVIEW.name) {
                OverviewBody(overviewViewModel)
            }
            composable(HomeScreen.SETTINGS.name) {

            }
        }
    }

    private fun googleSignIn() {
        startActivityForResult(
            GoogleSignInClientUtils.getGoogleSignInClient(this).signInIntent,
            REQUEST_GOOGLE_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GOOGLE_SIGN_IN) {
                handleGoogleSignInResult(data)
            }
        }
    }

    private fun handleGoogleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
        } catch (e: ApiException) {
        }
    }
}

