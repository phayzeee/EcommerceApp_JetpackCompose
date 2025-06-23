package com.example.ecomapp.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecomapp.model.CategoriesModel
import com.example.ecomapp.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CategoriesProductPage(modifier: Modifier = Modifier, categoryId : String) {
    val productsList = remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data").document("stock")
            .collection("products")
            .whereEqualTo("category",categoryId)
            .get().addOnCompleteListener() {
                if (it.isSuccessful) {
                    productsList.value = it.result.documents.mapNotNull { doc ->
                        doc.toObject(ProductModel::class.java)
                    }
                }
            }
    }

    LazyColumn (
        modifier = modifier.fillMaxSize()
            .padding(16.dp)
    ){
        items(productsList.value) { item ->
            Text(text = item.title)
        }
    }
}