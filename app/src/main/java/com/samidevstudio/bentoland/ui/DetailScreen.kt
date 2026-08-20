package com.samidevstudio.bentoland.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.samidevstudio.bentoland.data.ContentItem
import com.samidevstudio.bentoland.data.MenuItem
import java.util.*

private val BentoRed = Color(0xFFB5342B)
private val ScreenBg = Color(0xFFF7F1E4)

@Composable
fun DetailScreen(item: MenuItem, onBack: () -> Unit) {
    var quantity by remember { mutableIntStateOf(1) }
    val totalPrice = item.price * quantity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBg)
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            PhotoHeader(item = item, onBack = onBack)
            
            Column(
                modifier = Modifier
                    .offset(y = (-20).dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(ScreenBg)
                    .padding(20.dp)
            ) {
                // Name and Price
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = formatPrice(item.price),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BentoRed
                    )
                }

                // Subtitle: name_ja and calories
                Text(
                    text = "${item.nameJa} · ${item.calories}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Long description
                Text(
                    text = item.longDescription,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(24.dp))

                // IN THE BOX
                Text(
                    text = "IN THE BOX",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                ContentsList(contents = item.contents)

                Spacer(modifier = Modifier.height(24.dp))

                // Quantity Stepper
                QuantityStepper(
                    quantity = quantity,
                    onQuantityChange = { newQty ->
                        quantity = newQty
                    }
                )

                Spacer(modifier = Modifier.height(100.dp)) // Space for sticky bottom bar
            }
        }

        // Sticky Bottom Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            if (item.soldOut) {
                Button(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(disabledContainerColor = Color.LightGray)
                ) {
                    Text("Sold out", color = Color.White, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BentoRed)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Buy now", color = Color.White, fontWeight = FontWeight.Bold)
                        Text(formatPrice(totalPrice), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoHeader(item: MenuItem, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(296.dp)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(android.graphics.Color.parseColor(item.gradient.from)),
                        Color(android.graphics.Color.parseColor(item.gradient.to))
                    )
                )
            )
    ) {
        // Watermark Kanji
        Text(
            text = item.kanji,
            fontSize = 240.sp,
            color = Color.White.copy(alpha = 0.1f),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 40.dp, y = 60.dp)
        )

        // Image
        AsyncImage(
            model = item.imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Back Button
        Surface(
            onClick = onBack,
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp),
            color = Color.White.copy(alpha = 0.5f),
            shape = CircleShape
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "←",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Tag Pill
        if (!item.tag.isNullOrEmpty()) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, bottom = 40.dp)
            ) {
                Text(
                    text = item.tag,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun ContentsList(contents: List<ContentItem>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            contents.forEachIndexed { index, content ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = content.name,
                        fontSize = 14.sp
                    )
                    Text(
                        text = content.note,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                if (index < contents.size - 1) {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF0F0F0))
                }
            }
        }
    }
}

@Composable
fun QuantityStepper(quantity: Int, onQuantityChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Quantity", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFE4DAC6), RoundedCornerShape(24.dp))
        ) {
            Surface(
                onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                modifier = Modifier.size(40.dp),
                color = Color.Transparent
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("-", fontSize = 20.sp, color = BentoRed, fontWeight = FontWeight.Bold)
                }
            }
            Text(
                text = quantity.toString(),
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold
            )
            Surface(
                onClick = { if (quantity < 99) onQuantityChange(quantity + 1) },
                modifier = Modifier.size(40.dp),
                color = Color.Transparent
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("+", fontSize = 20.sp, color = BentoRed, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

private fun formatPrice(price: Int): String {
    return "¥" + String.format(Locale.JAPAN, "%,d", price)
}
