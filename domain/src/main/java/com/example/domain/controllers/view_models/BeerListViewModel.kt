package com.example.domain.controllers.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.data.database.DatabaseBeer
import com.example.data.repository.BeersRepository
import com.example.data.repository.FavoriteBeersRepository
import com.example.data.repository.FiltersRepository
import com.example.data.repository.SettingsRepository
import com.example.domain.models.DetailBeer
import com.example.domain.models.asDomainDetailBeer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.*

class BeerListViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val beersRepository = BeersRepository(application, viewModelScope)

    var filterName: String? = ""

    private val account = GoogleSignIn.getLastSignedInAccount(application)
    private val filtersRepository = FiltersRepository(application, account?.id ?: "")
    private val settingsRepository = SettingsRepository(application, account?.id ?: "")
    private val favoriteBeersRepository = FavoriteBeersRepository(application, account?.id ?: "")

    init {
        viewModelScope.launch {
            beersRepository.getBeersByFilter(1, filtersRepository.getFilterFromSharedPreferences())
        }
    }

    var pagedBeersLiveData: LiveData<PagedList<DatabaseBeer?>> = beersRepository.beers

    fun filterBeersByFilter(filter: String?) {
        filterName = filter
        val filterFromSharedPreferences = filtersRepository.getFilterFromSharedPreferences()
        filterFromSharedPreferences.name = filterName
        viewModelScope.launch {
            beersRepository.getBeersByFilter(1, filterFromSharedPreferences)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    private val _navigateToBeerClicked = MutableLiveData<Long>()
    val navigateToBeerClicked: LiveData<Long>
        get() = _navigateToBeerClicked

    fun onBeerClicked(beerId: Long) {
        _navigateToBeerClicked.value = beerId
    }

    fun onDetailBeerNavigated() {
        _navigateToBeerClicked.value = null
    }

    suspend fun getRandomBeer(): DetailBeer? {
        var randomBeer: DetailBeer? = null
        if (settingsRepository.getRandomBeerFavorites()) {
            if(hasFavorites()) {
                randomBeer = favoriteBeersRepository.getRandomBeer().asDomainDetailBeer()
            }
        } else {
            randomBeer = beersRepository.getRandomBeer().asDomainDetailBeer()
        }
        return randomBeer
    }

    private suspend fun hasFavorites(): Boolean {
        val number = favoriteBeersRepository.getTotalNumberByOwner()
        return number > 0
    }
}