package com.essie.myads.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.essie.myads.databinding.FragmentAccountSettingBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope


private const val TAG = "AccountSettingFragment"
private const val REQUEST_GOOGLE_SIGN_IN = 111
private const val REQUEST_ADSENSE_PERMISSION = 222

class AccountSettingFragment : Fragment() {

    private var binding: FragmentAccountSettingBinding? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GOOGLE_SIGN_IN) {
                handleGoogleSignInResult(data)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountSettingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        val binding = binding ?: return
        binding.addAccountButton.setOnClickListener { googleSignIn() }
    }

    private fun googleSignIn() {
        val activity = activity ?: return

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestScopes(Scope("https://www.googleapis.com/auth/adsense.readonly"))
            .build()

        val client = GoogleSignIn.getClient(activity, gso)
        startActivityForResult(client.signInIntent, REQUEST_GOOGLE_SIGN_IN)
    }

    private fun handleGoogleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (!GoogleSignIn.hasPermissions(
                    account,
                    Scope("https://www.googleapis.com/auth/adsense.readonly")
                )
            ) {
                GoogleSignIn.requestPermissions(
                    this,
                    REQUEST_ADSENSE_PERMISSION,
                    account,
                    Scope("https://www.googleapis.com/auth/adsense.readonly")
                )
            } else {

            }
        } catch (e: ApiException) {
            Log.w(TAG, e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}