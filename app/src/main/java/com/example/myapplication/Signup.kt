package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*
import nl.dionsegijn.konfetti.xml.KonfettiView

class Signup : AppCompatActivity() {
    private lateinit var etname :EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etname = findViewById(R.id.etName)
        btnSignup = findViewById(R.id.btnsignup)
        supportActionBar?.hide()
        btnSignup.setOnClickListener {

            val name = etname.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()


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
            val intent = Intent(this@Signup,MainActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    private fun signup(name:String,email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    addUserToDatabase(name,email,auth.currentUser?.uid!!)
                   val intent = Intent(this@Signup,MainActivity::class.java)
                    finish()
                    startActivity(intent)

                    val user = auth.currentUser
                    Toast.makeText(this@Signup, "Authentication Successful", Toast.LENGTH_SHORT).show()

                } else {
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