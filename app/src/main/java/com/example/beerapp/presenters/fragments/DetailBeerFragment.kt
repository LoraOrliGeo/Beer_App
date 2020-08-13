package com.example.beerapp.presenters.fragments

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.beerapp.R
import com.example.beerapp.databinding.DetailBeerScreenBinding
import com.example.domain.controllers.view_models.DetailBeerViewModel
import com.google.android.material.snackbar.Snackbar

class DetailBeerFragment : Fragment() {

    val args: DetailBeerFragmentArgs by navArgs()

    lateinit var detailBeerViewModel: DetailBeerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DetailBeerScreenBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        detailBeerViewModel = ViewModelProviders.of(this)
            .get(DetailBeerViewModel::class.java)

        binding.viewModel = detailBeerViewModel

        detailBeerViewModel.beerId = args.beerId

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fivoritize_menu, menu)
        val item = menu.getItem(0)
        detailBeerViewModel.checkForFavorite(args.beerId) { isFavorite ->
            if (isFavorite) {
                item.setIcon(R.drawable.favorite_fill)
            } else {
                item.setIcon(R.drawable.favorite_border)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.favoritize_beer) {
            detailBeerViewModel.checkForFavorite(args.beerId) { isFavorite ->
                if (isFavorite) {
                    detailBeerViewModel.deleteFromFavorites(args.beerId)
                    item.setIcon(R.drawable.favorite_border)
                    val snackbar =
                        Snackbar.make(view!!, R.string.fav_beer_removed, Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(
                        ContextCompat.getColor(
                            this.context!!,
                            R.color.secondaryLightColor
                        )
                    )
                    snackbar.setTextColor(
                        ContextCompat.getColor(
                            this.context!!,
                            R.color.colorPrimaryDark
                        )
                    )
                    snackbar.show()
                } else {
                    detailBeerViewModel.couldBeAdded {
                        if (detailBeerViewModel.couldBeAdded) {
                            detailBeerViewModel.addToFavorites(args.beerId)
                            item.setIcon(R.drawable.favorite_fill)
                            val snackbar =
                                Snackbar.make(
                                    view!!,
                                    R.string.fav_beer_added,
                                    Snackbar.LENGTH_SHORT
                                )
                            snackbar.setBackgroundTint(
                                ContextCompat.getColor(
                                    this.context!!,
                                    R.color.secondaryLightColor
                                )
                            )
                            snackbar.setTextColor(
                                ContextCompat.getColor(
                                    this.context!!,
                                    R.color.colorPrimaryDark
                                )
                            )
                            snackbar.show()
                        } else {
                            val snackbar = Snackbar.make(
                                view!!,
                                R.string.could_not_be_added_message,
                                Snackbar.LENGTH_SHORT
                            )
                            snackbar.setBackgroundTint(
                                ContextCompat.getColor(
                                    this.context!!,
                                    R.color.primaryLightColor
                                )
                            )
                            snackbar.show()
                        }
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}