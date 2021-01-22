package com.rrat.happyplaces.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import java.lang.StringBuilder
import java.util.*
import kotlin.coroutines.CoroutineContext

class GetAddressFromLatLng(context: Context, private val latitude: Double, private val longitude: Double) : CoroutineScope{


    private val geoCoder : Geocoder = Geocoder(context, Locale.getDefault())
    private lateinit var mAddressListener: AddressListener


    fun onBack() : String {
        val addressList: List<Address?> = geoCoder.getFromLocation(latitude, longitude, 1)

        if(addressList != null && addressList.isNotEmpty()){
            val address: Address? = addressList[0]
            val sb = StringBuilder()
            for(i in 0..address!!.maxAddressLineIndex){
                sb.append(address.getAddressLine(i)).append(" ")
            }
            sb.deleteCharAt(sb.length - 1)
            return sb.toString()
        }
    }

    interface AddressListener{
        fun onAddressFound(address: String?)
        fun onError()
    }

}