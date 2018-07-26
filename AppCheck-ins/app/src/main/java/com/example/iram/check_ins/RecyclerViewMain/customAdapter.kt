package com.example.iram.check_ins.RecyclerViewMain

import android.content.Context
import android.graphics.Color
import android.widget.RatingBar
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.iram.check_ins.Fourscuare.Venue
import com.example.iram.check_ins.R
import kotlinx.android.synthetic.main.venues_template.view.*


class customAdapter(items: ArrayList<Venue>, var listener: ClickListener, var longClickListener: LongClickListener) : RecyclerView.Adapter<customAdapter.ViewHolder>() {
    var multiSelection: Boolean? = false

    var itemsSelected: ArrayList<Int>
    var viewHolder: ViewHolder?=null
    var items: ArrayList<Venue>?=null
    init {
        this.items=items
        itemsSelected = ArrayList()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.venues_template, viewGroup, false)
        viewHolder = ViewHolder(view, listener, longClickListener)
        return viewHolder!!
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        var item=items?.get(i)
        viewHolder.tvName.text = "Name: " + item?.name
        //viewHolder.tvCost.text = "Cost: " + items[i].cost
        //viewHolder.rating.rating = items[i].rating

        /*if (itemsSelected.contains(i))
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY)
        else
            viewHolder.itemView.setBackgroundColor(Color.WHITE)*/

    }

    fun startActionMode() {
        multiSelection = true
    }

    fun destroyActionMode() {
        multiSelection = false
        itemsSelected.clear()
        notifyDataSetChanged()
    }

    fun finishActionMode() {
        val location = -1
        for (item in itemsSelected) {
            itemsSelected.remove(item)
        }
        multiSelection = false
        notifyDataSetChanged()
    }

    fun selectItem(index: Int) {
        var location = -1
        if (multiSelection!!) {
            for (i in 0 until itemsSelected.count()) {
                if (itemsSelected[i] === index) {
                    location = i
                }
            }
            if (location != -1) {
                itemsSelected.remove(location)
            } else {
                itemsSelected.add(index)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return items?.count()!!
    }

    fun getItemsSelected(): Int {
        return itemsSelected.count()
    }

    fun deleteSelected() {
        if (itemsSelected.count() > 0) {
            val itemsDeleted = ArrayList<Venue>()

            for (index in itemsSelected) {
                itemsDeleted.add(items?.get(index)!!)
            }
            items?.removeAll(itemsDeleted)
            itemsSelected.clear()

        }
    }

    class ViewHolder(itemView: View, listener:ClickListener, longClickListener:LongClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        var tvName: TextView
        //var tvCost: TextView
        //var rating: RatingBar
        var listener: ClickListener?=null
        var longClickListener: LongClickListener?=null

        init {
            tvName = itemView.tvName
            //tvCost = itemView.findViewById(R.id.tvCost)
            //rating = itemView.findViewById(R.id.rating)
            this.listener=listener
            this.longClickListener=longClickListener
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }


        override fun onClick(view: View?) {
            this.listener?.onClick(view!!, adapterPosition)
        }
        override fun onLongClick(view: View?): Boolean {
            this.longClickListener?.longClick(view!!, adapterPosition)
            return false
        }
    }
}