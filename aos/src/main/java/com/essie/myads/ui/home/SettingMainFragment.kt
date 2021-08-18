package com.essie.myads.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.essie.myads.BuildConfig
import com.essie.myads.R
import com.essie.myads.common.GoogleSignInClientUtils
import com.essie.myads.databinding.FragmentSettingMainBinding
import com.essie.myads.domain.usecase.AccountUseCase
import com.essie.myads.domain.usecase.DashboardDataUseCase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.myads.adsense.data.datasource.AdSenseRemoteDataSource
import com.myads.adsense.data.repository.AccountRepository
import com.myads.adsense.data.repository.AdsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

private const val TAG = "SettingMainFragment"
private const val REQUEST_GOOGLE_SIGN_IN = 111
private const val REQUEST_ADSENSE_PERMISSION = 222

class SettingMainFragment : Fragment() {
    private var binding: FragmentSettingMainBinding? = null

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
        binding = FragmentSettingMainBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val binding = binding ?: return
        binding.accountButtonContainer.setOnClickListener { googleSignIn() }
        binding.disconnectButton.setOnClickListener { googleSignOut() }

        val context = context ?: return
        val account = GoogleSignIn.getLastSignedInAccount(context)
        fillProfileView(account)
    }

    private fun googleSignIn() {
        getGoogleSignInClient()?.let {
            startActivityForResult(it.signInIntent, REQUEST_GOOGLE_SIGN_IN)
        }
    }

    private fun googleSignOut() {
        getGoogleSignInClient()?.revokeAccess()?.addOnCompleteListener {
            val context = context ?: return@addOnCompleteListener
            val account = GoogleSignIn.getLastSignedInAccount(context)
            fillProfileView(account)
        }
    }

    private fun getGoogleSignInClient(): GoogleSignInClient? {
        val activity = activity ?: return null
        return GoogleSignInClientUtils.getGoogleSignInClient(activity)
    }

    private fun handleGoogleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
//            if (!GoogleSignIn.hasPermissions(
//                    account,
//                    Scope("https://www.googleapis.com/auth/adsense.readonly")
//                )
//            ) {
//                GoogleSignIn.requestPermissions(
//                    this,
//                    REQUEST_ADSENSE_PERMISSION,
//                    account,
//                    Scope("https://www.googleapis.com/auth/adsense.readonly")
//                )
//            } else {
//
//            }
            fillProfileView(account)
        } catch (e: ApiException) {
            Log.w(TAG, e)
        }
    }

    private fun fillProfileView(account: GoogleSignInAccount?) {
        val binding = binding ?: return
        if (account == null) {
            binding.accountButtonContainer.isClickable = true
            binding.accountName.text = getString(R.string.connect_account)
            binding.profileImage.setImageResource(R.drawable.ic_ghost)
            binding.disconnectButton.visibility = View.GONE
        } else {
            binding.accountButtonContainer.isClickable = false
            binding.accountName.text = account.displayName ?: getString(R.string.unknown)
            Glide.with(this)
                .load(account.photoUrl)
                .circleCrop()
                .error(R.drawable.ic_google_simple)
                .into(binding.profileImage)
            binding.disconnectButton.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}