package com.ecommerce.android.data

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val imagePath: String = ""
)
