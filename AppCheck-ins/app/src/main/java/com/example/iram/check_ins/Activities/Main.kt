package com.example.iram.check_ins.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.Log
import com.example.iram.check_ins.Fourscuare.Foursquare
import com.example.iram.check_ins.Fourscuare.Venue
import com.example.iram.check_ins.Interfaces.getVenuesInterface
import com.example.iram.check_ins.Interfaces.locationListener
import com.example.iram.check_ins.R
import com.example.iram.check_ins.Util.Location
import com.google.android.gms.location.LocationResult
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.iram.check_ins.Fourscuare.Category
import com.example.iram.check_ins.Fragments.*
import com.example.iram.check_ins.Interfaces.VenuesLikesInterface
import com.example.iram.check_ins.Interfaces.categoriesVenuesInterface

class Main : AppCompatActivity() {
    private var adapter: TabAdapter? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    var location: Location?=null
    var foursquare:Foursquare?=null


    var list:RecyclerView? = null
    var layoutManager:RecyclerView.LayoutManager? = null

    var toolbar:Toolbar?=null
    val activity:AppCompatActivity=this
    companion object {
        val CURRENT_VENUE="checkins.Main"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        initTabs()


        foursquare=Foursquare(this, this)
        if(foursquare?.tokenAvailable()!!){
            foursquare?.loadCategories(object: categoriesVenuesInterface {
                override fun categoriesVenues(categories: ArrayList<Category>) {
                    Log.d("CATEGORIES", categories.count().toString())
                    CategoriesFragment.implementRecyclerView(categories, activity)
                }

            })
            foursquare?.getLikeVenues(object: VenuesLikesInterface {
                override fun venuesGenerated(venues: ArrayList<Venue>) {
                    Log.d("LIKES", venues.count().toString())
                    LikesFragment.implementRecyclerView(venues, activity)
                }
            })
            location= com.example.iram.check_ins.Util.Location(this, object:locationListener{
                override fun locationResponse(locationResult: LocationResult) {
                    val lat=locationResult.lastLocation.latitude.toString()
                    val lng=locationResult.lastLocation.longitude.toString()

                    foursquare?.getVenues(lat, lng, object:getVenuesInterface{
                        override fun venuesGenerated(venues: ArrayList<Venue>) {
                            VenuesFragment.implementRecyclerView(venues, activity)
                            location?.stopUpdateLocation()
                        }
                    })
                }
            })

        }else foursquare?.sendLogin()

    }

    private fun initTabs() {
        viewPager = findViewById(R.id.pager) as ViewPager
        tabLayout = findViewById(R.id.tabLayout)
        adapter = TabAdapter(supportFragmentManager)
        adapter?.addFragment(VenuesFragment(), "Venues")
        adapter?.addFragment(CategoriesFragment(), "Categories")
        adapter?.addFragment(LikesFragment(), "Likes")
        viewPager?.adapter = adapter
        tabLayout?.setupWithViewPager(viewPager)
    }

    override fun onPause() {
        super.onPause()
        location?.stopUpdateLocation()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        location?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    fun initToolbar(){
        toolbar=findViewById(R.id.toolbar)
        toolbar?.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.ic_profile->{
                val intent=Intent(this, Profile::class.java)
                startActivity(intent)
                return true
            }
            R.id.ic_Logout->{
                foursquare?.logout()
                val intent=Intent(this, Login::class.java)
                startActivity(intent)
                finish()
                return true
            }
            else->return super.onOptionsItemSelected(item)
        }

    }
    override fun onStart() {
        super.onStart()
        location?.inicializeLocation()
    }
}