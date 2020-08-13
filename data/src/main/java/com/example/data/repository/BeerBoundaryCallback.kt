package com.example.data.repository

import androidx.paging.PagedList
import com.example.data.Filter
import com.example.data.database.DatabaseBeer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val MAX_NUMBER_OF_PAGES: Int = 1000

class BeerBoundaryCallback(val callback: BoundCallbackListener, val scope: CoroutineScope)
    : PagedList.BoundaryCallback<DatabaseBeer>() {

    var totalPages: Int = MAX_NUMBER_OF_PAGES
    var page: Int = 1
    var filter: Filter? = null

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        scope.launch {
            callback.loadPage(page, filter)
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: DatabaseBeer) {
        super.onItemAtEndLoaded(itemAtEnd)
        if(page< totalPages) {
            page++
            scope.launch {
                callback.loadPage(page, filter)
            }
        }
    }
}