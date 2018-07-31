package com.example.iram.check_ins.Fourscuare

import android.content.Intent
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.iram.check_ins.Activities.Login
import com.example.iram.check_ins.Interfaces.*
import com.example.iram.check_ins.Messages.Errors
import com.example.iram.check_ins.Messages.Message
import com.example.iram.check_ins.Messages.Messages
import com.example.iram.check_ins.Util.Location
import com.example.iram.check_ins.Util.Network
import com.foursquare.android.nativeoauth.FoursquareOAuth
import com.google.gson.Gson

class Foursquare(var activity:AppCompatActivity, var destinyActivity:AppCompatActivity) {
    private val CODE_CONNECTION=200
    private val CODE_TOKEN_EXCHANGE=201

    private val CLIENT_ID="GASUFJU4LJJEPKBI5WRAOO42RO01G3BM3QCJI4H1T13K0S1G"
    private val CLIENT_SECRET="LH2UVZN0QAPNMU5AF0N0YMJUUBKP3CBQ3EVKEQCU3SOENCAC"

    private val SETTINGS="settings"
    private val ACCESS_TOKEN="accessToken"

    private val URL_BASE="https://api.foursquare.com/v2/"
    private val VERSION="v=20180117"

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

            if(!saveToken(accessToken)) Message.messageError(activity.applicationContext, Errors.ERROR_SAVE_TOKEN)
            else goToNextActivity()

        }else{
            Message.messageError(activity.applicationContext, Errors.ERROR_EXCHANGE_TOKEN)
        }
    }
    fun tokenAvailable():Boolean{
        return getToken()!=""
    }
    fun sendLogin(){
        activity.startActivity(Intent(this.activity, Login::class.java))
        activity.finish()
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
    fun logout(){
        val settings=activity.getSharedPreferences(SETTINGS, 0)
        val editor=settings.edit()
        editor.putString(ACCESS_TOKEN, "")

        editor.apply()
    }
    fun goToNextActivity(){
        activity.startActivity(Intent(this.activity, destinyActivity::class.java))
        activity.finish()
    }
    fun getVenues(lat:String, lng:String, getVenuesInterface: getVenuesInterface){
        val network=Network(activity)
        val section="venues/"
        val method="search/"
        val ll="ll=$lat,$lng"
        val token="oauth_token=${getToken()}"
        val url="$URL_BASE$section$method?limit=10&$ll&$token&$VERSION"
        network.httpRequest(activity.applicationContext, url, object:HttpResponse{
            override fun httpResponseSuccess(response: String) {
                var gson= Gson()
                var objectResonse=gson.fromJson(response, FoursquareAPIRequestVenues::class.java)

                var meta=objectResonse.meta
                var venues=objectResonse.response?.venues!!
                for(venue in venues){
                    getImagePreview(venue.id, object:ImagePreviewInterface{
                        override fun getImagePreview(photos: ArrayList<Photo>) {
                            if (photos.count()>0){
                                val urlImage= photos.get(0).makeURLImage(getToken(), VERSION, "original")
                                venue.imagePreview=urlImage
                            }
                            if (venue.categories?.count()!!>0){
                                val urlIcon=venue.categories?.get(0)?.icon?.makeURLImage(getToken(), VERSION, "64")
                                venue.iconCategory=urlIcon!!
                            }
                        }
                    })
                }
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
     fun getVenues(lat:String, lng:String, idCategory:String, getVenuesInterface: getVenuesInterface){
        val network=Network(activity)
        val section="venues/"
        val method="search/"
        val ll="ll=$lat,$lng"
        val category="categoryId=$idCategory"
        val token="oauth_token=${getToken()}"
        val url="$URL_BASE$section$method?limit=10&$ll&$category&$token&$VERSION"
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
    private fun getImagePreview(venueId:String, imagePreviewInterface: ImagePreviewInterface){
        val network=Network(activity)
        val section="venues/"
        val method="photos/"
        val token="oauth_token= ${getToken()}"
        val parameters="limit=1"
        val url="$URL_BASE$section$venueId/$method?$parameters&$token&$VERSION"
        network.httpRequest(activity.applicationContext, url, object:HttpResponse{
            override fun httpResponseSuccess(response: String) {
                var gson= Gson()
                var objectResonse=gson.fromJson(response, ImagePreviewVenueResponse::class.java)

                var meta=objectResonse.meta
                var photos=objectResonse.response?.photos?.items!!

                if(meta?.code==200){
                    imagePreviewInterface.getImagePreview(photos)
                }else if (meta?.code==400){
                    Message.messageError(activity.applicationContext, meta.errorDetail)
                }else{
                    //generic message
                    Message.messageError(activity.applicationContext, Errors.ERROR_QUERY)
                }
            }

        })
    }

    fun newCheckin(id:String, location:com.example.iram.check_ins.Fourscuare.Location, message:String){
        val network=Network(activity)
        val section="checkins/"
        val method="add/"
        val token="oauth_token=${getToken()}"
        var query="?venueId=$id&shout=$message&ll=${location.lat.toString()},${location.lng.toString()}&$token&$VERSION"
        val url="$URL_BASE$section$method$query"
        network.httpPOSTRequest(activity.applicationContext, url, object:HttpResponse{
            override fun httpResponseSuccess(response: String) {
                val gson= Gson()
                var objectResonse=gson.fromJson(response, FoursquareAPINewCheckin::class.java)

                var meta=objectResonse.meta

                if(meta?.code==200){
                    Message.message(activity.applicationContext, Messages.CHECKIN_SUCCESS)
                }else if (meta?.code==400){
                    Message.messageError(activity.applicationContext, meta.errorDetail)
                }else{
                    //generic message
                    Message.messageError(activity.applicationContext, Errors.ERROR_QUERY)
                }
            }

        })
    }
    fun newLike(id:String){
        val network=Network(activity)
        val section="venues/"
        val method="like/"
        val token="oauth_token=${getToken()}"
        var query="?$token&$VERSION"
        val url="$URL_BASE$section$id/$method$query"
        network.httpPOSTRequest(activity.applicationContext, url, object:HttpResponse{
            override fun httpResponseSuccess(response: String) {
                Log.d("NEW_LIKE", response)
                val gson=Gson()
                val objectResponse=gson.fromJson(response, LikeResponse::class.java)

                val meta=objectResponse.meta
                if(meta?.code==200){
                    Message.message(activity.applicationContext, Messages.LIKE_SUCCESS)
                }else if (meta?.code==400){
                    Message.messageError(activity.applicationContext, meta.errorDetail)
                }else{
                    //generic message
                    Message.messageError(activity.applicationContext, Errors.ERROR_QUERY)
                }
            }


        })
    }
    fun getLikeVenues(venuesLikesInterface: VenuesLikesInterface){
        val network=Network(activity)
        val section="users/"
        val method="self/"
        val token="oauth_token=${getToken()}"
        val url="$URL_BASE$section${method}venuelikes?limit=10&$token&$VERSION"
        network.httpRequest(activity.applicationContext, url, object:HttpResponse {
            override fun httpResponseSuccess(response: String) {
                var gson = Gson()
                var objectResonse = gson.fromJson(response, VenuesLikes::class.java)

                var meta = objectResonse.meta
                var venues = objectResonse.response?.venues?.items!!

                if (meta?.code == 200) {
                    venuesLikesInterface.venuesGenerated(venues)
                } else if (meta?.code == 400) {
                    Message.messageError(activity.applicationContext, meta.errorDetail)
                } else {
                    //generic message
                    Message.messageError(activity.applicationContext, Errors.ERROR_QUERY)
                }
            }
        })
    }
    fun getCurrentUser(currentUserInterface:UsersInterface){
        val network=Network(activity)
        val section="users/"
        val method="self/"
        val token="oauth_token=${getToken()}"
        var query="?$token&$VERSION"
        val url="$URL_BASE$section$method$query"
        network.httpRequest(activity.applicationContext, url, object:HttpResponse{
            override fun httpResponseSuccess(response: String) {
                val gson= Gson()
                var objectResonse=gson.fromJson(response, FoursquareAPISelfUser::class.java)

                var meta=objectResonse.meta

                if(meta?.code==200){
                    val user=objectResonse.response?.user
                    user?.photo?.makeURLImage(getToken(), VERSION, "128x128")
                    currentUserInterface.getCurrentUser(user!!)
                }else if (meta?.code==400){
                    Message.messageError(activity.applicationContext, meta.errorDetail)
                }else{
                    //generic message
                    Message.messageError(activity.applicationContext, Errors.ERROR_QUERY)
                }
            }

        })
    }
    fun loadCategories(categoriesInterface:categoriesVenuesInterface){
        val network=Network(activity)
        val section="venues/"
        val method="categories/"
        val token="oauth_token=${getToken()}"
        var query="?$token&$VERSION"
        val url="$URL_BASE$section$method$query"
        network.httpRequest(activity.applicationContext, url, object:HttpResponse{
            override fun httpResponseSuccess(response: String) {
                val gson= Gson()
                var objectResonse=gson.fromJson(response, FoursquareAPICategories::class.java)

                var meta=objectResonse.meta

                if(meta?.code==200){
                    val categories=objectResonse.response?.categories!!
                    for (category in categories){
                        category.icon?.makeURLImage(getToken(), VERSION, "bg_64")
                    }
                    categoriesInterface.categoriesVenues(objectResonse.response?.categories!!)
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