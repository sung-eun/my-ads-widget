package com.essie.myads.common

import android.app.Activity
import com.essie.myads.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope

object GoogleSignInClientUtils {
    fun getGoogleSignInClient(activity: Activity): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestProfile()
            .requestServerAuthCode(activity.getString(R.string.google_server_client_id))
            .requestScopes(Scope("https://www.googleapis.com/auth/adsense.readonly"))
            .build()

        return GoogleSignIn.getClient(activity, gso)
    }
}