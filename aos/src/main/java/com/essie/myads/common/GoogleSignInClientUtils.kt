package com.essie.myads.common

import android.app.Activity
import com.essie.myads.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.myads.adsense.data.datasource.remote.ServerConfig

object GoogleSignInClientUtils {
    const val REQUEST_CODE_PERMISSION = 101

    private const val ADSENSE_READ = "https://www.googleapis.com/auth/adsense.readonly"

    fun getGoogleSignInClient(activity: Activity): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestProfile()
            .requestServerAuthCode(ServerConfig.CLIENT_ID)
            .requestScopes(Scope(ADSENSE_READ))
            .build()

        return GoogleSignIn.getClient(activity, gso)
    }

    fun hasReadAdSensePermission(activity: Activity): Boolean {
        return GoogleSignIn.hasPermissions(
            GoogleSignIn.getLastSignedInAccount(activity),
            Scope(ADSENSE_READ)
        )
    }

    fun requestReadAdSensePermission(activity: Activity) {
        GoogleSignIn.requestPermissions(
            activity,
            REQUEST_CODE_PERMISSION,
            GoogleSignIn.getLastSignedInAccount(activity),
            Scope(ADSENSE_READ)
        )
    }
}