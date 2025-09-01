package com.example.ecomapp.model

data class UserModel(
    val name : String = "",
    val email : String = "",
    val uid : String = "",
    var cartItems : Map<String, Long> = emptyMap(),
    val address : String = ""
)