package com.oops.inventory_system

import java.util.Calendar
//storing profit
data class ProfitData(
    val currentMonthProfit: Double = 0.0,
    val lastMonthProfit: Double = 0.0
) {
    companion object {
        private val profitData = mutableMapOf<String, Double>()
        
        fun addProfit(itemName: String, profit: Double) {
            val currentMonth = getCurrentMonthKey()
            profitData[currentMonth] = (profitData[currentMonth] ?: 0.0) + profit
        }
        
        fun getCurrentMonthProfit(): Double {
            return profitData[getCurrentMonthKey()] ?: 0.0
        }
        
        fun getLastMonthProfit(): Double {
            //getting month here
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -1)
            val lastMonthKey = "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}"
            return profitData[lastMonthKey] ?: 0.0
        }
        
        private fun getCurrentMonthKey(): String {
            val calendar = Calendar.getInstance()
            return "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}"
        }
    }
} 