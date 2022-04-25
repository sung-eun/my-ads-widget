package com.essie.myads.common

import android.app.Activity
import android.content.Context
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.UserRecoverableAuthException
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.myads.adsense.data.IGoogleAccessTokenProvider
import com.myads.adsense.data.datasource.remote.ServerConfig

object GoogleSignInClientUtils : IGoogleAccessTokenProvider {
    const val REQUEST_CODE_PERMISSION = 101

    private const val ADSENSE_READ = "https://www.googleapis.com/auth/adsense.readonly"

    fun getGoogleSignInClient(activity: Activity): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestProfile()
            .requestEmail()
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

    override fun getAccessToken(context: Context): String {
        try {
            val googleAccount = GoogleSignIn.getLastSignedInAccount(context)?.account ?: return ""
            return GoogleAuthUtil.getToken(
                context,
                googleAccount,
                "oauth2:email openid profile $ADSENSE_READ"
            )
        } catch (e: UserRecoverableAuthException) {
            (context as Activity).startActivityForResult(e.intent, REQUEST_CODE_PERMISSION)
            return ""
        }
    }
}