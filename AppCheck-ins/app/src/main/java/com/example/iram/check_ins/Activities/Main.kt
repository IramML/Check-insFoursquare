package com.example.iram.check_ins.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.example.iram.check_ins.Fourscuare.Foursquare
import com.example.iram.check_ins.Fourscuare.Venue
import com.example.iram.check_ins.Interfaces.getVenuesInterface
import com.example.iram.check_ins.Interfaces.locationListener
import com.example.iram.check_ins.R
import com.example.iram.check_ins.Util.Location
import com.google.android.gms.location.LocationResult
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.iram.check_ins.RecyclerViewMain.ClickListener
import com.example.iram.check_ins.RecyclerViewMain.LongClickListener
import com.example.iram.check_ins.RecyclerViewMain.customAdapter
import com.google.gson.Gson


class Main : AppCompatActivity() {
    var location: Location?=null
    var foursquare:Foursquare?=null

    var adapterCustom: customAdapter? = null
    var list:RecyclerView? = null
    var layoutManager:RecyclerView.LayoutManager? = null

    companion object {
        val CURRENT_VENUE="checkins.Main"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list=findViewById(R.id.rvVenues)
        list?.setHasFixedSize(true)
        layoutManager=LinearLayoutManager(this)
        list?.layoutManager=layoutManager

        foursquare=Foursquare(this, this)
        if(foursquare?.tokenAvailable()!!){
            location= com.example.iram.check_ins.Util.Location(this, object:locationListener{
                override fun locationResponse(locationResult: LocationResult) {
                    val lat=locationResult.lastLocation.latitude.toString()
                    val lng=locationResult.lastLocation.longitude.toString()

                    foursquare?.getVenues(lat, lng, object:getVenuesInterface{
                        override fun venuesGenerated(venues: ArrayList<Venue>) {
                            implementRecyclerView(venues)
                            for(venue in venues){
                                Log.d("VENUE", venue.name)
                            }
                        }
                    })
                }
            })

        }

    }
    private fun implementRecyclerView(venuesList:ArrayList<Venue>){
        adapterCustom = customAdapter(venuesList, object : ClickListener{
            override fun onClick(view: View, index: Int) {
                val venueToJson=Gson()
                val currentVenueString=venueToJson.toJson(venuesList.get(index))
                val intent=Intent(applicationContext, VenueDetails::class.java)
                intent.putExtra(CURRENT_VENUE, currentVenueString)
                startActivity(intent)
            }

        }, object : LongClickListener{
            override fun longClick(view: View, index: Int) {
                /*if (!isActionMode) {
                    startSupportActionMode(callback)
                    isActionMode = true
                    toysAdapter.selectItem(index)
                } else {
                    toysAdapter.selectItem(index)
                }
                actionMode.setTitle(toysAdapter.getItemsSelected() + " selected")*/
            }
        })
        list?.adapter=adapterCustom
    }
    override fun onStart() {
        super.onStart()
        location?.inicializeLocation()
    }

    override fun onPause() {
        super.onPause()
        location?.stopUpdateLocation()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        location?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
