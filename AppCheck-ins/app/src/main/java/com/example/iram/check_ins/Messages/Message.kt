package com.example.iram.check_ins.Messages

import android.content.Context
import android.widget.Toast
import com.example.iram.check_ins.R

class Message {
    companion object {
        fun message(context: Context, message:Messages){
            var str=""
            when(message){
                Messages.RATIONALE->{
                    str=context.resources.getString(R.string.rationale_location)
                }
                Messages.CHECKIN_SUCCESS->{
                    str=context.resources.getString(R.string.checkin_success)
                }
                Messages.LIKE_SUCCESS->{
                    str=context.resources.getString(R.string.like_success)
                }
                Messages.TRACING_ROUTE->{
                    str=context.resources.getString(R.string.tracing_route)
                }
            }
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
        }
        fun messageError(context: Context, errors: Errors){
             var message=""
            when(errors){
                Errors.NO_NETWORK_AVAILABLE->{
                    message=context.resources.getString(R.string.no_network)
                }
                Errors.HTTP_ERROR->{
                    message=context.resources.getString(R.string.http_error)
                }
                Errors.NO_APP_FOURSQUARE_AVAILABLE->{
                    message=context.resources.getString(R.string.no_app_foursquare)
                }
                Errors.ERROR_CONNECTION_FSQR->{
                    message=context.resources.getString(R.string.error_connection_fsqr)
                }
                Errors.ERROR_EXCHANGE_TOKEN->{
                    message=context.resources.getString(R.string.error_exchange)
                }
                Errors.ERROR_SAVE_TOKEN->{
                    message=context.resources.getString(R.string.error_save_token)
                }
                Errors.PERMISSION_DENIED->{
                    message=context.resources.getString(R.string.permission_denied)
                }
                Errors.ERROR_QUERY->{
                    message=context.resources.getString(R.string.error_query)
                }
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
        fun messageError(context: Context, error: String) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }
}