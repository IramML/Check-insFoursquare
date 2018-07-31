package com.example.iram.check_ins.Activities

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.widget.*
import com.example.iram.check_ins.Fourscuare.Foursquare
import com.example.iram.check_ins.Fourscuare.Venue
import com.example.iram.check_ins.Fourscuare.objectGrid
import com.example.iram.check_ins.GridViewDetailsVenues.adapterGridView
import com.example.iram.check_ins.R
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.net.URLEncoder
import java.text.NumberFormat
import java.util.*

class VenueDetails : AppCompatActivity() {
    var toolbar: Toolbar?=null
    var btnCheckin:Button?=null
    var btnLike:Button?=null
    var ivPhoto:ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_details)
        ivPhoto=findViewById(R.id.ivPhoto)
        val tvName=findViewById<TextView>(R.id.tvName)
        val tvState=findViewById<TextView>(R.id.tvState)
        val tvCountry=findViewById<TextView>(R.id.tvCountry)
        btnCheckin=findViewById(R.id.btnCkeckin)
        btnLike=findViewById(R.id.btnLike)
        val grid=findViewById<GridView>(R.id.grid)

        val currentVenueString=intent.getStringExtra(Main.CURRENT_VENUE)
        val gson=Gson()
        val currentVenue=gson.fromJson(currentVenueString, Venue::class.java)
        initToolbar(currentVenue.name)
        val listGrid=ArrayList<objectGrid>()
        if(currentVenue.imagePreview.isNullOrEmpty())ivPhoto?.setImageResource(R.drawable.ic_launcher_background)
        else Picasso.get().load(currentVenue.imagePreview).placeholder(R.drawable.ic_launcher_background).into(ivPhoto)
        tvName.text=currentVenue.name
        tvState.text=currentVenue.location?.state
        tvCountry.text=currentVenue.location?.country

        listGrid.add(objectGrid("${currentVenue.categories?.get(0)?.name!!}", R.drawable.ic_grid_categories, ContextCompat.getColor(this, R.color.secondaryColor)))
        listGrid.add(objectGrid("Checkins: ${NumberFormat.getNumberInstance(Locale.US).format(currentVenue.stats?.checkinsCount)}", R.drawable.ic_grid_checkins, ContextCompat.getColor(this, R.color.primaryLightColor)))
        listGrid.add(objectGrid("Users: ${NumberFormat.getNumberInstance(Locale.US).format(currentVenue.stats?.usersCount)}", R.drawable.ic_grid_users, ContextCompat.getColor(this, R.color.primaryColor)))
        listGrid.add(objectGrid("Tips: ${NumberFormat.getNumberInstance(Locale.US).format(currentVenue.stats?.tipCount)}", R.drawable.ic_grid_tips, ContextCompat.getColor(this, R.color.secondaryLightColor)))
        val adapter=adapterGridView(this, listGrid)
        grid.adapter=adapter
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
