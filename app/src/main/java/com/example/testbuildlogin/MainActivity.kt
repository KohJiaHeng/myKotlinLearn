package com.example.testbuildlogin
import android.os.Bundle
import android.os.Build
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.testbuildlogin.model.UserCredential
import com.example.testbuildlogin.viewmodel.LoginViewModel
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.example.testbuildlogin.databinding.MyloginpageBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: MyloginpageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyloginpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NotificationHelper.createNotificationChannel(context = this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }

        binding.loginButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val userCredential = UserCredential(username = username, password = password)
            lifecycleScope.launch(Dispatchers.Main) {
                if(username.isEmpty()){
                    binding.usernameInput.error = "Username cannot be empty"
                }
                if(password.isEmpty()){
                    binding.passwordInput.error = "Password cannot be empty"
                }


                val canLogIn = loginViewModel.isLoginValid(userCredential)
                if (canLogIn) {
                    NotificationHelper.showWelcomeNotification(this@MainActivity, username)
                    goToUserPage(username = userCredential.username)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Different Username ${userCredential.username}",
                        Toast.LENGTH_LONG
                    ).show()
                    NotificationHelper.showFailToLogin(this@MainActivity, username)
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



