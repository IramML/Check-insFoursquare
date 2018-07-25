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
                Errors.NO_APP_FOURSQUARE_AVAILABLE->{
                    message="The foursquare application is not installed"
                }
                Errors.ERROR_CONNECTION_FSQR->{
                    message="Could not complete the connection to Foursquare"
                }
                Errors.ERROR_EXCHANGE_TOKEN->{
                    message="Could not complete the exchange token in Foursquare"
                }
                Errors.ERROR_SAVE_TOKEN->{
                    message="Could not save token"
                }
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}