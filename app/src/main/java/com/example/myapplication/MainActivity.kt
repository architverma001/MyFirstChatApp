package com.example.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.user_layout.*
import kotlinx.android.synthetic.main.user_layout.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userList:ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var searchViews:SearchView
    private lateinit var mDbref :DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userList = ArrayList()
        adapter = UserAdapter(this,userList)

        mAuth = FirebaseAuth.getInstance()
        searchViews = findViewById(R.id.searchView)
        searchViews.clearFocus()
       searchViews.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
           override fun onQueryTextSubmit(query: String?): Boolean {
             Toast.makeText(this@MainActivity,"listen",Toast.LENGTH_SHORT).show()
               return true
           }

           override fun onQueryTextChange(newText: String?): Boolean {
               Toast.makeText(this@MainActivity,"listen again",Toast.LENGTH_SHORT).show()
               return false
           }

       })
        recyclerView = findViewById(R.id.userRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        mDbref = FirebaseDatabase.getInstance().reference
        mDbref.child("user").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)
                  if(mAuth.currentUser?.uid!=currentUser?.uid)
                      userList.add(currentUser!!)
                }
             adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
               Log.d("Error",error.toString())
            }

        })






    }
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Exit Alert")
            .setMessage("Do You Want To Exit  App?")
            .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                super.onBackPressed()
            }
            .setNegativeButton(android.R.string.cancel) { dialog, whichButton ->

            }
            .show()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.logout){
            mAuth.signOut()
            Toast.makeText(baseContext,"user Logged out",Toast.LENGTH_SHORT).show()
            val intent = Intent(baseContext,login::class.java)
            startActivity(intent)
            return true
        }

        if(item.itemId==R.id.posts){
            val intents = Intent(this@MainActivity,PostActivity::class.java)
            Toast.makeText(this,"Directed to GroupChats",Toast.LENGTH_SHORT).show()
            startActivity(intents)
            return true

       }
        if(item.itemId==R.id.gchat){
            val intents = Intent(this@MainActivity,GroupChat::class.java)
            Toast.makeText(this,"Directed to posts",Toast.LENGTH_SHORT).show()
            startActivity(intents)
            return true

        }
        return true

    }




}