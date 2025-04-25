package com.example.finad.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finad.data.remote.ExpenseService
import com.example.finad.data.remote.dto.ListExpenseFilterDto
import com.example.finad.data.remote.entity.Expense
import com.example.finad.data.remote.entity.ExpenseByCategory
import kotlinx.coroutines.launch

class ExpenseViewModel() : ViewModel() {

    var expenses by mutableStateOf<List<Expense>>(emptyList())
        private set


    var total by mutableIntStateOf(0)
        private set

    var expensesByCategory by mutableStateOf<List<ExpenseByCategory>>(emptyList())
        private set

    var filters by mutableStateOf<ListExpenseFilterDto>(ListExpenseFilterDto())
        private set

    var endReached by mutableStateOf<Boolean>(false)
        private set

    var isLoading by mutableStateOf<Boolean>(false)
        private set

    var isFetchingMore by mutableStateOf<Boolean>(false)
        private set

    fun fetchExpenses() {
        viewModelScope.launch {
            isLoading = true
            try {
                ExpenseService.getAllExpenses(filters) {success, data ->
                    if(success) {
                        expenses = data!!.data
                        total = data.sum
                    }
                }
            } catch (e: Exception) {
            } finally {
                isLoading = false
            }
        }
    }

    fun fetchMoreExpenses() {
        filters = filters.copy(page = filters.page + 1)

        viewModelScope.launch {
            try {
                ExpenseService.getAllExpenses(filters) {success, data ->
                    if(success) {
                        expenses = expenses + data!!.data
                        total = data.sum
                        if (expenses.size == data.summary.total) endReached = true
                    }
                }
            } catch (e: Exception) {
            } finally {
                isFetchingMore = false
            }
        }
    }

    fun fetchExpensesByCategory() {
        viewModelScope.launch {
            isLoading = true
            try {
                ExpenseService.getAllExpensesByCategory(filters) {success, data ->
                    if(success) {
                        expensesByCategory = data!!.data
                    }
                }
            } catch (e: Exception) {
            } finally {
                isLoading = false
            }
        }
    }
}
