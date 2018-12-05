package com.example.iram.check_ins.Fragments

import android.support.design.widget.BottomSheetDialogFragment
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.iram.check_ins.Fourscuare.Foursquare
import com.example.iram.check_ins.Fourscuare.Venue
import com.example.iram.check_ins.R
import java.net.URLEncoder

class BottomSheetCheckinFragment: BottomSheetDialogFragment(){
    var mTag: String? = null
    var etComent: EditText?=null
    var btnCheckin: Button?=null

    var view0: View?=null
    var foursquare:Foursquare?=null
    var currentVenue:Venue?=null
    fun newInstance(tag: String): BottomSheetCheckinFragment {
        val fragment = BottomSheetCheckinFragment()
        val args = Bundle()
        args.putString("TAG", tag)
        fragment.setArguments(args)
        return fragment
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTag = arguments!!.getString("TAG")
        btnCheckin?.setOnClickListener {
            val message= URLEncoder.encode(etComent?.text.toString(), "UTF-8")
            foursquare?.newCheckin(currentVenue?.id!!, currentVenue?.location!!, message)
        }
    }
    fun getData(foursquare: Foursquare, currentVenue: Venue) {
        this.foursquare=foursquare
    }
    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        view0= inflater.inflate(R.layout.bottom_sheet_checkin, container, false)
        etComent=view0?.findViewById(R.id.etComent) as EditText
        btnCheckin=view0?.findViewById(R.id.btnCkeckin) as Button
        return view0
    }
}