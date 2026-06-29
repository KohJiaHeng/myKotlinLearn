package com.example.testbuildlogin
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.testbuildlogin.model.UserCredential
import com.example.testbuildlogin.viewmodel.LoginViewModel
import android.content.Intent
import com.example.testbuildlogin.databinding.MyloginpageBinding


class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: MyloginpageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyloginpageBinding.inflate(layoutInflater)
        setContentView(R.layout.myloginpage)

        binding.loginButton.setOnClickListener {
                val username = binding.usernameInput.text.toString()
                val password = binding.passwordInput.text.toString()
                val userCredential = UserCredential(username = username, password = password)
                val canLogIn = loginViewModel.isLoginValid(userCredential)

                if (canLogIn) {
                    goToUserPage(username = userCredential.username)
                } else {
                    Toast.makeText(
                        this,
                        "Different Username ${userCredential.username}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }


    }
    private fun goToUserPage(username: String) {
        val intent = Intent(this, MyUserPage::class.java).apply {
            putExtra("test", username)
        }
        startActivity(intent)
        finish()
    }
}

