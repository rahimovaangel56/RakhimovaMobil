package com.example.rakhimovakp.ui.catalog

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.rakhimovakp.databinding.FragmentAddCarBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddCarFragment : Fragment() {

    private var _binding: FragmentAddCarBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddCarViewModel by viewModels()
    private var selectedImageUri: Uri? = null

    // Контракт для выбора изображения (требуется AndroidX Activity 1.8.0+)
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            binding.imagePreview.setImageURI(uri)
            binding.imagePreview.visibility = View.VISIBLE
        } else {
            Toast.makeText(requireContext(), "Изображение не выбрано", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setupObservers()
    }

    private fun setupClickListeners() {
        // Кнопка выбора изображения
        binding.buttonSelectImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Кнопка добавления
        binding.buttonAddCar.setOnClickListener {
            val brand = binding.editTextBrand.text.toString().trim()
            val name = binding.editTextModel.text.toString().trim()
            val priceText = binding.editTextPrice.text.toString().trim()
            val description = binding.editTextDescription.text.toString().trim()

            if (validateInput(brand, name, priceText)) {
                val price = priceText.toDoubleOrNull() ?: 0.0
                // Передаем строку URI как ссылку на изображение
                val imageUrlString = selectedImageUri?.toString()
                viewModel.addCar(brand, name, price, description, imageUrlString)
            }
        }

        // Кнопка отмены
        binding.buttonCancel.setOnClickListener {
            showCancelConfirmationDialog()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.addCarState.collect { state ->
                when (state) {
                    is AddCarState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.buttonAddCar.isEnabled = false
                    }
                    is AddCarState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Автомобиль '${state.car.brand} ${state.car.name}' добавлен!",
                            Toast.LENGTH_LONG
                        ).show()
                        // Возвращаемся назад после успешного добавления
                        findNavController().popBackStack()
                    }
                    is AddCarState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonAddCar.isEnabled = true
                        Toast.makeText(
                            requireContext(),
                            "Ошибка: ${state.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is AddCarState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonAddCar.isEnabled = true
                    }
                }
            }
        }
    }

    private fun validateInput(brand: String, name: String, priceText: String): Boolean {
        return when {
            brand.isEmpty() -> {
                binding.editTextBrand.error = "Введите марку"
                false
            }
            name.isEmpty() -> {
                binding.editTextModel.error = "Введите модель"
                false
            }
            priceText.isEmpty() || priceText.toDoubleOrNull() == null -> {
                binding.editTextPrice.error = "Введите корректную цену"
                false
            }
            else -> true
        }
    }

    private fun showCancelConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Отменить добавление?")
            .setMessage("Все введенные данные будут потеряны.")
            .setPositiveButton("Да") { _, _ ->
                findNavController().popBackStack()
            }
            .setNegativeButton("Нет", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}