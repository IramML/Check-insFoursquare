package com.example.iram.check_ins.Fourscuare

import android.media.Image
import android.util.Log

class FoursquareAPIRequestVenues {
    var meta:Meta?=null
    var response:FoursquareResponseVenue?=null

}
class FoursquareAPINewCheckin{
    var meta:Meta?=null
}
class Meta{
    var code:Int=0
    var errorDetail:String=""
}
class FoursquareResponseVenue{
    var venues:ArrayList<Venue>?=null
}
class Venue{
    var id:String=""
    var name:String=""
    var location:Location?=null
    var categories:ArrayList<Category>?=null
    var stats:Stats?=null
    var imagePreview:String=""
    var iconCategory:String=""
}
class Location{
    var lat:Double=0.0
    var lng:Double=0.0
    var state:String=""
    var country:String=""
}
class Category{
    var id:String=""
    var name:String=""
    var icon:Icon?=null
    var pluralName:String=""
    var shortName:String=""
}
open class Icon{
    var prefix:String=""
    var suffix:String=""
    var urlIcon:String=""
    fun makeURLImage(tokenAccess:String, version:String, size:String):String{
        val prefix=prefix
        val suffix=suffix
        val token="oauth_token= $tokenAccess"
        val url="$prefix$size$suffix?$token&$version"
        urlIcon=url
        return url
    }
}
class Stats{
    var checkinsCount=0
    var usersCount=0
    var tipCount=0
}
class FoursquareAPISelfUser{
    var meta:Meta?=null
    var response:FoursquareResponseSelfUser?=null
}
class FoursquareResponseSelfUser{
    var user:User?=null
}
class User{
    var id=""
    var firstName=""
    var lastName=""
    var photo:Photo?=null
    var friends:Friends?=null
    var tips:Tips?=null
    var photos:Photos?=null
    var checkins:Checkins?=null
}
class Photo:Icon(){
    var id=""
    var width=0
    var height=0
}
class Friends{
    var count=0
}
class Tips{
    var count=0
}
class Photos{
    var count=0
    var items:ArrayList<Photo>?=null
}
class Checkins{
    var count=0
    var items:ArrayList<Checkin>?=null
}
class Checkin{
    var shout=""
    var venue:Venue?=null
}
class FoursquareAPICategories{
    var meta:Meta?=null
    var response:Categories?=null
}
class Categories{
    var categories:ArrayList<Category>?=null
}
class LikeResponse{
    var meta:Meta?=null
}
class VenuesLikes{
    var meta:Meta?=null
    var response:VenuesLikesResponse?=null
}
class VenuesLikesResponse{
    var venues:VenuesLikeObject?=null
}
class VenuesLikeObject{
    var items:ArrayList<Venue>?=null
}
class ImagePreviewVenueResponse{
    var meta:Meta?=null
    var response:PhotosResponse?=null
}
class PhotosResponse{
    var photos:Photos?=null
}