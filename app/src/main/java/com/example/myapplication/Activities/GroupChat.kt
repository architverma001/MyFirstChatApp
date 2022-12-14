package com.example.myapplication.Activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapters.MessageAdapter
import com.example.myapplication.R
import com.example.myapplication.models.User
import com.example.myapplication.models.message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.send.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GroupChat : AppCompatActivity() {
    private lateinit var chatRecylerView:RecyclerView
    private lateinit var messagebox: EditText
    private lateinit var sentButton:ImageView
    private lateinit var mesaagelist:ArrayList<message>

    private lateinit var mesageAdapter: MessageAdapter
    private lateinit var mDref:DatabaseReference

    var receiveroom :String?=null
    var sendereroom :String?=null
    var names :String?=null
    var num :Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)
        Toast.makeText(this,"Welcome",Toast.LENGTH_SHORT).show()
        mDref = FirebaseDatabase.getInstance().getReference()
        val receiveduid = intent.getStringExtra("uid")
        var num  = -1
        val senderUid =  FirebaseAuth.getInstance().currentUser?.uid
         names = ""

        sendereroom = receiveduid + senderUid
        receiveroom = senderUid + receiveduid

        supportActionBar?.title = "Group_Chats"

        chatRecylerView = findViewById(R.id.GChatRecyclerView)
        messagebox= findViewById(R.id.messageBox)
        sentButton = findViewById(R.id.sendButton)
        mesaagelist = ArrayList()
        mesageAdapter = MessageAdapter(this, mesaagelist)
        chatRecylerView.layoutManager = LinearLayoutManager(this)
        chatRecylerView.adapter = mesageAdapter

        mDref.child("GChats").child("chating")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    mesaagelist.clear()
                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(message::class.java)
                        mesaagelist.add(message!!)
                    }
                    mesageAdapter.notifyDataSetChanged()
                    if(mesageAdapter.itemCount>=1){
                        chatRecylerView.smoothScrollToPosition(mesageAdapter.itemCount-1)}
                }




                override fun onCancelled(error: DatabaseError) {
                    Log.d("Error",error.toString())
                }

            })


        mDref.child("user").child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    for(post in snapshot.children){






                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        sendButton.setOnClickListener {
            val messages = messagebox.text.toString().trim()
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val formatteds = current.format(formatter)
            val nme = names
            if (messagebox.text.isEmpty()) {
                messagebox.setError("message is required")
            } else {
                val messageObject = message(messages, senderUid,formatteds,names)
                mDref.child("GChats").child("chating")
                    .push().setValue(messageObject).addOnSuccessListener {
                    }
                messagebox.setText("")
            }
        }
    }


    override fun onBackPressed() {
//        AlertDialog.Builder(this)
//            .setTitle("Alert")
//            .setMessage("Do You Want To Exit the group chats?")
//            .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
//                super.onBackPressed()
//            }
//            .setNegativeButton(android.R.string.cancel) { dialog, whichButton ->
//
//            }
//            .show()
        Toast.makeText(baseContext,"Redirecting to main page",Toast.LENGTH_SHORT).show()
        super.onBackPressed()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.del,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== R.id.deletes){
            mDref.child("GChats").child("chating").removeValue()
                .addOnFailureListener{
                   Toast.makeText(this@GroupChat,"Failed to delete :( ",Toast.LENGTH_SHORT).show()
                }
            Toast.makeText(this,"Chats are deleted now", Toast.LENGTH_SHORT).show()
            return true
        }
        return true


    }





    }