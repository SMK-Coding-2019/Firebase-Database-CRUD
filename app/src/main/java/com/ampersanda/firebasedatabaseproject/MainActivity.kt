package com.ampersanda.firebasedatabaseproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    // TODO : buat list kosongan
    var data = mutableListOf<ItemFromDatabase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data.map { (_, value) -> value.toString() })

        // TODO : findViewById
        val button: Button = findViewById(R.id.button)
        val listView: ListView = findViewById(R.id.list_view)

        // TODO : set adapter
        listView.adapter = adapter

        button.setOnClickListener {
            // TODO : nambahin data ke database
            FirebaseDatabase.getInstance().reference.push().setValue("Lucky")
        }

        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                FirebaseDatabase.getInstance().reference.child(data[position].id).removeValue()
            }

        }

        listView.onItemLongClickListener = object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ): Boolean {
                FirebaseDatabase.getInstance().reference.child(data[position].id).setValue("Cleopatra")

                return true
            }


        }

        FirebaseDatabase.getInstance().reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataFromFirebase: Map<String, Any>? = dataSnapshot.value as Map<String, Any>?

                if (dataFromFirebase !== null) {
                    data = dataFromFirebase.map { (key, value) -> ItemFromDatabase(key, value) }
                        .toMutableList()
                    listView.adapter =
                        ArrayAdapter(
                            this@MainActivity, android.R.layout.simple_list_item_1,
                            dataFromFirebase.map { (_, value) -> value.toString() }.toMutableList()
                        )
                } else {
                    listView.adapter =
                        ArrayAdapter(
                            this@MainActivity, android.R.layout.simple_list_item_1,
                            mutableListOf<ItemFromDatabase>()
                        )
                }
            }
        })
    }
}
