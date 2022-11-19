package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class login : AppCompatActivity() {
    private lateinit var etEmail:EditText
    private lateinit var etPassword:EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignup:Button
    private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignup = findViewById(R.id.btnsignup)
        supportActionBar?.hide()
        btnSignup.setOnClickListener {
            val intent = Intent(this,Signup::class.java)
            startActivity(intent);
            Toast.makeText(this,"Signup page",Toast.LENGTH_SHORT).show()
        }
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString().trim()
            if(email.isEmpty()){
                etEmail.setError("Required")
                Toast.makeText(this@login,"Kindly fill email details",Toast.LENGTH_SHORT).show()
            }
            else if(pass.isEmpty()){
                etPassword.setError("Required")
                Toast.makeText(this@login,"Kindly fill the password",Toast.LENGTH_SHORT).show()
            }

           else Login(email,pass)
        }

        val currentUser = mAuth.currentUser
        if(currentUser != null){
            val intent = Intent(this@login,MainActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    private fun Login(email: String, pass: String) {
        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this@login,MainActivity::class.java)
                    finish()
                    startActivity(intent)

                    val user = mAuth.currentUser
                    Toast.makeText(this@login, "Login Successful", Toast.LENGTH_SHORT).show()

                } else {

                    Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_SHORT).show()

                }
            }
    }
}