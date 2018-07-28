package com.example.iram.check_ins.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.TextView
import android.widget.Toast
import com.example.iram.check_ins.Fourscuare.Foursquare
import com.example.iram.check_ins.Fourscuare.User
import com.example.iram.check_ins.Interfaces.UsersInterface
import com.example.iram.check_ins.R

class Profile : AppCompatActivity() {
    var foursquare: Foursquare?=null

    var toolbar: Toolbar?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val tvName=findViewById<TextView>(R.id.tvName)
        val tvFriends=findViewById<TextView>(R.id.tvFriends)
        val tvTips=findViewById<TextView>(R.id.tvTips)
        val tvPhototos=findViewById<TextView>(R.id.tvPhotos)
        val tvCheckins=findViewById<TextView>(R.id.tvCheckins)
        foursquare= Foursquare(this, this)
        if(foursquare?.tokenAvailable()!!){
            foursquare?.getCurrentUser(object: UsersInterface {
                override fun getCurrentUser(user: User) {
                    initToolbar("${user.firstName} ${user.lastName}")
                    tvName.text=user.firstName
                    tvFriends.text="${user.friend?.count.toString()} Friends"
                    tvTips.text="${user.tips?.count.toString()} Tips"
                    tvPhototos.text="${user.photos?.count.toString()} Photos"
                    tvCheckins.text="${user.checkins?.count.toString()} Checkins"
                }

            })
        }else foursquare?.sendLogin()

    }
    fun initToolbar(nameProfile:String){
        toolbar=findViewById(R.id.toolbar)
        toolbar?.setTitle(nameProfile)
        setSupportActionBar(toolbar)
        var actionBar=supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar?.setNavigationOnClickListener {
            finish()
        }
    }
}
