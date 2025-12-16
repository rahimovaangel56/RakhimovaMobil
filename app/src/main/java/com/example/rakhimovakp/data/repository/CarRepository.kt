package com.example.rakhimovakp.data.repository

import androidx.lifecycle.asFlow
import com.example.rakhimovakp.data.local.dao.CarDao
import com.example.rakhimovakp.data.models.Car
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CarRepository @Inject constructor(
    private val carDao: CarDao
) {

    suspend fun initializeData() {
        val existingCars = carDao.getAllCarsSync()
        if (existingCars.isEmpty()) {
            val initialCars = listOf(
                // ВАЖНО: Не указываем 'id'. База данных сгенерирует его сама.
                Car(
                    brand = "Haval",
                    name = "Jolion",
                    price = 15000000.0,
                    description = "Уютный кроссовер для города",
                    imageUrl = null
                ),
                Car(
                    brand = "Haval",
                    name = "F7",
                    price = 18000000.0,
                    description = "Динамичный дизайн и уверенный привод",
                    imageUrl = null
                ),
                Car(
                    brand = "Changan",
                    name = "CS35 Plus",
                    price = 12000000.0,
                    description = "Комфортный салон и компактные размеры",
                    imageUrl = null
                ),
                Car(
                    brand = "Changan",
                    name = "CS75 Plus",
                    price = 16000000.0,
                    description = "Сильный мотор и просторный салон",
                    imageUrl = null
                ),
                Car(
                    brand = "Omoda",
                    name = "C5",
                    price = 11000000.0,
                    description = "Яркий дизайн и технологичный интерьер",
                    imageUrl = null
                ),
                Car(
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

    // ============= Методы для получения данных =============
    fun getAllCars(): Flow<List<Car>> = carDao.getAllCars().asFlow()
    fun getCarsByBrand(brand: String): Flow<List<Car>> = carDao.getCarsByBrand(brand).asFlow()
    suspend fun getAllCarsSync(): List<Car> = carDao.getAllCarsSync()
    suspend fun getCarsByBrandSync(brand: String): List<Car> = carDao.getCarsByBrandSync(brand)

    // ============= НОВЫЙ метод для добавления одной машины =============
    suspend fun insertCar(car: Car) {
        carDao.insertCar(car)
    }
}