package com.example.myapplication.Dao

import com.example.myapplication.models.myUsers
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    val db = FirebaseFirestore.getInstance()
    val userCollection = db.collection("users")

    fun addUser(user:myUsers?){
        user?.let{
            GlobalScope.launch {
                userCollection.document(user.Nuid).set(it)
            }
        }
    }

}