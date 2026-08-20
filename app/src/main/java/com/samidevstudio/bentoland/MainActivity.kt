package com.samidevstudio.bentoland

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.samidevstudio.bentoland.data.BentoClient
import com.samidevstudio.bentoland.data.MenuItem
import com.samidevstudio.bentoland.data.MenuRepository
import com.samidevstudio.bentoland.ui.DetailScreen
import com.samidevstudio.bentoland.ui.MenuScreen
import com.samidevstudio.bentoland.ui.MenuViewModel

sealed class Screen {
    data object Menu : Screen()
    data class Detail(val item: MenuItem) : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val repository = MenuRepository(BentoClient.apiService)
        
        setContent {
            var currentScreen by remember { mutableStateOf<Screen>(Screen.Menu) }

            val viewModel: MenuViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return MenuViewModel(repository) as T
                    }
                }
            )

            when (val screen = currentScreen) {
                is Screen.Menu -> {
                    MenuScreen(
                        viewModel = viewModel,
                        onItemClick = { item ->
                            currentScreen = Screen.Detail(item)
                        }
                    )
                }
                is Screen.Detail -> {
                    DetailScreen(
                        item = screen.item,
                        onBack = { currentScreen = Screen.Menu }
                    )
                }
            }
        }
    }
}
