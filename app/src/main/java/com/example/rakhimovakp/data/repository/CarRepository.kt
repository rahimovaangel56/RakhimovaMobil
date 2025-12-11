package com.example.rakhimovakp.data.repository

import com.example.rakhimovakp.data.local.dao.CarDao
import com.example.rakhimovakp.data.models.Car
import javax.inject.Inject

class CarRepository @Inject constructor(
    private val carDao: CarDao
) {

    suspend fun initializeData() {
        val existingCars = carDao.getAllCarsSync()
        if (existingCars.isEmpty()) {
            val initialCars = listOf(
                Car(
                    id = 1,
                    brand = "Haval",
                    name = "Jolion",
                    price = 15000000.0,
                    description = "Уютный кроссовер для города",
                    imageUrl = null
                ),
                Car(
                    id = 2,
                    brand = "Haval",
                    name = "F7",
                    price = 18000000.0,
                    description = "Динамичный дизайн и уверенный привод",
                    imageUrl = null
                ),
                Car(
                    id = 3,
                    brand = "Changan",
                    name = "CS35 Plus",
                    price = 12000000.0,
                    description = "Комфортный салон и компактные размеры",
                    imageUrl = null
                ),
                Car(
                    id = 4,
                    brand = "Changan",
                    name = "CS75 Plus",
                    price = 16000000.0,
                    description = "Сильный мотор и просторный салон",
                    imageUrl = null
                ),
                Car(
                    id = 5,
                    brand = "Omoda",
                    name = "C5",
                    price = 11000000.0,
                    description = "Яркий дизайн и технологичный интерьер",
                    imageUrl = null
                ),
                Car(
                    id = 6,
                    brand = "Jaecoo",
                    name = "J7",
                    price = 20000000.0,
                    description = "Премиальный комфорт и полный привод",
                    imageUrl = null
                )
            )
            carDao.insertAllCars(initialCars)
        }
    }

    fun getAllCars() = carDao.getAllCars()
    fun getCarsByBrand(brand: String) = carDao.getCarsByBrand(brand)
    suspend fun getAllCarsSync() = carDao.getAllCarsSync()
    suspend fun getCarsByBrandSync(brand: String) = carDao.getCarsByBrandSync(brand)
}
