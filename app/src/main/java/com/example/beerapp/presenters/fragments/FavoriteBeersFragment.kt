package com.example.beerapp.presenters.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.example.beerapp.NavGraphDirections
import com.example.beerapp.R
import com.example.beerapp.databinding.FavoriteBeersScreenBinding
import com.example.beerapp.presenters.adapters.FavoriteBeerAdapter
import com.example.beerapp.presenters.adapters.FavoriteBeerListener
import com.example.domain.controllers.view_models.FavoriteBeerViewModel
import com.example.domain.models.asDomainDetailBeer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FavoriteBeersFragment : Fragment() {
    private lateinit var favoriteBeerViewModel: FavoriteBeerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)?.supportActionBar?.title = getString(R.string.favorites)

        val binding: FavoriteBeersScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.favorite_beers_screen, container, false)

        favoriteBeerViewModel =
            ViewModelProviders.of(this).get(FavoriteBeerViewModel::class.java)

        binding.favoriteBeerViewModel = favoriteBeerViewModel

        val favBeersAdapter = FavoriteBeerAdapter(FavoriteBeerListener { beerId ->
            favoriteBeerViewModel.onFavBeerClicked(beerId)
        })

        binding.viewPager.adapter = favBeersAdapter

        binding.viewPager.orientation = ORIENTATION_HORIZONTAL
        setupPageTransformer(binding)

        val tabLayout: TabLayout = binding.root.findViewById(R.id.tab_layout)

        TabLayoutMediator(tabLayout, binding.viewPager) { tab, position ->
            binding.viewPager.setCurrentItem(tab.position, true)
        }.attach()

        favoriteBeerViewModel.favoriteBeers.observe(viewLifecycleOwner, Observer {
            val viewPager: ViewPager2 = binding.root.findViewById(R.id.view_pager)
            val noFavoritesMsg: TextView = binding.root.findViewById(R.id.no_favorites_text)
            if (it.isEmpty()) {
                viewPager.visibility = View.GONE
                noFavoritesMsg.visibility = View.VISIBLE
                val dotIndicator = binding.root.findViewById<TabLayout>(R.id.tab_layout)
                dotIndicator.visibility = View.GONE
            } else {
                noFavoritesMsg.visibility = View.GONE
                viewPager.visibility = View.VISIBLE
                it.map { e -> e.asDomainDetailBeer() }
                favBeersAdapter.submitList(it)
            }
        })

        favoriteBeerViewModel.navigatedToDetail.observe(viewLifecycleOwner, Observer { beerId ->
            beerId?.let {
                val mainNavController =
                    Navigation.findNavController(this.activity!!, R.id.mainNavHost)
                mainNavController.navigate(
                    NavGraphDirections.actionFavoriteBeerFragmentToDetailBeer(
                        beerId
                    )
                )
                favoriteBeerViewModel.onFavBeerNavigated()
            }
        })

        return binding.root
    }

    private fun setupPageTransformer(binding: FavoriteBeersScreenBinding) {
        val viewPager2 = binding.viewPager

        with(viewPager2) {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
        }

        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
        val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)
        viewPager2.setPageTransformer { page, position ->
            val viewPager = page.parent.parent as ViewPager2
            val offset = position * -(2 * offsetPx + pageMarginPx)
            if (viewPager.orientation == ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -offset
                } else {
                    page.translationX = offset
                }
            } else {
                page.translationY = offset
            }
        }
    }
}