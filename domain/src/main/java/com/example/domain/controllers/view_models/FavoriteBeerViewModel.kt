package com.example.domain.controllers.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.data.database.FavoriteBeer
import com.example.data.repository.FavoriteBeersRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn

class FavoriteBeerViewModel(application: Application) : AndroidViewModel(application) {

    private val account = GoogleSignIn.getLastSignedInAccount(application)
    private val favoriteBeersRepository = FavoriteBeersRepository(application, account?.id ?: "")

    var favoriteBeers: LiveData<PagedList<FavoriteBeer>> = favoriteBeersRepository.favoriteBeers

    private val _navigatedToDetail = MutableLiveData<Long>()
    val navigatedToDetail: LiveData<Long>
        get() = _navigatedToDetail

    fun onFavBeerClicked(beerId: Long){
        _navigatedToDetail.value = beerId
    }

    fun onFavBeerNavigated(){
        _navigatedToDetail.value = null
    }
}