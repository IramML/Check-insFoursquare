package com.example.iram.check_ins.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import com.example.iram.check_ins.Fourscuare.Foursquare
import com.example.iram.check_ins.Fourscuare.User
import com.example.iram.check_ins.Fourscuare.objectGrid
import com.example.iram.check_ins.GridViewDetailsVenues.adapterGridView
import com.example.iram.check_ins.Interfaces.UsersInterface
import com.example.iram.check_ins.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_profile.*
import java.text.NumberFormat
import java.util.*

class Profile : AppCompatActivity() {
    var foursquare: Foursquare?=null

    var toolbar: Toolbar?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val tvName=findViewById<TextView>(R.id.tvName)
        val grid=findViewById<GridView>(R.id.grid)
        val profileImage=findViewById<CircleImageView>(R.id.profile_image)
        foursquare= Foursquare(this, this)
        if(foursquare?.tokenAvailable()!!){
            foursquare?.getCurrentUser(object: UsersInterface {
                override fun getCurrentUser(user: User) {
                    initToolbar("${user.firstName} ${user.lastName}")
                    tvName.text=user.firstName
                    Picasso.get().load(user.photo?.urlIcon).into(profileImage)
                    val listGrid=ArrayList<objectGrid>()
                    listGrid.add(objectGrid("Photos ${NumberFormat.getNumberInstance(Locale.US).format(user.photos?.count)}", R.drawable.ic_grid_photo, ContextCompat.getColor(applicationContext, R.color.secondaryColor)))
                    listGrid.add(objectGrid("Checkins: ${NumberFormat.getNumberInstance(Locale.US).format(user.checkins?.count)}", R.drawable.ic_grid_checkins, ContextCompat.getColor(applicationContext, R.color.primaryLightColor)))
                    listGrid.add(objectGrid("Friends: ${NumberFormat.getNumberInstance(Locale.US).format(user.friends?.count)}", R.drawable.ic_grid_users, ContextCompat.getColor(applicationContext, R.color.primaryColor)))
                    listGrid.add(objectGrid("Tips: ${NumberFormat.getNumberInstance(Locale.US).format(user.tips?.count)}", R.drawable.ic_grid_tips, ContextCompat.getColor(applicationContext, R.color.secondaryLightColor)))
                    val adapter= adapterGridView(applicationContext, listGrid)
                    grid.adapter=adapter
                }

            })
        }else foursquare?.sendLogin()

    }
    fun initToolbar(nameProfile:String){
        toolbar=findViewById(R.id.toolbar)
        toolbar?.setTitle(nameProfile)
        setSupportActionBar(toolbar)
        var actionBar=supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar?.setNavigationOnClickListener {
            finish()
        }
    }
}
