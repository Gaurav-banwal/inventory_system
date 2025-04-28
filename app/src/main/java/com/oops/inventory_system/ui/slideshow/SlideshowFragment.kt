package com.oops.inventory_system.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.oops.inventory_system.ProfitData
import com.oops.inventory_system.databinding.FragmentSlideshowBinding
import java.text.NumberFormat
import java.util.Locale

class SlideshowFragment : Fragment() {
    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!
    private lateinit var slideshowViewModel: SlideshowViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        slideshowViewModel = ViewModelProvider(this)[SlideshowViewModel::class.java]
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Observe profit data changes
        slideshowViewModel.profitData.observe(viewLifecycleOwner) { profitData ->
            updateProfitDisplay(profitData)
        }

        return root
    }

    private fun updateProfitDisplay(profitData: ProfitData) {
        // Create Indian Rupee number format
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        
        binding.currentMonthProfit.text = "Current Month Profit: ${numberFormat.format(profitData.currentMonthProfit)}"
        binding.lastMonthProfit.text = "Last Month Profit: ${numberFormat.format(profitData.lastMonthProfit)}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}