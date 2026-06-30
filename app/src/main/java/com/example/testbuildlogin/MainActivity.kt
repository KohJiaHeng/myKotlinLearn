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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myloginpage)

        val usernameEdit = findViewById<EditText>(R.id.usernameInput)
        val passwordEdit = findViewById<EditText>(R.id.passwordInput)
        val loginBtn = findViewById<Button>(R.id.loginButton)

        loginBtn.setOnClickListener {
            val username = usernameEdit.text.toString()
            val password = passwordEdit.text.toString()
            val userCredential = UserCredential(username = username, password = password)

            lifecycleScope.launch(Dispatchers.Main){
                val canLogIn = loginViewModel.isLoginValid(userCredential)

                if (canLogIn) {
                    goToUserPage(username = userCredential.username)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Different Username ${userCredential.username}",
                        Toast.LENGTH_LONG
                    ).show()
                }
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

