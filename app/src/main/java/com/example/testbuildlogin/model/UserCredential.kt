package com.example.testbuildlogin.model // 👈 Updated to lowercase

data class UserCredential(
    val username: String,
    val password: String? = null
)