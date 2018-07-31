package com.example.iram.check_ins.Activities

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.iram.check_ins.Fourscuare.Foursquare
import com.example.iram.check_ins.Fourscuare.User
import com.example.iram.check_ins.Fourscuare.Venue
import com.example.iram.check_ins.Interfaces.UsersInterface
import com.example.iram.check_ins.R
import com.google.gson.Gson
import java.net.URLEncoder

class VenueDetails : AppCompatActivity() {
    var toolbar: Toolbar?=null
    var btnCheckin:Button?=null
    var btnLike:Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_details)
        val tvName=findViewById<TextView>(R.id.tvName)
        val tvState=findViewById<TextView>(R.id.tvState)
        val tvCountry=findViewById<TextView>(R.id.tvCountry)
        val tvCategory=findViewById<TextView>(R.id.tvCategory)
        val tvCheckins=findViewById<TextView>(R.id.tvCheckins)
        val tvUsers=findViewById<TextView>(R.id.tvUsers)
        val tvTips=findViewById<TextView>(R.id.tvTips)
        btnCheckin=findViewById(R.id.btnCkeckin)
        btnLike=findViewById(R.id.btnLike)
        val currentVenueString=intent.getStringExtra(Main.CURRENT_VENUE)
        val gson=Gson()
        val currentVenue=gson.fromJson(currentVenueString, Venue::class.java)
        initToolbar(currentVenue.name)

        tvName.text=currentVenue.name
        tvState.text=currentVenue.location?.state
        tvCountry.text=currentVenue.location?.country
        tvCategory.text=currentVenue.categories?.get(0)?.name
        tvCheckins.text=currentVenue.stats?.checkinsCount.toString()
        tvUsers.text=currentVenue.stats?.usersCount.toString()
        tvTips.text=currentVenue.stats?.tipCount.toString()

        val foursquare=Foursquare(this, VenueDetails())

        btnCheckin?.setOnClickListener {
            if(foursquare.tokenAvailable()){
                val etMessage=EditText(this)
                etMessage.hint="message"

                var alertDialog= AlertDialog.Builder(this, R.style.CustomCastTheme).setTitle("New check-in")
                        .setMessage("Write a message")
                        .setView(etMessage)
                        .setPositiveButton("Check-in", DialogInterface.OnClickListener{
                            dialogInterface, i ->

                            val message=URLEncoder.encode(etMessage.text.toString(), "UTF-8")
                            foursquare.newCheckin(currentVenue.id, currentVenue.location!!, message)

                        }).setNegativeButton("Cancel", DialogInterface.OnClickListener({
                            dialogInterface, i ->

                        }))

                alertDialog.show()
            }else foursquare?.sendLogin()
        }
        btnLike?.setOnClickListener {
            if (foursquare.tokenAvailable()) foursquare.newLike(currentVenue.id)
        }
    }
    fun initToolbar(currentVenueName:String){
        toolbar=findViewById(R.id.toolbar)
        toolbar?.setTitle(currentVenueName)
        setSupportActionBar(toolbar)
        var actionBar=supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar?.setNavigationOnClickListener {
            finish()
        }
    }
}
