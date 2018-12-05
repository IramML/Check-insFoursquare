package com.example.iram.check_ins.GoogleMaps

import com.google.android.gms.maps.model.LatLng

data class GoogleMapsAPIRequest(val routes:ArrayList<Routes>)
data class Routes(val legs:ArrayList<Legs>)
data class Legs(val steps:ArrayList<Steps>)
data class Steps(val start_location:LatLon, val end_location:LatLon)
data class LatLon(val lat:Double, val lng:Double){
    fun toLatLng(): LatLng {
        return LatLng(lat, lng)
    }
}