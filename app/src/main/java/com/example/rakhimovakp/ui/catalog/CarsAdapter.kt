package com.example.rakhimovakp.ui.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rakhimovakp.data.models.Car
import com.example.rakhimovakp.databinding.ItemCarBinding
import java.text.NumberFormat
import java.util.Locale

class CarsAdapter(
    private val onItemClick: (Car) -> Unit = {},
    private val onAddToCart: (Car) -> Unit = {}
) : ListAdapter<Car, CarsAdapter.CarViewHolder>(DiffCallback) {

    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val binding = ItemCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = getItem(position)
        holder.bind(car)
    }

    inner class CarViewHolder(private val binding: ItemCarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(car: Car) = with(binding) {
            carBrandTextView.text = car.brand.uppercase()
            carModelTextView.text = car.name
            carDescriptionTextView.text = car.description?.takeIf { it.isNotBlank() }
                ?: "Свежий экземпляр, готов к тест-драйву."
            carPriceTextView.text = currencyFormatter.format(car.price)
            carYearTextView.text = "New"

            root.setOnClickListener { onItemClick(car) }
            btnAddToCart.setOnClickListener { onAddToCart(car) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Car>() {
        override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean {
            return oldItem == newItem
        }
    }
}
