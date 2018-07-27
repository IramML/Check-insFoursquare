package com.example.iram.check_ins.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.iram.check_ins.Fourscuare.Foursquare
import com.example.iram.check_ins.Fourscuare.User
import com.example.iram.check_ins.Fourscuare.Venue
import com.example.iram.check_ins.Interfaces.UsersInterface
import com.example.iram.check_ins.R
import com.google.gson.Gson

class VenueDetails : AppCompatActivity() {

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

        val currentVenueString=intent.getStringExtra(Main.CURRENT_VENUE)
        val gson=Gson()
        val currentVenue=gson.fromJson(currentVenueString, Venue::class.java)


        tvName.text=currentVenue.name
        tvState.text=currentVenue.location?.state
        tvCountry.text=currentVenue.location?.country
        tvCategory.text=currentVenue.categories?.get(0)?.name
        tvCheckins.text=currentVenue.stats?.checkinsCount.toString()
        tvUsers.text=currentVenue.stats?.usersCount.toString()
        tvTips.text=currentVenue.stats?.tipCount.toString()

        val foursquare=Foursquare(this, VenueDetails())
        if(foursquare.tokenAvailable()){
            //foursquare.newCheckin(currentVenue.id, currentVenue.location!!, "Hola%20mundo")
            foursquare.getCurrentUser(object:UsersInterface{
                override fun getCurrentUser(user: User) {
                    Toast.makeText(applicationContext, user.firstName, Toast.LENGTH_SHORT).show()
                }

            })
        }
    }
}
