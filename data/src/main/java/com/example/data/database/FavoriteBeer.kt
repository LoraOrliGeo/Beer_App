package com.example.data.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_beers")
data class FavoriteBeer(
    @PrimaryKey
    @Embedded
    val favBeerId: FavBeerId,
    val name: String,
    val imageUrl: String?,
    val tagline: String,
    val description: String,
    val abv: Double, // alcohol by volume
    val ibu: Double?, // international bitterness units
    val ebc: Double? // european brewery convention
//    val foods: List<String>
)

data class FavBeerId(
    val id: Long,
    var ownerId: String
)