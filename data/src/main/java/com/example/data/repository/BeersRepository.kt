package com.example.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.data.*
import com.example.data.database.BeersDatabase
import com.example.data.database.DatabaseBeer
import com.example.data.database.getDatabase
import com.example.data.network.BeerApiService
import com.example.data.network.DTOs.asDatabaseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class BeersRepository(context: Context, scope: CoroutineScope) :
    BoundCallbackListener {

    private val database: BeersDatabase = getDatabase(context)

    val beerService = BeerApiService.getService()
    val beerBoundaryCallback = BeerBoundaryCallback(this, scope)

    var beers: LiveData<PagedList<DatabaseBeer?>> = let {
        val dataSourceFactory = database.beerDao().beers()
        val config = PagedList.Config.Builder().setPageSize(BEERS_PER_PAGE).build()
        LivePagedListBuilder(dataSourceFactory, config)
            .setBoundaryCallback(beerBoundaryCallback)
            .build()
    }

    companion object {
        private const val BEERS_PER_PAGE = 25
    }

    suspend fun getBeer(id: Long): DatabaseBeer? {
        return database.beerDao().getBeer(id)
    }

    override suspend fun loadPage(page: Int, filter: Filter?) {
        val mapOfFilters: MutableMap<String, String?> =
            mutableMapOf("page" to page.toString(), "per_page" to BEERS_PER_PAGE.toString())
        mapOfFilters.putAll(getFiltersAsMap(filter))

        filter?.name?.let {
            if (it.isNotEmpty()) {
                mapOfFilters["beer_name"] = it
            }
        }

        withContext(Dispatchers.IO) {
            val beersFiltered = beerService.getBeers(mapOfFilters).await()
            if (beersFiltered.isEmpty() || beersFiltered.size < BEERS_PER_PAGE) {
                beerBoundaryCallback.totalPages = page
            }

            beerBoundaryCallback.filter = filter
            database.beerDao().insertAll(beersFiltered.map { e -> e.asDatabaseModel() })
        }
    }

    suspend fun getBeersByFilter(page: Int, filter: Filter?) {
        val mapOfFilters: MutableMap<String, String?> =
            mutableMapOf("page" to page.toString(), "per_page" to BEERS_PER_PAGE.toString())
        mapOfFilters.putAll(getFiltersAsMap(filter))

        withContext(Dispatchers.IO) {
            try {
                val beersFiltered = beerService.getBeers(mapOfFilters).await()
                if (beersFiltered.isEmpty() || beersFiltered.size < BEERS_PER_PAGE) {
                    beerBoundaryCallback.totalPages = page
                } else {
                    beerBoundaryCallback.totalPages = MAX_NUMBER_OF_PAGES
                }
                beerBoundaryCallback.page = 1
                beerBoundaryCallback.filter = filter
                database.beerDao().deleteBeers()
                database.beerDao().insertAll(beersFiltered.map { e -> e.asDatabaseModel() })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getRandomBeer(): DatabaseBeer {
        val randomBeer = beerService.getRandomBeer().await()[0].asDatabaseModel()
        withContext(Dispatchers.IO) {
            database.beerDao().insertAll(listOf(randomBeer))
        }
        return randomBeer
    }

    private fun getFiltersAsMap(filter: Filter?): MutableMap<String, String?> {
        val mapOfFilters = mutableMapOf<String, String?>()

        if ((filter?.name?.isEmpty() == false)) {
            mapOfFilters["beer_name"] = filter.name
        }
        if (!filter?.yeast.isNullOrEmpty()) {
            mapOfFilters["yeast"] = filter?.yeast
        }
        if (!filter?.hops.isNullOrEmpty()) {
            mapOfFilters["hops"] = filter?.hops
        }
        if (!filter?.malt.isNullOrEmpty()) {
            mapOfFilters["malt"] = filter?.malt
        }
        if (!filter?.food.isNullOrEmpty()) {
            mapOfFilters["food"] = filter?.food
        }

        filter?.ibuFrom?.let {
            if (it > IBU_MIN_VALUE && it <= IBU_MAX_VALUE) {
                mapOfFilters["ibu_gt"] = filter.ibuFrom.toString()
            }
        }

        filter?.ibuTo?.let {
            if (it > IBU_MIN_VALUE && it <= IBU_MAX_VALUE) {
                mapOfFilters["ibu_lt"] = filter.ibuFrom.toString()
            }
        }

        filter?.abvFrom?.let {
            if (it > ABV_MIN_VALUE && it <= ABV_MAX_VALUE) {
                mapOfFilters["abv_gt"] = filter.abvFrom.toString()
            }
        }

        filter?.abvTo?.let {
            if (it > ABV_MIN_VALUE && it <= ABV_MAX_VALUE) {
                mapOfFilters["abv_lt"] = filter.abvTo.toString()
            }
        }

        filter?.ebcFrom?.let {
            if (it > EBC_MIN_VALUE && it <= EBC_MAX_VALUE) {
                mapOfFilters["ebc_gt"] = filter.ebcFrom.toString()
            }
        }

        filter?.ebcTo?.let {
            if (it > EBC_MIN_VALUE && it <= EBC_MAX_VALUE) {
                mapOfFilters["ebc_lt"] = filter.ebcTo.toString()
            }
        }

        filter?.brewedAfter?.let {
            if (it.isNotEmpty()) {
                mapOfFilters["brewed_after"] = filter.brewedAfter
            }
        }

        filter?.brewedBefore?.let {
            if (it.isNotEmpty()) {
                mapOfFilters["brewed_before"] = filter.brewedBefore
            }
        }
        return mapOfFilters
    }
}

interface BoundCallbackListener {
    suspend fun loadPage(page: Int, filter: Filter?)
}