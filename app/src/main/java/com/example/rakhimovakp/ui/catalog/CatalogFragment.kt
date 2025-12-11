package com.example.rakhimovakp.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rakhimovakp.R
import com.example.rakhimovakp.data.models.Car
import com.example.rakhimovakp.databinding.FragmentCatalogBinding
import com.example.rakhimovakp.ui.viewmodels.CartViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    private val catalogViewModel: CatalogViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var carsAdapter: CarsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupChipGroup()
        observeData()
        setupCartShortcut()
    }

    private fun setupRecyclerView() {
        carsAdapter = CarsAdapter(
            onItemClick = { car ->
                showCarDetails(car)
            },
            onAddToCart = { car ->
                val title = "${car.brand} ${car.name}"
                cartViewModel.add(car.id, title, car.price, car.imageUrl)
                Toast.makeText(
                    requireContext(),
                    "$title добавлен в корзину",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        binding.carsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = carsAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupChipGroup() {
        binding.brandChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                when (checkedIds[0]) {
                    binding.chipAll.id -> {
                        catalogViewModel.filterCarsByBrand(null)
                    }

                    else -> {
                        val selectedChip = group.findViewById<Chip>(checkedIds[0])
                        val selectedBrand = selectedChip.text.toString()
                        catalogViewModel.filterCarsByBrand(selectedBrand)
                    }
                }
            }
        }
    }

    private fun observeData() {
        catalogViewModel.cars.observe(viewLifecycleOwner) { cars ->
            carsAdapter.submitList(cars)
        }

        catalogViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingIndicator.isVisible = isLoading
        }

        catalogViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "Ошибка: $it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupCartShortcut() {
        binding.btnGoToCart.setOnClickListener {
            findNavController().navigate(R.id.cartFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.count.collect { count ->
                    val suffix = if (count > 0) " ($count)" else ""
                    binding.btnGoToCart.text = "Корзина$suffix"
                }
            }
        }
    }

    private fun showCarDetails(car: Car) {
        Toast.makeText(
            requireContext(),
            "Вы выбрали: ${car.brand} ${car.name}",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
