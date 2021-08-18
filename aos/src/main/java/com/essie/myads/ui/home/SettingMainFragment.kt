package com.essie.myads.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.essie.myads.R
import com.essie.myads.databinding.FragmentSettingMainBinding

class SettingMainFragment : Fragment() {
    private var binding: FragmentSettingMainBinding? = null

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
        binding.accountsButton.setOnClickListener {
            it.findNavController().navigate(R.id.nav_accounts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}