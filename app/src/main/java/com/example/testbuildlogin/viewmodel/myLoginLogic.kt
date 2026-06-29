package com.example.testbuildlogin.viewmodel
import androidx.lifecycle.ViewModel
import com.example.testbuildlogin.model.UserCredential

class LoginViewModel : ViewModel() {
    fun isLoginValid(credential: UserCredential): Boolean {
        return credential.username == credential.password
    }
}