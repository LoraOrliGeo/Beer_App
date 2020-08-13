package com.example.data.repository

import android.content.Context
import com.example.data.Filter
import com.example.data.shared_preferences.SharedPreferencesDataSource

class FiltersRepository(val context: Context?, ownerId: String) {
    private var sharedPreferences =
        SharedPreferencesDataSource(context, ownerId)

    fun storeInSharedPreferences(filter: Filter) {
        sharedPreferences.putString("beer_name", filter.name)
        sharedPreferences.putString("yeast", filter.yeast)
        sharedPreferences.putString("hops", filter.hops)
        sharedPreferences.putString("malt", filter.malt)
        sharedPreferences.putString("food", filter.food)
        sharedPreferences.putFloat("ibuFrom", filter.ibuFrom)
        sharedPreferences.putFloat("ibuTo", filter.ibuTo)
        sharedPreferences.putFloat("abvFrom", filter.abvFrom)
        sharedPreferences.putFloat("abvTo", filter.abvTo)
        sharedPreferences.putFloat("ebcFrom", filter.ebcFrom)
        sharedPreferences.putFloat("ebcTo", filter.ebcTo)
        sharedPreferences.putString("brewedBefore", filter.brewedBefore)
        sharedPreferences.putString("brewedAfter", filter.brewedAfter)
    }

    fun getFilterFromSharedPreferences(): Filter {
        return Filter(
            name = sharedPreferences.getString("beer_name"),
            yeast = sharedPreferences.getString("yeast"),
            hops = sharedPreferences.getString("hops"),
            malt = sharedPreferences.getString("malt"),
            food = sharedPreferences.getString("food"),
            ibuFrom = sharedPreferences.getFloat("ibuFrom"),
            ibuTo = sharedPreferences.getFloat("ibuTo"),
            abvFrom = sharedPreferences.getFloat("abvFrom"),
            abvTo = sharedPreferences.getFloat("abvTo"),
            ebcFrom = sharedPreferences.getFloat("ebcFrom"),
            ebcTo = sharedPreferences.getFloat("ebcTo"),
            brewedBefore = sharedPreferences.getString("brewedBefore"),
            brewedAfter = sharedPreferences.getString("brewedAfter")
        )
    }

    fun clear(){
        sharedPreferences.clearAll()
    }
}