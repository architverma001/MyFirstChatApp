package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private lateinit var mDref:DatabaseReference
class MessageAdapter(val context: Context,val messageList:ArrayList<message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      val sendmessage  = itemView.findViewById<TextView>(R.id.text_send_message)
      val sendmessageTime  = itemView.findViewById<TextView>(R.id.text_send_message_time)
        val SendimageView = itemView.findViewById<ImageView>(R.id.myProfilePic)
    }

    class RecieveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receivemessage  = itemView.findViewById<TextView>(R.id.text_receive_message)
        val receivemessageTime  = itemView.findViewById<TextView>(R.id.text_receive_message_time)
        val receiveimageView = itemView.findViewById<ImageView>(R.id.myProfilePic)
    }
  val ITEM_RECEIVE = 1
  val ITEM_SENT = 2
    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return  ITEM_SENT
        }
        else{
            return   ITEM_RECEIVE
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       if(viewType==1){
          val view = LayoutInflater.from(context).inflate(R.layout.received_message,parent,false)
           return RecieveViewHolder(view)
       }
        else{
           val view = LayoutInflater.from(context).inflate(R.layout.send,parent,false)
           return SendViewHolder(view)
       }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentmessage = messageList[position]
        if(holder.javaClass==SendViewHolder::class.java){
          val viewHolder = holder as SendViewHolder
            holder.sendmessage.text = currentmessage.message
            holder.sendmessageTime.text = currentmessage.time

            holder.itemView.setOnClickListener {
                Toast.makeText(context,"item clicked",Toast.LENGTH_SHORT).show()
            }

        }
        else{
            val viewHolder = holder as RecieveViewHolder
            holder.receivemessage.text = currentmessage.message
            holder.receivemessageTime.text = currentmessage.time
            holder.itemView.setOnClickListener {
                Toast.makeText(context,"item clicked",Toast.LENGTH_SHORT).show()

            }
        }
    }



    override fun getItemCount(): Int {
       return messageList.size
    }
}