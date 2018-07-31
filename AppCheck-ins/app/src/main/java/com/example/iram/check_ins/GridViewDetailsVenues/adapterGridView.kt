package com.example.iram.check_ins.GridViewDetailsVenues

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.iram.check_ins.Fourscuare.objectGrid
import com.example.iram.check_ins.R
import kotlinx.android.synthetic.main.template_grid_detail_venue.view.*

class adapterGridView(var context:Context, items:ArrayList<objectGrid>):BaseAdapter(){
    var items:ArrayList<objectGrid>?=null
    init {
        this.items=items
    }
    override fun getView(position: Int, convertView: View?, p2: ViewGroup?): View {
        var view:View?= convertView
        var viewHolder:ViewHolder?=null


        if (view==null){
            view=LayoutInflater.from(context).inflate(R.layout.template_grid_detail_venue,null)
            viewHolder=ViewHolder(view)
            view.tag=viewHolder
        }else{
            viewHolder=view.tag as? ViewHolder
        }
        val item=items?.get(position) as objectGrid

        viewHolder?.name?.text=item.name
        viewHolder?.image?.setImageResource(item.icon)
        viewHolder?.container?.setBackgroundColor(item.background)
        return view!!
    }

    override fun getItem(position: Int): Any {
        return this.items?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return this.items?.count()!!
    }
    private class ViewHolder(view: View){
        var name:TextView?=null
        var image:ImageView?=null
        var container:LinearLayout?=null
        init {
            name=view.tvName
            image=view.ivPhoto
            container=view.container
        }
    }
}