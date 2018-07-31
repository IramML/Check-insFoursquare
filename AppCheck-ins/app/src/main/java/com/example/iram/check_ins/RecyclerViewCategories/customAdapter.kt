package com.example.iram.check_ins.RecyclerViewCategories

import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.iram.check_ins.Fourscuare.Category
import com.example.iram.check_ins.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.venues_template.view.*


class customAdapter(items: ArrayList<Category>, var listener: ClickListener, var longClickListener: LongClickListener) : RecyclerView.Adapter<customAdapter.ViewHolder>() {
    var viewHolder: ViewHolder?=null
    var items: ArrayList<Category>?=null
    init {
        this.items=items
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.template_categories, viewGroup, false)
        viewHolder = ViewHolder(view, listener, longClickListener)
        return viewHolder!!
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        var item=items?.get(i)
        viewHolder.tvName.text = "${item?.name}"
        Picasso.get().load(item?.icon?.urlIcon).into(viewHolder.ivPhoto)
    }

    override fun getItemCount(): Int {
        return items?.count()!!
    }
    class ViewHolder(itemView: View, listener:ClickListener, longClickListener:LongClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        var tvName: TextView
        var ivPhoto:ImageView?=null
        var listener: ClickListener?=null
        var longClickListener: LongClickListener?=null
        init {
            tvName = itemView.tvName
            ivPhoto= itemView.ivPhoto
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