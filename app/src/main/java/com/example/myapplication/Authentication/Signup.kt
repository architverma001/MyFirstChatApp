package com.example.myapplication.Authentication

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.Activities.MainActivity
import com.example.myapplication.R
import com.example.myapplication.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {
    private lateinit var etname :EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference
    private lateinit var loader:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()
        loader = ProgressDialog(this)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etname = findViewById(R.id.etName)
        btnSignup = findViewById(R.id.btnsignup)
        supportActionBar?.hide()
        btnSignup.setOnClickListener {

            val name = etname.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            loader.setTitle("Signing up")
            loader.setMessage("Please wait")
            loader.show()

            if(email.isEmpty()){
                etEmail.setError("Required")
                Toast.makeText(this@Signup,"Kindly fill email details",Toast.LENGTH_SHORT).show()
            }
            else if(password.isEmpty()){
                etPassword.setError("Required")
                Toast.makeText(this@Signup,"Kindly fill the password",Toast.LENGTH_SHORT).show()
            }
            else if(name.isEmpty()){
                etname.setError("Required")
                Toast.makeText(this@Signup,"Kindly fill the password",Toast.LENGTH_SHORT).show()
            }

            else signup(name,email,password)

        }

        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(this@Signup, MainActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    private fun signup(name:String,email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    loader.dismiss()
                    addUserToDatabase(name,email,auth.currentUser?.uid!!)
                   val intent = Intent(this@Signup, MainActivity::class.java)
                    finish()
                    startActivity(intent)

                    val user = auth.currentUser
                    Toast.makeText(this@Signup, "Authentication Successful", Toast.LENGTH_SHORT).show()

                } else {
                    loader.dismiss()
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@Signup, task.exception.toString(), Toast.LENGTH_SHORT).show()

                }
            }

    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
   mDbRef = FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(uid).setValue(User(name,email,uid))
    }
}