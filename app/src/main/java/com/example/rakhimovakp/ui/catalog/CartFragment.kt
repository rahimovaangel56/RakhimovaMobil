package com.example.rakhimovakp.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rakhimovakp.R
import com.example.rakhimovakp.data.models.Car
import com.example.rakhimovakp.databinding.FragmentCartBinding
import com.example.rakhimovakp.ui.viewmodels.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupList()
        setupActions()
        observeCart()
    }

    private fun setupList() {
        cartAdapter = CartAdapter(
            onIncrement = { item -> cartViewModel.add(item.carId, item.carName, item.carPrice, item.imageUrl) },
            onDecrement = { item -> cartViewModel.decrement(item.carId) },
            onRemove = { item -> cartViewModel.remove(item.carId) }
        )
        binding.cartRecyclerView.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setupActions() {
        binding.emptyActionButton.setOnClickListener {
            findNavController().navigate(R.id.catalogFragment)
        }

        binding.btnClearCart.setOnClickListener {
            cartViewModel.clear()
        }

        binding.btnCheckout.setOnClickListener {
            val carItems = cartViewModel.items.value
            val action = CartFragmentDirections.actionCartFragmentToCheckoutFragment(
                carId = carItems.map { it.carId }.toLongArray()
            )
            findNavController().navigate(action)
        }
    }

    private fun observeCart() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    cartViewModel.items.collect { items ->
                        cartAdapter.submitList(items)
                        binding.emptyState.isVisible = items.isEmpty()
                        binding.cartRecyclerView.isVisible = items.isNotEmpty()
                    }
                }
                launch {
                    cartViewModel.total.collect { sum ->
                        binding.cartTotal.text =
                            NumberFormat.getCurrencyInstance(Locale("ru", "RU")).format(sum)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
