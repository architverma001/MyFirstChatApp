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
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth

class UserAdapter(val context: Context,val userList: ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder> (){
  private lateinit var updateName:TextView


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
     val view:View = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return  UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textname.text = currentUser.name
        holder.itemView.setOnLongClickListener{
            Toast.makeText(context, "Long click detected", Toast.LENGTH_SHORT).show()

            val builder = AlertDialog.Builder(context)
            val button:Button
            val view:View
            val inflater:LayoutInflater
            inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.activity_update_name,null)

            val alert :AlertDialog
             alert = builder.create()
            updateName = holder.itemView.findViewById(R.id.txt_name)
           val names:EditText
           names = view.findViewById(R.id.editTextTextPersonName)

            val name = names.text

            button = view.findViewById(R.id.uPbutton)

            button.setOnClickListener {
                if(name!=null){
                    updateName.setText(name)
                }
            }
            alert.show()

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
    }
}