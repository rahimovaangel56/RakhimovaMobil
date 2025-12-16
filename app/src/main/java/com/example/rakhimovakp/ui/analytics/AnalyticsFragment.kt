package com.example.rakhimovakp.ui.analytics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.rakhimovakp.databinding.FragmentAnalyticsBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AnalyticsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCharts()
        setupObservers()
        viewModel.loadAnalyticsData()
    }

    private fun setupCharts() {
        setupBarChart()
        setupPieChart()
    }

    private fun setupBarChart() {
        // Настройка внешнего вида столбчатой диаграммы
        with(binding.barChart) {
            description.isEnabled = true
            description.text = "Продажи по месяцам"
            description.textSize = 12f
            setDrawGridBackground(false)

            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.granularity = 1f
            xAxis.labelCount = 12

            axisLeft.setDrawGridLines(true)
            axisRight.isEnabled = false

            legend.isEnabled = true
            animateY(1000)
        }
    }

    private fun setupPieChart() {
        // Настройка внешнего вида круговой диаграммы
        with(binding.pieChart) {
            description.isEnabled = true
            description.text = "Распределение по маркам"
            description.textSize = 12f
            setDrawHoleEnabled(true)
            holeRadius = 40f
            transparentCircleRadius = 45f
            setDrawEntryLabels(false)
            setUsePercentValues(true)
            animateY(1000)
        }
    }

    private fun setupObservers() {
        viewModel.analyticsData.observe(viewLifecycleOwner) { data ->
            updateBarChart(data.monthlySales)
            updatePieChart(data.brandDistribution)
            updateStats(data)
        }
    }

    private fun updateBarChart(monthlySales: List<Pair<String, Float>>) {
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        monthlySales.forEachIndexed { index, pair ->
            entries.add(BarEntry(index.toFloat(), pair.second))
            labels.add(pair.first)
        }

        val dataSet = BarDataSet(entries, "Продажи")
        dataSet.color = Color.parseColor("#2196F3")
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 10f

        val data = BarData(dataSet)
        binding.barChart.data = data
        binding.barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.barChart.invalidate()
    }

    private fun updatePieChart(brandDistribution: List<Pair<String, Float>>) {
        val entries = ArrayList<PieEntry>()

        brandDistribution.forEach { pair ->
            entries.add(PieEntry(pair.second, pair.first))
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 12f

        val data = PieData(dataSet)
        binding.pieChart.data = data
        binding.pieChart.invalidate()
    }

    private fun updateStats(data: AnalyticsData) {
        binding.totalSales.text = "₽${"%,.0f".format(data.totalRevenue)}"
        binding.avgSale.text = "₽${"%,.0f".format(data.averageSale)}"
        binding.topBrand.text = data.topBrand
        binding.salesCount.text = data.totalSales.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}