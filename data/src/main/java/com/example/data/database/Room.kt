package com.example.data.database

import android.content.Context
import androidx.paging.DataSource
import androidx.room.*

@Dao
interface BeerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(beers: List<DatabaseBeer>)

    @Query("SELECT * FROM beersdatabase ORDER BY id")
    fun beers(): DataSource.Factory<Int, DatabaseBeer>

    @Query("SELECT * FROM beersdatabase WHERE id = :beerId")
    suspend fun getBeer(beerId: Long): DatabaseBeer

    @Query("DELETE FROM beersdatabase")
    fun deleteBeers()
}

@Dao
interface FavoriteBeerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavoriteBeer(beer: FavoriteBeer?)

    @Query("DELETE FROM favorite_beers WHERE id = :beerId AND ownerId = :ownerId")
    fun deleteFavoriteBeer(beerId: Long?, ownerId: String?)

    @Query("SELECT * FROM favorite_beers WHERE ownerId = :ownerId")
    fun getFavoriteBeers(ownerId: String?): DataSource.Factory<Int, FavoriteBeer>

    @Query("SELECT COUNT(*) FROM favorite_beers WHERE ownerId = :ownerId")
    suspend fun getFavoriteBeersCount(ownerId: String?): Int

    @Query("SELECT * FROM favorite_beers WHERE id = :beerId AND ownerId = :ownerId")
    suspend fun getFavBeerByIdAndOwner(beerId: Long, ownerId: String?): FavoriteBeer

    @Query("SELECT * FROM favorite_beers WHERE ownerId = :ownerId ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomBeer(ownerId: String?): FavoriteBeer
}

@Database(entities = [DatabaseBeer::class, FavoriteBeer::class], version = 6, exportSchema = false)
abstract class BeersDatabase : RoomDatabase() {
    abstract fun beerDao(): BeerDao
    abstract fun favoriteBeerDao(): FavoriteBeerDao
}

private lateinit var INSTANCE: BeersDatabase

fun getDatabase(context: Context): BeersDatabase {
    synchronized(BeersDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext, BeersDatabase::class.java, "beers"
                ).fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}