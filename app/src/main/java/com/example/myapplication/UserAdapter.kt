package com.example.myapplication

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myapplication.models.myUsers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post.view.*

class UserAdapter(val context: Context,val userList: ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder> (){
  private lateinit var updateName:TextView

    var receiveroom :String?=null
    var sendereroom :String?=null
    //val senderUid =  FirebaseAuth.getInstance().currentUser?.uid
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
     val view:View = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return  UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
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
            Toast.makeText(context, "Long click detected", Toast.LENGTH_SHORT).show()
            return@setOnLongClickListener true
        }



        holder.itemView.setOnClickListener {
            val intent = Intent(context,ChatActivity::class.java)
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