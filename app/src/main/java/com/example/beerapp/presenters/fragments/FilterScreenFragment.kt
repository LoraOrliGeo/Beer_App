package com.example.beerapp.presenters.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.beerapp.R
import com.example.beerapp.databinding.FilterScreenBinding
import com.example.data.Filter
import com.example.domain.controllers.view_models.FiltersViewModel
import com.example.domain.controllers.view_model_factories.FiltersViewModelFactory
import kotlinx.android.synthetic.main.filter_screen.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class FilterScreenFragment : Fragment(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private lateinit var filtersViewModel: FiltersViewModel
    private lateinit var binding: FilterScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.filter_screen, container, false)

        val application = requireNotNull(this.activity).application
        val filtersViewModelFactory =
            FiltersViewModelFactory(application)
        filtersViewModel =
            ViewModelProviders.of(this, filtersViewModelFactory).get(FiltersViewModel::class.java)
        binding.filtersViewModel = filtersViewModel

        setClickListenersToCleanFilter(binding)
        showDatePickerOnClick(binding)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // take the Filter's values from SP and set the editTexts values
        val enteredFilters = filtersViewModel.getEnteredFilters()
        binding.yeastFilter.setText(enteredFilters.yeast)
        binding.hopsFilter.setText(enteredFilters.hops)
        binding.maltFilter.setText(enteredFilters.malt)
        binding.foodFilter.setText(enteredFilters.food)
        binding.ibuFromFilter.setText(enteredFilters.ibuFrom.toString())
        binding.ibuToFilter.setText(enteredFilters.ibuTo.toString())
        binding.abvFromFilter.setText(enteredFilters.abvFrom.toString())
        binding.abvToFilter.setText(enteredFilters.abvTo.toString())
        binding.ebcFromFilter.setText(enteredFilters.ebcFrom.toString())
        binding.ebcToFilter.setText(enteredFilters.ebcTo.toString())
        binding.brewedBeforeFilter.setText(enteredFilters.brewedBefore.toString())
        binding.brewedAfterFilter.setText(enteredFilters.brewedAfter.toString())
    }

    private fun getFilters(binding: FilterScreenBinding): Filter {
        val yeast = binding.constraintLayout.yeast_filter.text.toString()
        val hops = binding.constraintLayout.hops_filter.text.toString()
        val malt = binding.constraintLayout.malt_filter.text.toString()
        val food = binding.constraintLayout.food_filter.text.toString()

        val ibuFrom = binding.constraintLayout.ibu_from_filter.text.toString()
        val ibuTo = binding.constraintLayout.ibu_to_filter.text.toString()

        val abvFrom = binding.constraintLayout.abv_from_filter.text.toString()
        val abvTo = binding.constraintLayout.abv_to_filter.text.toString()

        val ebcFrom = binding.constraintLayout.ebc_from_filter.text.toString()
        val ebcTo = binding.constraintLayout.ebc_to_filter.text.toString()

        val brewedBefore = binding.brewedBeforeFilter.text.toString()
        val brewedAfter = binding.brewedAfterFilter.text.toString()

        return Filter(
            yeast = yeast,
            hops = hops,
            malt = malt,
            food = food,
            ibuFrom = getFilterValue(ibuFrom),
            ibuTo = getFilterValue(ibuTo),
            abvFrom = getFilterValue(abvFrom),
            abvTo = getFilterValue(abvTo),
            ebcFrom = getFilterValue(ebcFrom),
            ebcTo = getFilterValue(ebcTo),
            brewedBefore = brewedBefore,
            brewedAfter = brewedAfter
        )
    }

    private fun showDatePickerOnClick(binding: FilterScreenBinding) {
        val calendar = Calendar.getInstance()

        val dateBeforeSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateBefore(calendar)
            }

        binding.brewedBeforeFilter.setOnClickListener {
            DatePickerDialog(
                context!!,
                dateBeforeSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val dateAfterSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateAfter(calendar)
            }

        binding.brewedAfterFilter.setOnClickListener {
            DatePickerDialog(
                context!!,
                dateAfterSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateDateBefore(calendar: Calendar) {
        val format = "MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(format)
        binding.brewedBeforeFilter.text = simpleDateFormat.format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateDateAfter(calendar: Calendar) {
        val format = "MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(format)
        binding.brewedAfterFilter.text = simpleDateFormat.format(calendar.time)
    }

    private fun setClickListenersToCleanFilter(binding: FilterScreenBinding) {
        binding.clearYeastFilter.setOnClickListener {
            binding.yeastFilter.setText("")
        }
        binding.clearHopsFilter.setOnClickListener {
            binding.hopsFilter.setText("")
        }
        binding.clearMaltFilter.setOnClickListener {
            binding.maltFilter.setText("")
        }
        binding.clearFoodFilter.setOnClickListener {
            binding.foodFilter.setText("")
        }
        binding.clearBeforeFilter.setOnClickListener {
            binding.brewedBeforeFilter.text = ""
        }
        binding.clearAfterFilter.setOnClickListener {
            binding.brewedAfterFilter.text = ""
        }
    }


    private fun getFilterValue(filterText: String): Float {
        return if (filterText.isEmpty()) {
            0.0F
        } else {
            filterText.toFloat()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.clear_filters_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear_filters) {
            filtersViewModel.clearShredPreferences()
            clearAllFilters()
            return true
        } else {
            filtersViewModel.saveFilters(getFilters(binding))
            return NavigationUI.onNavDestinationSelected(
                item,
                view!!.findNavController()
            ) || super.onOptionsItemSelected(item)
        }
    }

    private fun clearAllFilters() {
        binding.yeastFilter.setText("")
        binding.maltFilter.setText("")
        binding.hopsFilter.setText("")
        binding.foodFilter.setText("")

        binding.abvFromFilter.setText("")
        binding.abvToFilter.setText("")
        binding.ibuFromFilter.setText("")
        binding.ibuToFilter.setText("")
        binding.ebcFromFilter.setText("")
        binding.ebcToFilter.setText("")

        binding.brewedBeforeFilter.text = ""
        binding.brewedAfterFilter.text = ""
    }
}
