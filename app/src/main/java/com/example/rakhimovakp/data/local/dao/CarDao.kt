package com.example.rakhimovakp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rakhimovakp.data.models.Car

@Dao
interface CarDao {
    @Query("SELECT * FROM cars")
    fun getAllCars(): LiveData<List<Car>>

    @Query("SELECT * FROM cars WHERE brand = :brand")
    fun getCarsByBrand(brand: String): LiveData<List<Car>>

    @Query("SELECT * FROM cars")
    suspend fun getAllCarsSync(): List<Car>

    @Query("SELECT * FROM cars WHERE brand = :brand")
    suspend fun getCarsByBrandSync(brand: String): List<Car>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: Car)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCars(cars: List<Car>)

    @Query("DELETE FROM cars")
    suspend fun deleteAllCars()
}
