package com.example.rakhimovakp.ui.analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class AnalyticsViewModel @Inject constructor() : ViewModel() {

    private val _analyticsData = MutableLiveData<AnalyticsData>()
    val analyticsData: LiveData<AnalyticsData> = _analyticsData

    fun loadAnalyticsData() {
        viewModelScope.launch {
            // Имитация загрузки данных
            delay(500)

            // Демо-данные для примера
            val data = AnalyticsData(
                monthlySales = listOf(
                    "Янв" to 4500000f,
                    "Фев" to 5200000f,
                    "Мар" to 6100000f,
                    "Апр" to 4800000f,
                    "Май" to 7200000f,
                    "Июн" to 8900000f,
                    "Июл" to 9300000f,
                    "Авг" to 8700000f,
                    "Сен" to 9500000f,
                    "Окт" to 10100000f,
                    "Ноя" to 11000000f,
                    "Дек" to 12500000f
                ),
                brandDistribution = listOf(
                    "Haval" to 35f,
                    "Changan" to 25f,
                    "Omoda" to 20f,
                    "Jaecoo" to 15f,
                    "Другие" to 5f
                ),
                totalRevenue = 98_700_000,
                averageSale = 18_500_000,
                topBrand = "Haval Jolion",
                totalSales = 154
            )

            _analyticsData.value = data
        }
    }
}

data class AnalyticsData(
    val monthlySales: List<Pair<String, Float>>,
    val brandDistribution: List<Pair<String, Float>>,
    val totalRevenue: Double,
    val averageSale: Double,
    val topBrand: String,
    val totalSales: Int
)