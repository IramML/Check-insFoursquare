package com.example.iram.check_ins.Fourscuare

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
    var categories:ArrayList<CategorY>?=null
    var stats:Stats?=null
}
class Location{
    var lat:Double=0.0
    var lng:Double=0.0
    var state:String=""
    var country:String=""
}
class CategorY{
    var id:String=""
    var name:String=""
    var icon:Icon?=null
}
open class Icon{
    var prefix:String=""
    var suffix:String=""
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
    var friend:Friends?=null
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