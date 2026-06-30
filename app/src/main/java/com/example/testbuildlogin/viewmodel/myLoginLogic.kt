package com.example.testbuildlogin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.testbuildlogin.model.UserCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    suspend fun isLoginValid(credential: UserCredential): Boolean {
        return withContext(Dispatchers.IO) {
//            delay(1.seconds)
            credential.username == credential.password
        }
    }


}