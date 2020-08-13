package com.example.domain.controllers.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.data.repository.BeersRepository
import com.example.data.repository.FavoriteBeersRepository
import com.example.data.repository.MAX_NUMBER_OF_FAVORITE_BEERS
import com.example.domain.models.DetailBeer
import com.example.domain.models.asDomainDetailBeer
import com.example.domain.models.asFavoriteBeer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.*

class DetailBeerViewModel(application: Application) : AndroidViewModel(application) {

    var beerId = -1L
        set(value) {
            field = value
            viewModelScope.launch {
                var beerFromRepo = beersRepository.getBeer(value)?.asDomainDetailBeer()
                if(beerFromRepo == null){
                    beerFromRepo = favoriteBeersRepository.getBeerByIdAndOwner(value).asDomainDetailBeer()
                }
                beer.value = beerFromRepo
            }
        }

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val beersRepository = BeersRepository(application, viewModelScope)

    private val account = GoogleSignIn.getLastSignedInAccount(application)
    private val favoriteBeersRepository = FavoriteBeersRepository(application, account?.id ?: "")

    var beer = MutableLiveData<DetailBeer>()

    var isFavoriteBeer = false
    var couldBeAdded = true

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun checkForFavorite(beerId: Long, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val favoriteBeer = favoriteBeersRepository.getBeerByIdAndOwner(beerId)
            isFavoriteBeer = favoriteBeer != null
            onComplete(isFavoriteBeer)
        }
    }

    fun addToFavorites(beerId: Long) {
        viewModelScope.launch {
            val detailBeer = beersRepository.getBeer(beerId)?.asDomainDetailBeer()
            detailBeer?.isFavorite = true
            isFavoriteBeer = true
            favoriteBeersRepository.addFavorite(detailBeer.asFavoriteBeer())
        }
    }

    fun deleteFromFavorites(beerId: Long) {
        viewModelScope.launch {
            favoriteBeersRepository.deleteFromFavorites(beerId)
        }
    }

    fun couldBeAdded(onComplete: (Boolean) -> Unit){
        viewModelScope.launch {
            val totalNumber = favoriteBeersRepository.getTotalNumberByOwner()
            if(totalNumber >= MAX_NUMBER_OF_FAVORITE_BEERS){
                couldBeAdded = false
            }
            onComplete(couldBeAdded)
        }
    }
}