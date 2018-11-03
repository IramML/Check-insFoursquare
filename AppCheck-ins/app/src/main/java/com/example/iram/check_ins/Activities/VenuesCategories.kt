package com.example.iram.check_ins.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import com.example.iram.check_ins.Fourscuare.Category
import com.example.iram.check_ins.Fourscuare.Foursquare
import com.example.iram.check_ins.Fourscuare.Venue
import com.example.iram.check_ins.Fragments.CategoriesFragment
import com.example.iram.check_ins.Interfaces.getVenuesInterface
import com.example.iram.check_ins.Interfaces.locationListener
import com.example.iram.check_ins.R
import com.example.iram.check_ins.RecyclerViewMain.ClickListener
import com.example.iram.check_ins.RecyclerViewMain.LongClickListener
import com.example.iram.check_ins.RecyclerViewMain.customAdapter
import com.example.iram.check_ins.Util.Location
import com.google.android.gms.location.LocationResult
import com.google.gson.Gson

class enuesCategories : AppCompatActivity() {
    var location: Location?=null
    var foursquare: Foursquare?=null

    var adapterCustom: customAdapter? = null
    var list:RecyclerView? = null
    var layoutManager:RecyclerView.LayoutManager? = null

    var toolbar:Toolbar?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venues_categories)

        initRecyclerView()

        val currentCategoryString=intent.getStringExtra(CategoriesFragment.CURRENT_CATEGORY)
        val gson=Gson()
        val currentCategory=gson.fromJson(currentCategoryString, Category::class.java)
        initToolbar(currentCategory.name)
        foursquare=Foursquare(this, this)
        if(foursquare?.tokenAvailable()!!){
            location= com.example.iram.check_ins.Util.Location(this, object: locationListener {
                override fun locationResponse(locationResult: LocationResult) {
                    val lat=locationResult.lastLocation.latitude.toString()
                    val lng=locationResult.lastLocation.longitude.toString()
                    val categoryId=currentCategory.id

                   foursquare?.getVenues(lat, lng, categoryId, object: getVenuesInterface {
                        override fun venuesGenerated(venues: ArrayList<Venue>) {
                            implementRecyclerView(venues)
                            location?.stopUpdateLocation()
                        }
                    })
                }
            })
        }else foursquare?.sendLogin()
    }
    private fun initRecyclerView(){
        list=findViewById(R.id.rvVenuesCategories)
        list?.setHasFixedSize(true)
        layoutManager= LinearLayoutManager(this)
        list?.layoutManager=layoutManager
    }
    private fun implementRecyclerView(venuesList:ArrayList<Venue>){
        adapterCustom = customAdapter(venuesList, object : ClickListener {
            override fun onClick(view: View, index: Int) {
                val venueToJson= Gson()
                val currentVenueString=venueToJson.toJson(venuesList.get(index))
                val intent= Intent(applicationContext, VenueDetails::class.java)
                intent.putExtra(Main.CURRENT_VENUE, currentVenueString)
                startActivity(intent)
            }

        }, object : LongClickListener {
            override fun longClick(view: View, index: Int) {}
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
    fun initToolbar(category:String){
        toolbar=findViewById(R.id.toolbar)
        toolbar?.setTitle(category)
        setSupportActionBar(toolbar)
        var actionBar=supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar?.setNavigationOnClickListener {
            finish()
        }
    }

}
