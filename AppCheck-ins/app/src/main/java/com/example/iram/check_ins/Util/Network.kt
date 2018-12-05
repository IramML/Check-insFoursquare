package com.example.iram.check_ins.Util

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.iram.check_ins.Interfaces.HttpResponse
import com.example.iram.check_ins.Messages.Errors
import com.example.iram.check_ins.Messages.Message

class Network(var activity: AppCompatActivity){
    fun availableNetwok():Boolean {
        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
    fun httpRequest(context: Context, url:String, httpResponse: HttpResponse){
        if(availableNetwok()) {
            Log.d("URL_SERVICE","$activity $url")
            val queue = Volley.newRequestQueue(context)

            val request = StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
                httpResponse.httpResponseSuccess(response)
            }, Response.ErrorListener { error ->
                Log.d("HTTP_REQUEST", error.message.toString())
                Message.messageError(context, Errors.HTTP_ERROR)
            })
            queue.add(request)
        }else{
            Message.messageError(context, Errors.NO_NETWORK_AVAILABLE)
        }
    }
    fun httpPOSTRequest(context: Context, url:String, httpResponse: HttpResponse){
        if(availableNetwok()) {
            val queue = Volley.newRequestQueue(context)

            val request = StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
                httpResponse.httpResponseSuccess(response)
            }, Response.ErrorListener { error ->
                Log.d("HTTP_REQUEST", error.message.toString())
                Message.messageError(context, Errors.HTTP_ERROR)
            })
            queue.add(request)
        }else{
            Message.messageError(context, Errors.NO_NETWORK_AVAILABLE)
        }
    }
}