package com.example.domain.controllers.view_model_factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.controllers.view_models.BeerListViewModel

class BeerListViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BeerListViewModel::class.java)) {
            return BeerListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}