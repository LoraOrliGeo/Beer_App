package com.example.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.data.database.FavoriteBeer
import com.example.data.database.getDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val MAX_NUMBER_OF_FAVORITE_BEERS = 15

class FavoriteBeersRepository(context: Context, private val ownerId: String) {

    private val database = getDatabase(context)

    val favoriteBeers: LiveData<PagedList<FavoriteBeer>> = let {
        val dataSource = database.favoriteBeerDao().getFavoriteBeers(ownerId)
        val config = PagedList.Config.Builder().setPageSize(15).build()
        LivePagedListBuilder(dataSource, config).build()
    }

    suspend fun addFavorite(beer: FavoriteBeer?) {
        withContext(Dispatchers.IO) {
            beer?.favBeerId?.ownerId = ownerId
            val currentNumberFavBeers =
                database.favoriteBeerDao().getFavoriteBeersCount(ownerId)
            if (currentNumberFavBeers < MAX_NUMBER_OF_FAVORITE_BEERS) {
                database.favoriteBeerDao().addFavoriteBeer(beer)
            }
        }
    }

    suspend fun deleteFromFavorites(beerId: Long) {
        withContext(Dispatchers.IO) {
            database.favoriteBeerDao().deleteFavoriteBeer(beerId, ownerId)
        }
    }

    suspend fun getBeerByIdAndOwner(beerId: Long): FavoriteBeer {
        return database.favoriteBeerDao().getFavBeerByIdAndOwner(beerId, ownerId)
    }

    suspend fun getTotalNumberByOwner(): Int {
        return database.favoriteBeerDao().getFavoriteBeersCount(ownerId)
    }

    suspend fun getRandomBeer(): FavoriteBeer {
        return database.favoriteBeerDao().getRandomBeer(ownerId)
    }
}