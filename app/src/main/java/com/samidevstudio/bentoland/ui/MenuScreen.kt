package com.samidevstudio.bentoland.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.samidevstudio.bentoland.data.MenuItem
import java.text.NumberFormat
import java.util.*

private val BentoRed = Color(0xFFB5342B)
private val ScreenBg = Color(0xFFF7F1E4)
private val PageBg = Color(0xFFEFE7D8)
private val ChipBorder = Color(0xFFE4DAC6)
private val ChipTextUnselected = Color(0xFF6B5F4B)

@Composable
fun MenuScreen(viewModel: MenuViewModel, onItemClick: (MenuItem) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
            .statusBarsPadding(),
        containerColor = ScreenBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Header(
                itemCount = if (uiState is MenuUiState.Success) (uiState as MenuUiState.Success).items.size else 0
            )

            CategoryRow(
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                    viewModel.filterByCategory(if (category == "All") null else category)
                }
            )

            when (val state = uiState) {
                is MenuUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = BentoRed)
                    }
                }
                is MenuUiState.Success -> {
                    MenuList(
                        items = state.items,
                        onItemClick = onItemClick
                    )
                }
                is MenuUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Error: ${state.message}", color = BentoRed)
                    }
                }
            }
        }
    }
}

@Composable
fun Header(itemCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(BentoRed)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Bento Land",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Made fresh this morning · $itemCount today",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun CategoryRow(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("All", "Meat", "Fish", "Veggie")
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onCategorySelected(category) },
                color = if (isSelected) BentoRed else Color.White,
                shape = RoundedCornerShape(20.dp),
                border = if (isSelected) null else BorderStroke(1.dp, ChipBorder)
            ) {
                Text(
                    text = category,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = if (isSelected) Color.White else ChipTextUnselected,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun MenuList(
    items: List<MenuItem>,
    onItemClick: (MenuItem) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->
            MenuCard(item = item, onClick = { onItemClick(item) })
        }
    }
}

@Composable
fun MenuCard(
    item: MenuItem,
    onClick: () -> Unit
) {
    val opacity = if (item.soldOut) 0.45f else 1.0f
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(opacity)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gradient + Kanji + Image Tile
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(android.graphics.Color.parseColor(item.gradient.from)),
                                Color(android.graphics.Color.parseColor(item.gradient.to))
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.kanji,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!item.tag.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.width(6.dp))
                        TagPill(text = item.tag, color = Color(0xFFF0F0F0))
                    }
                    if (item.soldOut) {
                        Spacer(modifier = Modifier.width(6.dp))
                        TagPill(text = "Sold out", color = Color(0xFFFFEBEE), textColor = BentoRed)
                    }
                }
                Text(
                    text = item.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = formatPrice(item.price),
                    color = BentoRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = " ›",
                    color = Color.LightGray,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun TagPill(text: String, color: Color, textColor: Color = Color.Gray) {
    Surface(
        color = color,
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            fontSize = 10.sp,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatPrice(price: Int): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.JAPAN)
    formatter.maximumFractionDigits = 0
    return "¥" + String.format("%,d", price)
}
