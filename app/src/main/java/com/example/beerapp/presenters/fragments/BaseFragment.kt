package com.example.beerapp.presenters.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.beerapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewGroup = inflater.inflate(R.layout.fragment_container, container, false)
        val bottomNavigationView =
            viewGroup.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nestedNavHost) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)

        return viewGroup
    }
}