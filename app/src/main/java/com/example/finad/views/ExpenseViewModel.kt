package com.example.finad.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finad.data.remote.CategoryService
import com.example.finad.data.remote.ExpenseService
import com.example.finad.data.remote.dto.CreateExpenseDto
import com.example.finad.data.remote.dto.ListExpenseFilterDto
import com.example.finad.data.remote.entity.Category
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

    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

    var filters by mutableStateOf<ListExpenseFilterDto>(ListExpenseFilterDto())
        private set

    var endReached by mutableStateOf<Boolean>(false)
        private set

    var isLoading by mutableStateOf<Boolean>(true)
        private set

    var isFetchingMore by mutableStateOf<Boolean>(false)
        private set

    private var isInitialLoadDone = false
    private var isFetchingExpenses = false
    private var isFetchingCategories = false
    private var isFetchingAllCategories = false

    fun updateFilters(newFilters: ListExpenseFilterDto) {
        if (isLoading) return

        filters = newFilters.copy(page = 1)
        endReached = false
        isLoading = true
        expenses = emptyList()

        fetchExpenses()
    }

    fun fetchExpenses() {
        if (isFetchingExpenses) return

        isFetchingExpenses = true
        viewModelScope.launch {
            try {
                ExpenseService.getAllExpenses(filters) { success, data ->
                    if (success && data != null) {
                        expenses = data.data
                        total = data.sum
                        if (data.data.size < filters.pageSize) {
                            endReached = true
                        }
                    }
                    isLoading = false
                    isFetchingExpenses = false
                    isInitialLoadDone = true
                }
            } catch (e: Exception) {
                isLoading = false
                isFetchingExpenses = false
                isInitialLoadDone = true
            }
        }
    }

    fun fetchMoreExpenses() {
        if (isFetchingMore || endReached || isFetchingExpenses) return

        isFetchingMore = true
        filters = filters.copy(page = filters.page + 1)

        viewModelScope.launch {
            try {
                ExpenseService.getAllExpenses(filters) { success, data ->
                    if (success && data != null) {
                        expenses = expenses + data.data
                        total = data.sum

                        if (data.data.size < filters.pageSize) {
                            endReached = true
                        }
                    }
                    isFetchingMore = false
                }
            } catch (e: Exception) {
                isFetchingMore = false
            }
        }
    }

    fun fetchExpensesByCategory() {
        if (isFetchingCategories) return

        isFetchingCategories = true
        viewModelScope.launch {
            try {
                ExpenseService.getAllExpensesByCategory(filters) { success, data ->
                    if (success && data != null) {
                        expensesByCategory = data.data
                    }
                    isFetchingCategories = false
                }
            } catch (e: Exception) {
                isFetchingCategories = false
            }
        }
    }

    fun fetchAllCategories() {
        if (isFetchingAllCategories) return

        isFetchingAllCategories = true
        viewModelScope.launch {
            try {
                CategoryService.getAllCategories { success, data ->
                    if (success && data != null) {
                        categories = data
                    }
                    isFetchingAllCategories = false
                }
            } catch (e: Exception) {
                isFetchingAllCategories = false
            }
        }
    }

    fun updateExpense(expense: Expense, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val updateDto =
                    CreateExpenseDto(
                        name = expense.name,
                        value = expense.value,
                        card = expense.card,
                        bank = expense.bank,
                        categoryId = expense.categoryId,
                        timestamp = expense.timestamp
                    )

                ExpenseService.updateExpense(expense.id.toString(), updateDto) { success ->
                    if (success) {
                        expenses =
                            expenses.map { old ->
                                if (old.id == expense.id) {
                                    expense
                                } else {
                                    old
                                }
                            }
                        fetchExpensesByCategory()
                        callback(true)
                    } else {
                        callback(false)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                callback(false)
            }
        }
    }

    fun refreshData() {
        if (isLoading || isFetchingExpenses) return

        isLoading = true
        endReached = false
        fetchExpenses()
        fetchExpensesByCategory()
        fetchAllCategories()
    }
}
