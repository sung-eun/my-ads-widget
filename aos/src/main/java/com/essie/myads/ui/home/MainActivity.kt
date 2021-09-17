package com.essie.myads.ui.home

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.essie.myads.BuildConfig
import com.essie.myads.R
import com.essie.myads.common.GoogleSignInClientUtils
import com.essie.myads.common.ui.component.CustomTabRow
import com.essie.myads.common.ui.theme.AppTheme
import com.essie.myads.databinding.ActivityMainBinding
import com.essie.myads.domain.usecase.AccountUseCase
import com.essie.myads.domain.usecase.DashboardDataUseCase
import com.essie.myads.ui.home.overview.OverviewBody
import com.essie.myads.ui.home.settings.SettingsBody
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.myads.adsense.data.LocalDataSourceDelegate
import com.myads.adsense.data.datasource.remote.AdSenseRemoteDataSource
import com.myads.adsense.data.datasource.remote.GoogleAuthRemoteDataSource
import com.myads.adsense.data.repository.AccountRepository
import com.myads.adsense.data.repository.AdsRepository
import com.myads.adsense.data.repository.AuthRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

private const val REQUEST_GOOGLE_SIGN_IN = 111

@FlowPreview
class MainActivity : AppCompatActivity(), IGoogleAccountManager {

    private lateinit var binding: ActivityMainBinding

    private var firstBackPressedTimestamp: Long = -1

    //TODO DI
    private val authLocalDataSource by lazy { LocalDataSourceDelegate.getAuthLocalDataSource(this) }
    private val accountUseCase by lazy {
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

    private val mainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(
                accountUseCase,
                DashboardDataUseCase(
                    AdsRepository(
                        AdSenseRemoteDataSource(
                            authLocalDataSource,
                            BuildConfig.DEBUG
                        )
                    )
                ),
                this
            )
        ).get(MainViewModel::class.java)
    }

    private val adSize: AdSize
        get() {
            val displayMetrics = Resources.getSystem().displayMetrics
            val density = displayMetrics.density
            val displayWidthPixels = displayMetrics.widthPixels.toFloat()
            val adWidth = (displayWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.composeView.setContent {
            AppMain()
        }
        initAds()
    }

    private fun initAds() {
        MobileAds.initialize(this)
        val adView = AdView(this)
        (adView.layoutParams as? FrameLayout.LayoutParams)?.gravity = Gravity.CENTER
        binding.adContainer.addView(adView)

        adView.adSize = adSize
        adView.adUnitId = getString(R.string.admob_banner_unit)
        adView.loadAd(AdRequest.Builder().build())
    }

    override fun onStart() {
        super.onStart()
        silentSignIn()
    }

    private fun silentSignIn() {
        GoogleSignInClientUtils.getGoogleSignInClient(this).silentSignIn().addOnCompleteListener {
            mainViewModel.refreshToken()

            val googleAccount = GoogleSignIn.getLastSignedInAccount(this)
            lifecycleScope.launch {
                mainViewModel.updateGoogleAccount(googleAccount)
                mainViewModel.fetchInitData(googleAccount?.serverAuthCode)
            }
        }
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

    @ExperimentalCoilApi
    @Composable
    fun HomeNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
        NavHost(
            navController = navController,
            startDestination = HomeScreen.OVERVIEW.name,
            modifier = modifier
        ) {
            composable(HomeScreen.OVERVIEW.name) {
                OverviewBody(mainViewModel) {
                    GoogleSignInClientUtils.requestReadAdSensePermission(
                        this@MainActivity
                    )
                }
            }
            composable(HomeScreen.SETTINGS.name) {
                SettingsBody(
                    mainViewModel,
                    { googleSignIn() },
                    {
                        GoogleSignInClientUtils.getGoogleSignInClient(this@MainActivity)
                            .revokeAccess()
                        mainViewModel.updateGoogleAccount(null)
                        mainViewModel.refresh()
                    })
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
            } else if (requestCode == GoogleSignInClientUtils.REQUEST_CODE_PERMISSION) {
                lifecycleScope.launch {
                    mainViewModel.fetchInitData(GoogleSignIn.getLastSignedInAccount(this@MainActivity)?.serverAuthCode)
                }
            }
        }
    }

    private fun handleGoogleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            mainViewModel.updateGoogleAccount(account)
            lifecycleScope.launch {
                mainViewModel.fetchInitData(account.serverAuthCode)
            }
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }

    override fun hasAdSensePermission(): Boolean =
        GoogleSignInClientUtils.hasReadAdSensePermission(this)

    override fun onBackPressed() {
        val currentTimestamp = System.currentTimeMillis()
        if (firstBackPressedTimestamp < 0 || currentTimestamp - firstBackPressedTimestamp > 2000) {
            firstBackPressedTimestamp = currentTimestamp
            Toast.makeText(this, R.string.message_back_press_finish_app, Toast.LENGTH_SHORT).show()
        } else {
            finish()
        }
    }

}

