package com.example.myapplication.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Activities.ChatActivity
import com.example.myapplication.R
import com.example.myapplication.models.User
import com.example.myapplication.models.message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class UserAdapter(val context: Context,val userList: ArrayList<User>): RecyclerView.Adapter<RecyclerView.ViewHolder> (){
  private lateinit var updateName:TextView
    var clickId :String?=null
    var receiveroom :String?=null

    val senderUid =  FirebaseAuth.getInstance().currentUser?.uid
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
     val view:View = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return  UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as UserViewHolder
        val currentUser = userList[position]
        holder.textname.text = currentUser.name
        FirebaseDatabase.getInstance().getReference().child("Chats")
            .child(FirebaseAuth.getInstance().uid+currentUser.uid)
            .child("chating")
           .limitToLast(1)
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.hasChildren()){
                        for(postSnapshot in snapshot.children){
                            val message = postSnapshot.getValue(message::class.java)
                            holder.lastmessage.text = message?.message.toString()

                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                   Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show()
                }

            })
        Picasso.get().load(currentUser.profile).placeholder(R.drawable.profilepic).into(holder.image)

        holder.itemView.setOnLongClickListener{
           Toast.makeText(context,"Long click detected",Toast.LENGTH_SHORT).show()
            return@setOnLongClickListener true
        }





        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)

            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
     return userList.size
    }

    class UserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
     val textname = itemView.findViewById<TextView>(R.id.txt_name)
     val lastmessage = itemView.findViewById<TextView>(R.id.lastMessage)
     val image = itemView.findViewById<ImageView>(R.id.myProfilePic)
    }
}