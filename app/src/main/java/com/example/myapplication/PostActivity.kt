package com.example.myapplication

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.user_layout.*
import java.text.SimpleDateFormat
import java.util.*

class PostActivity : AppCompatActivity() {
    private lateinit var mAuth:FirebaseAuth
    private lateinit var database:FirebaseDatabase
    private lateinit var mref :DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.postingactivity,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.choose_img){

           Intent(Intent.ACTION_GET_CONTENT).also {
               it.type = "image/*"
               startActivityForResult(it,0)
           }
            return true
        }
        if(item.itemId==R.id.upload){
          val progressDialog = ProgressDialog(this@PostActivity)
            progressDialog.setMessage("uploading image")
            progressDialog.setTitle("Posting")
            progressDialog.show()

            val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
            val now = Date()
            val filename = formatter.format(now)

            return true
        }
        return true


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && resultCode==Activity.RESULT_OK){
            val uri = data?.data
            postImage.setImageURI(uri)
           // myProfilePic.setImageURI(uri)

        }
    }
}






