package com.example.iram.check_ins.RecyclerViewMain

import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.iram.check_ins.Fourscuare.Venue
import com.example.iram.check_ins.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.venues_template.view.*


class customAdapter(items: ArrayList<Venue>, var listener: ClickListener, var longClickListener: LongClickListener) : RecyclerView.Adapter<customAdapter.ViewHolder>() {
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
        viewHolder.tvName?.text = item?.name
        viewHolder.tvState?.text ="State: ${item?.location?.state}"
        viewHolder.tvCategory?.text = item?.categories?.get(0)?.name
        viewHolder.tvCheckins?.text = "Checkins: ${item?.stats?.checkinsCount.toString()}"
        if(!item?.imagePreview.isNullOrEmpty())
            Picasso.get().load(item?.imagePreview).placeholder(R.drawable.placeholder).into(viewHolder.ivPhoto)
        if(!item?.iconCategory.isNullOrEmpty())
            Picasso.get().load(item?.iconCategory).placeholder(R.drawable.ic_category).into(viewHolder.ivCategory)

    }
    override fun getItemCount(): Int {
        return items?.count()!!
    }
    class ViewHolder(itemView: View, listener:ClickListener, longClickListener:LongClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        var tvName: TextView?=null
        var tvState:TextView?=null
        var tvCategory:TextView?=null
        var tvCheckins:TextView?=null
        var ivPhoto:ImageView?=null
        var ivCategory:ImageView?=null
        var listener: ClickListener?=null
        var longClickListener: LongClickListener?=null

        init {
            tvName = itemView.tvName
            tvState = itemView.tvState
            tvCategory = itemView.tvCategory
            tvCheckins = itemView.tvCheckins
            ivPhoto=itemView.ivPhoto
            ivCategory=itemView.imgCategory
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