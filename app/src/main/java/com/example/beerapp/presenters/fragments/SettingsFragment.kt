package com.example.beerapp.presenters.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import coil.transform.CircleCropTransformation
import com.example.beerapp.R
import com.example.beerapp.databinding.SettingsScreenBinding
import com.example.beerapp.ui.LoginActivity
import com.example.domain.controllers.view_models.SettingsViewModel
import java.util.*

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    private var notifications: Switch? = null
    private var randomBeerSelection: Switch? = null
    private var darkTheme: Switch? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)?.supportActionBar?.title = getString(R.string.profile)

        val binding: SettingsScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.settings_screen, container, false)

        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

        binding.settingsViewModel = settingsViewModel

        val user = settingsViewModel.getUser()
        binding.userName.text = user?.displayName
        binding.userEmail.text = user?.email
        setUserImage(binding.userImage, user?.photoUrl.toString())

        binding.logoutButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (v?.id == R.id.logout_button) {
                    settingsViewModel.signOut(context) {
                        val intent = Intent(context, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        })

        notifications = binding.notificationSwitch
        randomBeerSelection = binding.randomBeerSwitch
        darkTheme = binding.themeSwitch

        setLocale(Locale.getDefault().language)

        darkTheme?.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        })

        return binding.root
    }

    private fun setUserImage(view: ImageView, imageUrl: String?) {
        if (imageUrl.isNullOrEmpty() || imageUrl.equals("null")) {
            view.load(R.drawable.account_circle)
        } else {
            view.load(imageUrl) {
                this.transformations(CircleCropTransformation())
            }
        }
    }

    override fun onPause() {
        settingsViewModel.saveSettings(
            notifications?.isChecked,
            randomBeerSelection?.isChecked,
            darkTheme?.isChecked
        )
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        notifications?.isChecked = settingsViewModel.getNotifications()
        randomBeerSelection?.isChecked = settingsViewModel.getRandomBeerFavortes()
        darkTheme?.isChecked = settingsViewModel.getDarkTheme()
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale)
            context?.createConfigurationContext(config)
        }
    }
}
