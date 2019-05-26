package com.example.iram.check_ins.Activities


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.widget.*
import com.example.iram.check_ins.Fourscuare.Foursquare
import com.example.iram.check_ins.Fourscuare.Venue
import com.example.iram.check_ins.Fourscuare.objectGrid
import com.example.iram.check_ins.Fragments.BottomSheetCheckinFragment
import com.example.iram.check_ins.GridViewDetailsVenues.adapterGridView
import com.example.iram.check_ins.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import mehdi.sakout.fancybuttons.FancyButton
import java.text.NumberFormat
import java.util.*

class VenueDetails : AppCompatActivity(), OnMapReadyCallback {
    var toolbar: Toolbar?=null
    var btnCheckin: FancyButton?=null
    var btnLike:FancyButton?=null
    var ivPhoto:ImageView?=null
    var btnHTG:ImageButton?=null
    private lateinit var mMap: GoogleMap
    var lng:Double=0.0
    var lat:Double=0.0
    var currentVenue:Venue?=null

    var bottomSheetRiderFragment: BottomSheetCheckinFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_details)
        ivPhoto=findViewById(R.id.ivPhoto)
        val tvName=findViewById<TextView>(R.id.tvName)
        val tvState=findViewById<TextView>(R.id.tvState)
        val tvCountry=findViewById<TextView>(R.id.tvCountry)
        btnCheckin=findViewById(R.id.btnCheckin)
        btnLike=findViewById(R.id.btnLike)
        btnHTG=findViewById(R.id.btnHTG)
        val grid=findViewById<GridView>(R.id.grid)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        bottomSheetRiderFragment= BottomSheetCheckinFragment().newInstance("Checkin bottom sheet")
        val currentVenueString=intent.getStringExtra(Main.CURRENT_VENUE)
        val gson=Gson()
        currentVenue=gson.fromJson(currentVenueString, Venue::class.java)
        initToolbar(currentVenue?.name!!)
        val listGrid=ArrayList<objectGrid>()
        if(!currentVenue?.imagePreview.isNullOrEmpty())
            Picasso.get().load(currentVenue?.imagePreview).placeholder(R.drawable.placeholder).into(ivPhoto)
        tvName.text=currentVenue?.name
        tvState.text=currentVenue?.location?.state
        tvCountry.text=currentVenue?.location?.country

        listGrid.add(objectGrid("${currentVenue?.categories?.get(0)?.name!!}", R.drawable.ic_grid_categories, ContextCompat.getColor(this, R.color.grid1Color)))
        listGrid.add(objectGrid("${resources.getString(R.string.checkins_grid)}: ${NumberFormat.getNumberInstance(Locale.US).format(currentVenue?.stats?.checkinsCount)}", R.drawable.ic_grid_checkins, ContextCompat.getColor(this, R.color.grid2Color)))
        listGrid.add(objectGrid("${resources.getString(R.string.users_grid)}: ${NumberFormat.getNumberInstance(Locale.US).format(currentVenue?.stats?.usersCount)}", R.drawable.ic_grid_users, ContextCompat.getColor(this, R.color.grid3Color)))
        listGrid.add(objectGrid("${resources.getString(R.string.tips_grid)}: ${NumberFormat.getNumberInstance(Locale.US).format(currentVenue?.stats?.tipCount)}", R.drawable.ic_grid_tips, ContextCompat.getColor(this, R.color.grid4Color)))
        val adapter=adapterGridView(this, listGrid)
        grid.adapter=adapter
        val foursquare=Foursquare(this, VenueDetails())
        lat=currentVenue?.location?.lat!!
        lng=currentVenue?.location?.lng!!

        btnHTG?.setOnClickListener {
            //How to get activity
            var intent= Intent(this, MapsActivity::class.java)
            intent.putExtra("LAT", lat)
            intent.putExtra("LNG", lng)
            intent.putExtra("NAME", currentVenue?.name)
            startActivity(intent)
        }
        btnCheckin?.setOnClickListener {
            if(foursquare.tokenAvailable()){
                bottomSheetRiderFragment?.show(getSupportFragmentManager(), bottomSheetRiderFragment?.getTag())
                bottomSheetRiderFragment?.getData(foursquare, currentVenue!!)

            }else foursquare?.sendLogin()
        }
        btnLike?.setOnClickListener {
            if (foursquare.tokenAvailable()) foursquare.newLike(currentVenue?.id!!)
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //venue marker
        val venue = LatLng(lat, lng)
        mMap.addMarker(MarkerOptions().position(venue).title(currentVenue?.name))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(venue))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f))

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
