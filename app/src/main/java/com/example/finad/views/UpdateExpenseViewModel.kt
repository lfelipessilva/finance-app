package com.example.finad.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finad.data.remote.CategoryService
import com.example.finad.data.remote.ExpenseService
import com.example.finad.data.remote.entity.Category
import com.example.finad.data.remote.entity.Expense
import kotlinx.coroutines.launch

class UpdateExpenseViewModel: ViewModel() {

    var expense by mutableStateOf<Expense?>(null)
        private set

    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    fun loadAll(expenseId: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                ExpenseService.getExpenseById(expenseId) { success, data ->
                    if (success) {
                        expense = data
                    }
                }
                CategoryService.getAllCategories() { success, data ->
                    if (success) {
                        categories = data!!.data
                    }
                }
            } catch (e: Exception) {
            } finally {
                isLoading = false
            }
        }
    }

    fun updateExpense(expenseId: String, updated: Expense, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                ExpenseService.updateExpense(expenseId, updated) { success ->
                    if (success) {
                        onSuccess()
                    }
                }
            } catch (e: Exception) {
            }
        }
    }
}
