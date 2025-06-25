package com.example.ecomapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecomapp.pages.CategoriesProductPage
import com.example.ecomapp.pages.ProductDetailsPage
import com.example.ecomapp.screens.AuthScreen
import com.example.ecomapp.screens.HomeScreen
import com.example.ecomapp.screens.LoginScreen
import com.example.ecomapp.screens.SignupScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    GlobalNavigation.navController = navController

    val isLoggedIn = Firebase.auth.currentUser != null
    val firstPage = if (isLoggedIn) "home" else "auth"

    NavHost(navController = navController, startDestination = firstPage) {

        composable("auth") {
            AuthScreen(modifier, navController)
        }

        composable("login") {
            LoginScreen(modifier, navController)
        }

        composable("signup") {
            SignupScreen(modifier, navController)
        }

        composable("home") {
            HomeScreen(modifier, navController)
        }

        composable("category-product/{categoryId}") {
            val categoryId = it.arguments?.getString("categoryId")
            CategoriesProductPage(modifier, categoryId!!)
        }

        composable("product-details/{productId}") {
            val productId = it.arguments?.getString("productId")
            ProductDetailsPage(modifier, productId!!)
        }
    }
}

object GlobalNavigation {
    lateinit var navController: NavController
}