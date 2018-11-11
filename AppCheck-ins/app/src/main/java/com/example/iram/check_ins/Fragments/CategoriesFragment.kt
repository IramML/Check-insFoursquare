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
import com.example.iram.check_ins.Activities.VenuesCategories
import com.example.iram.check_ins.Fourscuare.Category
import com.example.iram.check_ins.R
import com.example.iram.check_ins.RecyclerViewCategories.ClickListener
import com.example.iram.check_ins.RecyclerViewCategories.LongClickListener
import com.example.iram.check_ins.RecyclerViewCategories.customAdapter
import com.google.gson.Gson

class CategoriesFragment : Fragment() {
    var view0:View?=null
    var layoutManager: RecyclerView.LayoutManager? = null

    companion object {
        var adapterCustom: customAdapter? = null
        var list: RecyclerView? = null
        val CURRENT_CATEGORY="checkins.Categories"
        public fun implementRecyclerView(categoriesList:ArrayList<Category>, activity: AppCompatActivity){
                adapterCustom = customAdapter(categoriesList, object : ClickListener {
                    override fun onClick(view: View, index: Int) {
                        val categoryToJson = Gson()
                        val currentCategoryString = categoryToJson.toJson(categoriesList.get(index))
                        val intent = Intent(activity.applicationContext, VenuesCategories::class.java)
                        intent.putExtra(CategoriesFragment.CURRENT_CATEGORY, currentCategoryString)
                        activity.startActivity(intent)
                    }

                }, object : LongClickListener {
                    override fun longClick(view: View, index: Int) {}
                })
                list?.adapter = adapterCustom
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view0=inflater.inflate(R.layout.fragment_categories, container, false)
        return view0
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        list =activity?.findViewById(R.id.rvCategories) as RecyclerView
        list?.setHasFixedSize(true)
        layoutManager=LinearLayoutManager(view0?.context)
        list?.layoutManager=layoutManager
    }

}
