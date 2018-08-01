package com.example.iram.check_ins.GoogleMaps

import android.support.v7.app.AppCompatActivity
import com.example.iram.check_ins.Interfaces.HttpResponse
import com.example.iram.check_ins.Interfaces.getRouteInterface
import com.example.iram.check_ins.Util.Network
import com.google.android.gms.maps.model.LatLng

class GoogleMapsAPI(var activity:AppCompatActivity){
    fun getRoute(currentLocation:LatLng, venueLocation:LatLng,  getRouteInterface: getRouteInterface){
        val network= Network(activity)
        val coordinates= LatLng( venueLocation.latitude, venueLocation.longitude)
        val origin="origin="+currentLocation.latitude+","+currentLocation.longitude+"&"
        val destination="destination="+coordinates.latitude+","+coordinates.longitude+"&"
        val parameters=origin+destination+"sensor=false&mode=driving"
        val url="http://maps.googleapis.com/maps/api/directions/json?$parameters"
        network.httpRequest(activity.applicationContext, url, object:HttpResponse{
            override fun httpResponseSuccess(response: String) {
                getRouteInterface.getRoute(response)
            }
        })
    }
}