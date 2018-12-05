package com.example.iram.check_ins.Fourscuare

import android.media.Image
import android.util.Log

data class FoursquareAPIRequestVenues(val meta:Meta, val response:FoursquareResponseVenue)
data class FoursquareAPINewCheckin(val meta:Meta)
data class Meta(val code:Int, val errorDetail:String)
data class FoursquareResponseVenue(val venues:ArrayList<Venue>)
data class Venue(val id:String, val name:String, val location:Location, val categories:ArrayList<Category>,
                 val stats:Stats, var imagePreview:String, var iconCategory:String)
data class Location(val lat:Double, val lng:Double, val state:String, val country:String)
data class Category(val id:String, val name:String, val icon:Icon,  val pluralName:String, val shortName:String)
open class Icon{
    var prefix:String=""
    var suffix:String=""
    var urlIcon:String=""
    fun makeURLImage(tokenAccess:String, version:String, size:String):String{
        val prefix=prefix
        val suffix=suffix
        val token="oauth_token=$tokenAccess"
        val url="$prefix$size$suffix?$token&$version"
        urlIcon=url
        return url
    }
}
data class Stats(val checkinsCount:Int, val usersCount:Int, val tipCount:Int)
data class FoursquareAPISelfUser(val meta:Meta, val response:FoursquareResponseSelfUser)
data class FoursquareResponseSelfUser(val user:User)
data class User(val id:String, val firstName:String, val lastName:String, val photo:Photo, val friends:Friends,
                var tips:Tips, val photos:Photos, val checkins:Checkins)
data class Photo(val id:String="", val width:Int=0, val height:Int):Icon()
data class Friends(val count:Int)
data class Tips(val count:Int)
data class Photos(val count:Int, val items:ArrayList<Photo>)
data class Checkins(val count:Int, val items:ArrayList<Checkin>)
data class Checkin(val shout:String, val venue:Venue)
data class FoursquareAPICategories(var meta:Meta, var response:Categories)
data class Categories(val categories:ArrayList<Category>)
data class LikeResponse(val meta:Meta)
data class VenuesLikes(val meta:Meta, val response:VenuesLikesResponse)
data class VenuesLikesResponse(val venues:VenuesLikeObject)
data class VenuesLikeObject(val items:ArrayList<Venue>)
data class ImagePreviewVenueResponse(var meta:Meta?=null, var response:PhotosResponse?=null)
data class PhotosResponse(val photos:Photos)