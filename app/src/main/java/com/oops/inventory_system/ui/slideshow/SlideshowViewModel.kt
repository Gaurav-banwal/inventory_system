package com.oops.inventory_system.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.oops.inventory_system.Item
import com.oops.inventory_system.ProfitData
import java.text.SimpleDateFormat
import java.util.*

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Loading net profit information..."
    }
    val text: LiveData<String> = _text

    private val _profitData = MutableLiveData<ProfitData>()
    val profitData: LiveData<ProfitData> = _profitData

    private val db = FirebaseFirestore.getInstance()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    init {
        loadProfitData()
    }

    private fun loadProfitData() {
        db.collection("profit_data")
            .document("current_profit")
            .addSnapshotListener { document, _ ->
                if (document != null && document.exists()) {
                    val currentMonthProfit = (document.get("currentMonthProfit") as? Number)?.toDouble() ?: 0.0
                    val lastMonthProfit = (document.get("lastMonthProfit") as? Number)?.toDouble() ?: 0.0
                    
                    val profitData = ProfitData(
                        currentMonthProfit = currentMonthProfit,
                        lastMonthProfit = lastMonthProfit
                    )
                    _profitData.value = profitData
                }
            }
    }

    fun updateProfit(profit: Int) {
        val currentProfit = _profitData.value?.currentMonthProfit ?: 0.0
        val newProfit = currentProfit + profit
        
        val profitData = ProfitData(
            currentMonthProfit = newProfit,
            lastMonthProfit = _profitData.value?.lastMonthProfit ?: 0.0
        )
        
        _profitData.value = profitData
        
        // Update Firestore
        db.collection("profit_data")
            .document("current_profit")
            .set(profitData)
    }
}