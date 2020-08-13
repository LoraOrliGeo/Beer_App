package com.example.data.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "beersdatabase")
data class DatabaseBeer(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val tagline: String,
    val firstBrewed: String,
    val description: String,
    val imageUrl: String?,
    val abv: Double, // alcohol by volume
    val ibu: Double?, // international bitterness units
    val ebc: Double?, // european brewery convention
    val targetFg: Double?, // target final gravity
    val targetOg: Double?, // target original gravity
    val srm: Double?, // standard reference method
    val ph: Double?,
    val attenuationLevel: Double?,
    @Embedded(prefix = "volume_")
    val volume: Measurable,
    @Embedded(prefix = "boil_")
    val boilVolume: Measurable,
//    @Embedded
//    val method: Method,
//    @Embedded
//    var ingredients: List<Ingredient> = listOf(),
//    @Embedded
//    var foodPairing: List<String> = listOf(),
    val brewersTips: String,
    val contributedBy: String
)

data class Measurable(
    var value: Double,
    var unit: String
)

//data class Method(
//    var mashTemp: Array<MashTemperature> = emptyArray(),
//    val fermentation: Measurable,
//    val twist: String?
//)

data class MashTemperature(val temp: Measurable, val duration: Double?)

open class Ingredient(var type: String, var name: String, var amount: Measurable?)

class Malt(name: String, amount: Measurable) : Ingredient("malt", name, amount)

class Hops(name: String, amount: Measurable, add: String, attribute: String) :
    Ingredient("hops", name, amount)

class Yeast(value: String) : Ingredient("yeast", value, null)
