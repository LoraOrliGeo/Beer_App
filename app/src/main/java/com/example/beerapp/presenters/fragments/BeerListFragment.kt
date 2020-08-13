package com.example.beerapp.presenters.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import com.example.beerapp.NavGraphDirections
import com.example.beerapp.R
import com.example.beerapp.databinding.BeerListBinding
import com.example.beerapp.utils.BeerListAdapter
import com.example.beerapp.utils.DetailBeerListener
import com.example.domain.controllers.view_models.BeerListViewModel
import com.example.domain.controllers.view_model_factories.BeerListViewModelFactory
import com.example.domain.models.asDomainBaseModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.beer_list.view.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

private const val DEBOUNCE_TIMEOUT = 1000L

class BeerListFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private lateinit var beerViewModel: BeerListViewModel

    private var passedBeerName: EditText? = null

    val adapter = BeerListAdapter(DetailBeerListener { beerId ->
        beerViewModel.onBeerClicked(beerId)
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)?.supportActionBar?.title =
            getString(R.string.beer_list_text)

        val binding: BeerListBinding =
            DataBindingUtil.inflate(inflater, R.layout.beer_list, container, false)

        val application = requireNotNull(this.activity).application
        val beerViewModelFactory =
            BeerListViewModelFactory(application)
        beerViewModel =
            ViewModelProviders.of(this, beerViewModelFactory).get(BeerListViewModel::class.java)

        binding.beerListViewModel = beerViewModel

        binding.beersListRecyclerView.adapter = adapter

        observePagedListBeers(binding)

        beerViewModel.navigateToBeerClicked.observe(viewLifecycleOwner, Observer { beerId ->
            beerId?.let {
                // main nav controller with global action
                val mainNavController =
                    Navigation.findNavController(this.activity!!, R.id.mainNavHost)
                mainNavController
                    .navigate(NavGraphDirections.actionBeerListFragmentToDetailBeer(beerId))
                beerViewModel.onDetailBeerNavigated()
            }
        })

        passedBeerName = binding.constraintLayout.edit_text
        searchByInput(passedBeerName)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        passedBeerName?.setText(beerViewModel.filterName)
        beerViewModel.filterBeersByFilter(beerViewModel.filterName)
    }

    private fun searchByInput(passedBeerName: EditText?) {
        passedBeerName?.addTextChangedListener(object : TextWatcher {
            private var searchFor = ""
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString()
                if (input == searchFor) {
                    return
                }
                searchFor = input
                launch {
                    delay(DEBOUNCE_TIMEOUT)
                    if (searchFor != input) {
                        return@launch
                    }
                    beerViewModel.filterBeersByFilter(searchFor)
                }
            }
        })
    }

    private fun observePagedListBeers(binding: BeerListBinding) {
        beerViewModel.pagedBeersLiveData.observe(viewLifecycleOwner, Observer {
            val noBeersMsg: TextView = binding.root.findViewById(R.id.no_beers_text)
            val beerList: RecyclerView = binding.root.findViewById(R.id.beers_list_recycler_view)
            if (it.isEmpty()) {
                noBeersMsg.visibility = View.VISIBLE
                beerList.visibility = View.GONE
            } else {
                noBeersMsg.visibility = View.GONE
                beerList.visibility = View.VISIBLE
                it.map { e -> e?.asDomainBaseModel() }
                adapter.submitList(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.filters_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.filter_screen) {
            val mainNavController = Navigation.findNavController(this.activity!!, R.id.mainNavHost)
            NavigationUI.onNavDestinationSelected(item, mainNavController)
        } else if (item.itemId == R.id.dice) {
            val mainNavController = Navigation.findNavController(this.activity!!, R.id.mainNavHost)
            launch {
                val randomBeer = beerViewModel.getRandomBeer()
                if (randomBeer != null) {
                    mainNavController.navigate(
                        NavGraphDirections.actionBeerListFragmentToDetailBeer(
                            randomBeer.id
                        )
                    )
                } else {
                    val snackbar = Snackbar.make(
                        view!!,
                        "There are no favorite beers added!",
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.setBackgroundTint(Color.parseColor("#002554"))
                    snackbar.show()
                }
            }
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}