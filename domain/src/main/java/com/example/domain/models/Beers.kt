package com.example.domain.models

import com.example.data.database.DatabaseBeer
import com.example.data.database.FavBeerId
import com.example.data.database.FavoriteBeer

open class BaseBeer(
    open val id: Long = 0,
    open val imageUrl: String?,
    open val name: String,
    open val tagline: String
)

data class DetailBeer(
    var isFavorite: Boolean = false,
    override val id: Long,
    override val imageUrl: String?,
    override val name: String,
    override val tagline: String,
    val description: String,
    val firstBrewed: String,
    val abv: Double,
    val ibu: Double?,
    val ebc: Double?,
    // set them when uncomment ingredients in entity DatabaseBeer
//    val maltContent: String,
//    val hopsContent: String,
//    val yeastContent: String,
//    val foodPairing: List<String>,
    val brewersTips: String,
    val contributedBy: String
) : BaseBeer(id, imageUrl, name, tagline) {

    fun getAbvContentPercentage(): Int {
        return abv.toInt()
    }

    fun getIbuContentIntValue(): Int {
        return ibu?.toInt() ?: 0
    }

    fun getEbcContentIntValue(): Int {
        return ebc?.toInt() ?: 0
    }

    // TODO create more getContent functions for ingredients and for food pairing
}

fun DatabaseBeer.asDomainBaseModel(): BaseBeer {
    val baseBeer = BaseBeer(this.id, this.imageUrl ?: "", this.name, this.tagline)
    return baseBeer
}

fun DatabaseBeer.asDomainDetailBeer(): DetailBeer {
    val detailBeer = DetailBeer(
        id = this.id, imageUrl = this.imageUrl, name = this.name, tagline = this.tagline,
        description = this.description, firstBrewed = this.firstBrewed,
        abv = this.abv, ibu = this.ibu, ebc = this.ebc,
        brewersTips = this.brewersTips, contributedBy = this.contributedBy
    )

    return detailBeer
}

fun FavoriteBeer.asDomainDetailBeer(): DetailBeer {
    return DetailBeer(
        id = this.favBeerId.id, name = this.name, imageUrl = this.imageUrl ?: "",
        tagline = this.tagline, description = this.description,
        firstBrewed = "", abv = this.abv, ibu = this.ibu,
        ebc = this.ebc, brewersTips = "", contributedBy = ""
    )
}

fun DetailBeer?.asFavoriteBeer(): FavoriteBeer? {
    var favoriteBeer: FavoriteBeer? = null
    this?.let {
        favoriteBeer = FavoriteBeer(
            favBeerId = FavBeerId(this.id, ownerId = ""),
            name = this.name,
            imageUrl = this.imageUrl,
            tagline = this.tagline,
            description = this.description,
            abv = this.abv,
            ibu = this.ibu,
            ebc = this.ebc
        )
    }
    return favoriteBeer
}
