package com.example.domain.controllers.view_models

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.data.Filter
import com.example.data.repository.FiltersRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn

class FiltersViewModel(application: Application) :
    AndroidViewModel(application) {

    private val account = GoogleSignIn.getLastSignedInAccount(application)
    private val filterRepository = FiltersRepository(application, account?.id ?: "")

    fun saveFilters(filters: Filter) {
        filterRepository.storeInSharedPreferences(filters)
    }

    fun getEnteredFilters(): Filter {
        return filterRepository.getFilterFromSharedPreferences()
    }

    fun clearShredPreferences() {
        filterRepository.clear()
    }
}