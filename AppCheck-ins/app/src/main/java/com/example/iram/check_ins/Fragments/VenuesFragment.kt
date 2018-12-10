package com.example.iram.check_ins.Fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.iram.check_ins.Activities.Main
import com.example.iram.check_ins.Activities.VenueDetails
import com.example.iram.check_ins.Fourscuare.Foursquare
import com.example.iram.check_ins.Fourscuare.Venue
import com.example.iram.check_ins.R
import com.example.iram.check_ins.RecyclerViewMain.ClickListener
import com.example.iram.check_ins.RecyclerViewMain.LongClickListener
import com.example.iram.check_ins.RecyclerViewMain.customAdapter
import com.example.iram.check_ins.Util.Location
import com.google.gson.Gson

class VenuesFragment : Fragment() {

    var view0:View?=null
    var layoutManager:RecyclerView.LayoutManager? = null

    companion object {
        var list:RecyclerView? = null
        var adapterCustom: customAdapter? = null
        val CURRENT_VENUE="checkins.Main"
        public fun implementRecyclerView(venuesList:ArrayList<Venue>, activity:AppCompatActivity){
            adapterCustom = customAdapter(venuesList, object : ClickListener{
                override fun onClick(view: View, index: Int) {
                    val venueToJson=Gson()
                    val currentVenueString=venueToJson.toJson(venuesList.get(index))
                    val intent=Intent(activity.applicationContext, VenueDetails::class.java)
                    intent.putExtra(Main.CURRENT_VENUE, currentVenueString)
                    activity.startActivity(intent)
                }

            }, object : LongClickListener{
                override fun longClick(view: View, index: Int) { }
            })
            list?.adapter=adapterCustom
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view0=inflater.inflate(R.layout.fragment_venues, container, false)
        return view0

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        list=activity?.findViewById(R.id.rvVenues) as RecyclerView
        list?.setHasFixedSize(true)
        layoutManager=LinearLayoutManager(view0?.context)
        list?.layoutManager=layoutManager
    }


}
