package com.example.data

const val IBU_MIN_VALUE = 0.0F
const val IBU_MAX_VALUE = 1157.0F
const val ABV_MIN_VALUE = 0.0F
const val ABV_MAX_VALUE = 55.0F
const val EBC_MIN_VALUE = 2.0F
const val EBC_MAX_VALUE = 600.0F

data class Filter(
    var name: String? = null,
    var yeast: String? = null,
    var hops: String? = null,
    var malt: String? = null,
    var food: String? = null,
    var ibuFrom: Float? = null,
    var ibuTo: Float? = null,
    var abvFrom: Float? = null,
    var abvTo: Float? = null,
    var ebcFrom: Float? = null,
    var ebcTo: Float? = null,
    var brewedBefore: String? = null,
    var brewedAfter: String? = null
)