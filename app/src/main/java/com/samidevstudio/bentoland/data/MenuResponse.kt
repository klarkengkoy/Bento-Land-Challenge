package com.samidevstudio.bentoland.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuResponse(
    val shop: Shop,
    val categories: List<Category>,
    val items: List<MenuItem>
)

@Serializable
data class Shop(
    val name: String,
    val branch: String,
    val tagline: String,
    val currency: String,
    @SerialName("tax_rate") val taxRate: Double,
    val pickup: PickupInfo
)

@Serializable
data class PickupInfo(
    val title: String,
    val description: String
)

@Serializable
data class Category(
    val id: String,
    val label: String
)

@Serializable
data class MenuItem(
    val id: String,
    val kanji: String,
    val name: String,
    @SerialName("name_ja") val nameJa: String,
    val description: String,
    @SerialName("long_description") val longDescription: String,
    val price: Int,
    val calories: Int,
    val category: String,
    val tag: String? = null,
    @SerialName("sold_out") val soldOut: Boolean,
    val gradient: Gradient,
    @SerialName("image_url") val imageUrl: String,
    val contents: List<ContentItem>
)

@Serializable
data class Gradient(
    val from: String,
    val to: String
)

@Serializable
data class ContentItem(
    val name: String,
    val note: String
)
