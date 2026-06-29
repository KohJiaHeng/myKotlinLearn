package com.example.testbuildlogin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testbuildlogin.model.MyProduct

class MyUserPage : ComponentActivity() {
    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemText: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.userpage)
        val welcomeTitle = findViewById<TextView>(R.id.homeWelcomeTitle)
        val logoutBtn = findViewById<Button>(R.id.logoutButton)
        val username = intent.getStringExtra("test") ?: "User"
        val myRecyclePOV = findViewById<RecyclerView>(R.id.myRecycleList)
        val myArrayContent = arrayOf(
            MyProduct(productName = "Apple", productPrice = 123),
            MyProduct(productName = "Sam Sumg", productPrice = 23),
            MyProduct(productName = "Huawei", productPrice = 45)
        )

        welcomeTitle.text = "Welcome, $username"

        myRecyclePOV.layoutManager = LinearLayoutManager(this)
        myRecyclePOV.adapter = object : RecyclerView.Adapter<ProductViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false)
                return ProductViewHolder(view)
            }

            override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
                val product = myArrayContent[position]
                holder.itemText.text = "${product.productName} — ${product.productPrice}"
            }

            override fun getItemCount(): Int = myArrayContent.size
        }

        logoutBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}