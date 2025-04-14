package com.oops.inventory_system

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class selection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_selection)

        val category = intent.getStringExtra("category_name")
        Toast.makeText(this, "Selected Category: $category", Toast.LENGTH_SHORT).show()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            supportActionBar?.title = category
            window.statusBarColor= ContextCompat.getColor(this,R.color.brandgreen)
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val itemList = listOf("Item 1", "Item 2", "Item 3", "Item 4","Item 5","Item 6")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemList)
        val listView = findViewById<ListView>(R.id.listview)
        listView.adapter = adapter



        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String

            val intent = Intent(this, details::class.java).apply {
                putExtra("item_details", selectedItem)
            }
            startActivity(intent)
        }





    }






}