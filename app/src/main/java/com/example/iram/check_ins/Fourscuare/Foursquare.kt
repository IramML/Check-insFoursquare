package com.example.iram.check_ins.Fourscuare

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.example.iram.check_ins.Interfaces.HttpResponse
import com.example.iram.check_ins.Interfaces.getVenuesInterface
import com.example.iram.check_ins.Messages.Errors
import com.example.iram.check_ins.Messages.Message
import com.example.iram.check_ins.Util.Network
import com.foursquare.android.nativeoauth.FoursquareOAuth
import com.google.gson.Gson

class Foursquare(var activity:AppCompatActivity, var destinyActivity:AppCompatActivity) {
    private val CODE_CONNECTION=200
    private val CODE_TOKEN_EXCHANGE=201

    private val CLIENT_ID="GASUFJU4LJJEPKBI5WRAOO42RO01G3BM3QCJI4H1T13K0S1G"
    private val CLIENT_SECRET="FJ1PWWDWASRGEYKNJ3LM1XOLSILDXCOY3YCVNRSBHZ0ZZHKH"

    private val SETTINGS="settings"
    private val ACCESS_TOKEN="accessToken"

    private val URL_BASE="https//api.foursquare.com/v2/"
    private val VERSION="20180117"

    fun logIn(){
        val intent=FoursquareOAuth.getConnectIntent(activity.applicationContext, CLIENT_ID)
        if(FoursquareOAuth.isPlayStoreIntent(intent)){
            Message.messageError(activity.applicationContext, Errors.NO_APP_FOURSQUARE_AVAILABLE)
            activity.startActivity(intent)
        }else{
            activity.startActivityForResult(intent, CODE_CONNECTION)
        }
    }
    fun validateActivityResult(requestCode:Int, resultCode:Int, data: Intent?){
        when(requestCode) {
            CODE_CONNECTION -> {
                connectionComplete(resultCode, data)
            }
            CODE_TOKEN_EXCHANGE -> {
                exchangeTokenComplete(resultCode, data)
            }
        }
    }
    private fun connectionComplete(resultCode: Int, data: Intent?){
        val requestCode=FoursquareOAuth.getAuthCodeFromResult(resultCode, data)
        val exception=requestCode.exception

        if (exception==null){
            val code=requestCode.code
            makeExchangeToken(code)
        }else{
            Message.messageError(activity.applicationContext, Errors.ERROR_CONNECTION_FSQR)
        }
    }
    private fun makeExchangeToken(code:String){
        val intent=FoursquareOAuth.getTokenExchangeIntent(activity.applicationContext, CLIENT_ID, CLIENT_SECRET, code)
        activity.startActivityForResult(intent, CODE_TOKEN_EXCHANGE)
    }
    private fun exchangeTokenComplete(resultCode: Int, data: Intent?){
        val responseToken=FoursquareOAuth.getTokenFromResult(resultCode, data)
        val exception=responseToken.exception
        if (exception==null){
            val accessToken=responseToken.accessToken
            if(!saveToken(accessToken)){
                Message.messageError(activity.applicationContext, Errors.ERROR_SAVE_TOKEN)
                goToNextActivity(destinyActivity)
            }


        }else{
            Message.messageError(activity.applicationContext, Errors.ERROR_EXCHANGE_TOKEN)
        }
    }
    private fun tokenAvailable():Boolean{
        return getToken()!=""
    }
    fun getToken():String{
        val settings=activity.getSharedPreferences(SETTINGS, 0)
        val token=settings.getString(ACCESS_TOKEN, "")
        return token
    }
    private fun saveToken(token:String):Boolean{
        if (token.isEmpty()){
            return false
        }
        val settings=activity.getSharedPreferences(SETTINGS, 0)
        val editor=settings.edit()
        editor.putString(ACCESS_TOKEN, token)

        editor.apply()
        return true
    }
    private fun goToNextActivity(destinyActivity: AppCompatActivity){
        activity.startActivity(Intent(this.activity, destinyActivity::class.java))
        activity.finish()
    }
    private fun getVenues(lat:String, lng:String, getVenuesInterface: getVenuesInterface){
        val network=Network(activity)
        val section="venues/"
        val method="search/"
        val ll="ll=$lat,$lng"
        val token="oauth_token= ${getToken()}"
        val url="$URL_BASE$section$method?$ll&$token & $VERSION"
        network.httpRequest(activity.applicationContext, url, object:HttpResponse{
            override fun httpResponseSuccess(response: String) {
                var gson= Gson()
                var objectResonse=gson.fromJson(response, FoursquareAPIRequestVenues::class.java)

                var meta=objectResonse.meta
                var venues=objectResonse.response?.venues!!

                if(meta?.code==200){
                    getVenuesInterface.venuesGenerated(venues)
                }else if (meta?.code==400){
                    Message.messageError(activity.applicationContext, meta.errorDetail)
                }else{
                    //generic message
                    Message.messageError(activity.applicationContext, Errors.ERROR_QUERY)
                }
            }

        })
    }
}