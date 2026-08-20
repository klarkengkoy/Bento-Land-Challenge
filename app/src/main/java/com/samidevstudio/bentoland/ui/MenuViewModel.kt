package com.samidevstudio.bentoland.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samidevstudio.bentoland.data.MenuItem
import com.samidevstudio.bentoland.data.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface MenuUiState {
    data object Loading : MenuUiState
    data class Success(val items: List<MenuItem>) : MenuUiState
    data class Error(val message: String) : MenuUiState
}

class MenuViewModel(private val repository: MenuRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<MenuUiState>(MenuUiState.Loading)
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    private var allItems: List<MenuItem> = emptyList()

    init {
        loadMenu()
    }

    fun refreshMenu() {
        loadMenu()
    }

    private fun loadMenu() {
        viewModelScope.launch {
            _uiState.value = MenuUiState.Loading
            repository.fetchMenu()
                .onSuccess { items ->
                    allItems = items
                    _uiState.value = MenuUiState.Success(items)
                }
                .onFailure { error ->
                    _uiState.value = MenuUiState.Error(error.message ?: "Unknown error")
                }
        }
    }

    fun filterByCategory(category: String?) {
        if (category.isNullOrEmpty()) {
            _uiState.value = MenuUiState.Success(allItems)
        } else {
            val filtered = allItems.filter { it.category.equals(category, ignoreCase = true) }
            _uiState.value = MenuUiState.Success(filtered)
        }
    }
}
