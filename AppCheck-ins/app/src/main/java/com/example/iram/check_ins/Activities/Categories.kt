package com.example.iram.check_ins.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.example.iram.check_ins.Fourscuare.Category
import com.example.iram.check_ins.Fourscuare.Foursquare
import com.example.iram.check_ins.Interfaces.categoriesVenuesInterface
import com.example.iram.check_ins.R
import com.example.iram.check_ins.RecyclerViewCategories.customAdapter
import com.example.iram.check_ins.RecyclerViewCategories.ClickListener
import com.example.iram.check_ins.RecyclerViewCategories.LongClickListener
import com.google.gson.Gson

class Categories : AppCompatActivity() {
    var adapterCustom: customAdapter? = null
    var list: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    var toolbar: Toolbar?=null
    companion object {
        val CURRENT_CATEGORY="checkins.Categories"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        initRecyclerView()
        initToolbar()
        var foursquare=Foursquare(this, Categories())
        foursquare.loadCategories(object:categoriesVenuesInterface{
            override fun categoriesVenues(categories: ArrayList<Category>) {
                Log.d("CATEGORIES", categories.count().toString())
                implementRecyclerView(categories)
            }

        })
    }
    private fun initRecyclerView(){
        list=findViewById(R.id.rvCategories)
        list?.setHasFixedSize(true)
        layoutManager= LinearLayoutManager(this)
        list?.layoutManager=layoutManager
    }
    private fun implementRecyclerView(categoriesList:ArrayList<Category>){
        adapterCustom = customAdapter(categoriesList, object : ClickListener {
            override fun onClick(view: View, index: Int) {
                val categoryToJson= Gson()
                val currentCategoryString=categoryToJson.toJson(categoriesList.get(index))
                val intent= Intent(applicationContext, VenuesCategories::class.java)
                intent.putExtra(Categories.CURRENT_CATEGORY, currentCategoryString)
                startActivity(intent)
            }

        }, object : LongClickListener {
            override fun longClick(view: View, index: Int) {}
        })
        list?.adapter=adapterCustom
    }
    fun initToolbar(){
        toolbar=findViewById(R.id.toolbar)
        toolbar?.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)
    }
}
