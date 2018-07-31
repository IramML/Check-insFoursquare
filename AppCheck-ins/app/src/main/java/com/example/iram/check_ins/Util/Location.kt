package com.example.iram.check_ins.Util

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.example.iram.check_ins.Interfaces.locationListener
import com.example.iram.check_ins.Messages.Errors
import com.example.iram.check_ins.Messages.Message
import com.example.iram.check_ins.Messages.Messages
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

class Location(var activity: AppCompatActivity, locationListener: locationListener){
    private val permissionFineLocation=android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permissionCoarseLocation=android.Manifest.permission.ACCESS_COARSE_LOCATION

    private val REQUEST_CODE_LOCATION=100

    private var fusedLocationClient:FusedLocationProviderClient?=null

    private var locationRequest:LocationRequest?=null
    private var callbabck:LocationCallback?=null
    init {
        fusedLocationClient= FusedLocationProviderClient(activity.applicationContext)

        inicializeLocationRequest()
        callbabck=object: LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)

                locationListener.locationResponse(p0!!)
            }
        }
    }

    private fun inicializeLocationRequest() {
        locationRequest= LocationRequest()
        locationRequest?.interval=50000
        locationRequest?.fastestInterval=5000
        locationRequest?.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private fun validatePermissionsLocation():Boolean{
        val fineLocationAvailable=ActivityCompat.checkSelfPermission(activity.applicationContext, permissionFineLocation)==PackageManager.PERMISSION_GRANTED
        val coarseLocationAvailable=ActivityCompat.checkSelfPermission(activity.applicationContext, permissionFineLocation)==PackageManager.PERMISSION_GRANTED

        return fineLocationAvailable && coarseLocationAvailable
    }
    private fun requestPermissions(){
        val contextProvider=ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionFineLocation)

        if(contextProvider){
            Message.message(activity.applicationContext, Messages.RATIONALE)
        }
        permissionRequest()
    }
    private fun permissionRequest(){
        ActivityCompat.requestPermissions(activity, arrayOf(permissionFineLocation, permissionCoarseLocation), REQUEST_CODE_LOCATION)
    }
    fun onRequestPermissionsResult(requestCode:Int, permissions:Array<out String>, grantResults:IntArray){
        when(requestCode){
            REQUEST_CODE_LOCATION->{
                if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    getLocation()
                }else{
                    Message.messageError(activity.applicationContext, Errors.PERMISSION_DENIED)
                }
            }
        }
    }
    fun stopUpdateLocation(){
        this.fusedLocationClient?.removeLocationUpdates(callbabck)
    }
    fun inicializeLocation(){
        if (validatePermissionsLocation()){
            getLocation()
        }else{
            requestPermissions()
        }
    }
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        validatePermissionsLocation()

        fusedLocationClient?.requestLocationUpdates(locationRequest, callbabck, null)
    }
}