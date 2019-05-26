package com.example.iram.check_ins.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.iram.check_ins.Fourscuare.Foursquare
import com.example.iram.check_ins.R
import mehdi.sakout.fancybuttons.FancyButton

class Login : AppCompatActivity() {
    var foursquare:Foursquare?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var btnLogin=findViewById<FancyButton>(R.id.btnLogin)
        foursquare=Foursquare(this, Main())
        if (foursquare?.tokenAvailable()!!){
            foursquare?.goToNextActivity()
        }
        btnLogin.setOnClickListener {
            foursquare?.logIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        foursquare?.validateActivityResult(requestCode, resultCode, data)
    }
}
