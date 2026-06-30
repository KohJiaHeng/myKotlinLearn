package com.example.testbuildlogin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.testbuildlogin.model.UserCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class LoginViewModel : ViewModel() {
    suspend fun isLoginValid(credential: UserCredential): Boolean {
        return withContext(Dispatchers.IO) {
            delay(2.seconds)
            credential.username == credential.password
        }
    }
}