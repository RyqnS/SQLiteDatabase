package com.example.sqlitedatabase

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlitedatabase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var edName: EditText
    private lateinit var edEmail: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var binding: ActivityMainBinding
    private lateinit var btnUpdate: Button

    private lateinit var dbh: DBH
    private lateinit var recyclerView: RecyclerView
    private var adapter:UserAdapter? = null
    private var user:User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter()
        recyclerView.adapter = adapter
        edEmail = binding.edEmail
        edName = binding.edName
        btnAdd = binding.btnAdd
        btnView = binding.btnView
        btnUpdate = binding.btnUpdate
        dbh = DBH(this)

        btnAdd.setOnClickListener{
            val name = edName.text.toString()
            val email = edEmail.text.toString()

            if(name.isEmpty() || email.isEmpty()){
                Toast.makeText(this,"Please enter required field", Toast.LENGTH_SHORT).show()

            }else{
                val user = User(name = name,email = email)
                val status = dbh.insertUser(user)

                if(status > -1){
                    Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()
                    edName.setText("")
                    edEmail.setText("")
                    edName.requestFocus()
                    val userList = dbh.getAllUsers()
                    Log.e("pppp","$(userList.size)")
                    adapter?.addItems(userList)
                }else{
                    Toast.makeText(this,"Failed to add", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnView.setOnClickListener{
            val userList = dbh.getAllUsers()
            Log.e("pppp","$(userList.size)")
            adapter?.addItems(userList)
        }
        adapter?.setOnClickItem {
            Toast.makeText(this,it.name,Toast.LENGTH_SHORT).show()
            edName.setText(it.name)
            edEmail.setText(it.email)
            user = it

        }

        adapter?.setOnClickDeleteItem {
            deleteUser(it.id)

        }
        btnUpdate.setOnClickListener {
            updateUser()
        }

    }

    private fun updateUser() {
        val name = edName.text.toString()
        val email = edEmail.text.toString()

        if(name == user?.name && email == user?.email){
            Toast.makeText(this,"Nothing changed",Toast.LENGTH_SHORT).show()
            return
        }

        if(user != null){
            val user = User(id = user!!.id, name = name, email = email)
            val status = dbh.updateUser(user)
            if(status > -1){
                edName.setText("")
                edEmail.setText("")
                edName.requestFocus()
                val userList = dbh.getAllUsers()
                Log.e("pppp","$(userList.size)")
                adapter?.addItems(userList)
            }else{
                Toast.makeText(this,"Failed to update", Toast.LENGTH_SHORT).show()
            }
        }else{
            return
        }
    }

    private fun deleteUser(id:Int) {
       val builder = AlertDialog.Builder(this)
       builder.setMessage("Confirm Deletion?")
       builder.setCancelable(true)
       builder.setPositiveButton("Yes") { dialog, _ ->
           dbh.deleteUserById(id)
           val userList = dbh.getAllUsers()
           Log.e("pppp","$(userList.size)")
           adapter?.addItems(userList)
           dialog.dismiss()
       }
       builder.setNegativeButton("No"){ dialog, _ ->
           dialog.dismiss()
       }
       val alert = builder.create()
       alert.show()

    }
}


