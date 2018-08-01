package com.example.iram.check_ins.GoogleMaps

import com.google.android.gms.maps.model.LatLng

class GoogleMapsAPIRequest {
    var routes:ArrayList<Routes>?=null
}
class Routes{
    var legs:ArrayList<Legs>?=null
}
class Legs{
    var steps:ArrayList<Steps>?=null
}
class Steps{
    var start_location:LatLon?=null
    var end_location:LatLon?=null
}
class LatLon{
    var lat:Double=0.0
    var lng:Double=0.0
    fun toLatLng(): LatLng {
        return LatLng(lat, lng)
    }
}