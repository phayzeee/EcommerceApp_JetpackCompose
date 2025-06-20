package com.example.ecomapp.pages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CategoriesProductPage(modifier: Modifier = Modifier, categoryId : String) {
    Text(text = "Categories Product Page :::::$categoryId")
}