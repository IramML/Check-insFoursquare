package com.example.iram.check_ins.Activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.Toast
import com.example.iram.check_ins.GoogleMaps.GoogleMapsAPI
import com.example.iram.check_ins.GoogleMaps.GoogleMapsAPIRequest
import com.example.iram.check_ins.Interfaces.getRouteInterface
import com.example.iram.check_ins.Interfaces.locationListener
import com.example.iram.check_ins.R
import com.example.iram.check_ins.Util.Location
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var location: Location?=null
    var latVenue:Double?=null
    var lngVenue:Double?=null
    var currentLat:Double?=null
    var currentLng:Double?=null
    var name=""

    var toolbar:Toolbar?=null
    var googleMapsAPI:GoogleMapsAPI?=null

    private var currentRoute: Polyline?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        initToolbar()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        latVenue=intent.getDoubleExtra("LAT", -34.0)
        lngVenue=intent.getDoubleExtra("LNG", 151.0)
        name=intent.getStringExtra("NAME")
        googleMapsAPI=GoogleMapsAPI(this)
        Toast.makeText(applicationContext, "Tracing route...", Toast.LENGTH_SHORT).show()
        location= Location(this, object:locationListener{
            @SuppressLint("MissingPermission")
            override fun locationResponse(locationResult: LocationResult) {
                currentLat=locationResult.lastLocation.latitude
                currentLng=locationResult.lastLocation.longitude
                if (mMap!=null){
                    mMap.isMyLocationEnabled=true
                    mMap.uiSettings.isMyLocationButtonEnabled=true
                }
                val currentLocation:LatLng=LatLng(currentLat!!, currentLng!!)

                val currentMarker = mMap.addMarker(MarkerOptions()
                        .position(currentLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location)))


                val venueLocation:LatLng= LatLng(latVenue!!, lngVenue!!)
                googleMapsAPI?.getRoute(currentLocation, venueLocation, object:getRouteInterface{
                    override fun getRoute(json: String) {
                        Log.d("JSON_ROUTE", json)
                        val coordinates:PolylineOptions=getCoordinates(json)
                        if (currentRoute!=null){
                            currentRoute?.remove()
                        }
                        if(coordinates!=null)
                        currentRoute=mMap.addPolyline(coordinates)
                    }

                })
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val venue = LatLng(latVenue!!, lngVenue!!)
        mMap.addMarker(MarkerOptions().position(venue).title(name))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(venue))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f))
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
    fun initToolbar(){
        toolbar=findViewById(R.id.toolbar)
        toolbar?.setTitle(R.string.app_map)
        setSupportActionBar(toolbar)
        var actionBar=supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar?.setNavigationOnClickListener {
            finish()
        }
    }
    private fun getCoordinates(json:String):PolylineOptions{
        val gson= Gson()
        val objectGson=gson.fromJson(json, GoogleMapsAPIRequest::class.java)

        Log.d("JSON_RESPONSE", json)
        val points=objectGson.routes?.get(0)!!.legs?.get(0)!!.steps!!
        var coordinates=PolylineOptions()
        for (point in points){
            coordinates.add(point.start_location?.toLatLng())
            coordinates.add(point.end_location?.toLatLng())
        }
        coordinates.color(Color.CYAN).width(15f)
        location?.stopUpdateLocation()
        return coordinates
    }
}
