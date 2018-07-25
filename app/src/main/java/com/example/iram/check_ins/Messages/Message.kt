package com.example.iram.check_ins.Messages

import android.content.Context
import android.widget.Toast

class Message {
    companion object {
        fun messageSuccess(){

        }
        fun messageError(context: Context, errors: Errors){
             var message=""
            when(errors){
                Errors.NO_NETWORK_AVAILABLE->{
                    message="No network available"
                }
                Errors.HTTP_ERROR->{
                    message="There was an error with the http request"
                }
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}