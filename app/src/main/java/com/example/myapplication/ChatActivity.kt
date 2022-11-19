package com.example.myapplication


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecylerView:RecyclerView
    private lateinit var messagebox: EditText
    private lateinit var sentButton:ImageView
    private lateinit var mesaagelist:ArrayList<message>
    private lateinit var mesageAdapter:MessageAdapter
    private lateinit var mDref:DatabaseReference


    var receiveroom :String?=null
    var sendereroom :String?=null
    var lastMessage :String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
         mDref = FirebaseDatabase.getInstance().getReference()

        val name = intent.getStringExtra("name")
        val receiveduid = intent.getStringExtra("uid")
        var num  = -1

        val senderUid =  FirebaseAuth.getInstance().currentUser?.uid

        sendereroom = receiveduid + senderUid
        receiveroom = senderUid + receiveduid
        supportActionBar?.title = name

        chatRecylerView = findViewById(R.id.ChatRecyclerView)
        messagebox= findViewById(R.id.messageBox)
        sentButton = findViewById(R.id.sendButton)
        mesaagelist = ArrayList()
        mesageAdapter = MessageAdapter(this, mesaagelist)
        chatRecylerView.layoutManager = LinearLayoutManager(this)
        chatRecylerView.adapter = mesageAdapter

        mDref.child("Chats").child(sendereroom!!).child("chating")
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
                TODO("Not yet implemented")
            }

        })
        sendButton.setOnClickListener {
            val messages = messagebox.text.toString().trim()
            val current = LocalDateTime.now()

            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val formatted = current.format(formatter)

            if (messagebox.text.isEmpty()) {
                messagebox.setError("message is required")
            } else {
                val messageObject = message(messages, senderUid,formatted)
                mDref.child("Chats").child(sendereroom!!).child("chating")
                    .push().setValue(messageObject).addOnSuccessListener {
                        mDref.child("Chats").child(receiveroom!!).child("chating")
                            .push().setValue(messageObject)
                    }



                messagebox.setText("")
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.deletechats,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.delete){
            AlertDialog.Builder(this)
                .setTitle("Exit Alert")
                .setMessage("Do You Want To Delete The Chats?")
                .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                    mDref.child("Chats").child(sendereroom!!).child("chating").removeValue()
                        .addOnFailureListener{
                            mDref.child("Chats").child(receiveroom!!).child("chating").removeValue()
                        }
                    Toast.makeText(this,"Chats are deleted now", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(android.R.string.cancel) { dialog, whichButton ->

                }
                .show()

            return true
        }
        return true


    }


    override fun onBackPressed() {
       Toast.makeText(this,"You exited chatting",Toast.LENGTH_SHORT).show()
        super.onBackPressed()
    }

}





