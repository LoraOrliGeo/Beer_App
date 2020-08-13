package com.example.data.network.DTOs

import com.example.data.database.DatabaseBeer
import com.example.data.database.Measurable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkBeer(
    val id: Long,
    val name: String,
    val tagline: String,
    @Json(name = "first_brewed")
    val firstBrewed: String,
    val description: String,
    @Json(name = "image_url")
    val imageUrl: String?,
    val abv: Double, // alcohol by volume
    val ibu: Double?, // international bitterness units
    val ebc: Double?, // european brewery convention
    @Json(name = "target_fg")
    val targetFg: Double?, // target final gravity
    @Json(name = "target_og")
    val targetOg: Double?, // target original gravity
    val srm: Double?, // standard reference method
    val ph: Double?,
    @Json(name = "attenuation_level")
    val attenuationLevel: Double?,
    val volume: Measurable,
    @Json(name = "boil_volume")
    val boilVolume: Measurable,
//    @Embedded
//    val method: Method,
//    @Embedded
//    var ingredients: List<Ingredient> = listOf(),
//    @Embedded
//    var foodPairing: List<String> = listOf(),
    @Json(name = "brewers_tips")
    val brewersTips: String,
    @Json(name = "contributed_by")
    val contributedBy: String
)

fun NetworkBeer.asDatabaseModel(): DatabaseBeer {
    return DatabaseBeer(
            id = this.id,
            name = this.name,
            tagline = this.tagline,
            firstBrewed = this.firstBrewed,
            description = this.description,
            imageUrl = this.imageUrl,
            abv = this.abv,
            ibu = this.ibu,
            ebc = this.ebc,
            targetFg = this.targetFg,
            targetOg = this.targetOg,
            srm = this.srm,
            ph = this.ph,
            attenuationLevel = this.attenuationLevel,
            volume = this.volume,
            boilVolume = this.boilVolume,
            brewersTips = this.brewersTips,
            contributedBy = this.contributedBy
        )
}