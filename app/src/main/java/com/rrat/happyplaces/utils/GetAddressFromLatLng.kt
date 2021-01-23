package com.rrat.happyplaces.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*
import kotlin.coroutines.*

class GetAddressFromLatLng(context: Context, private val latitude: Double, private val longitude: Double) {


    private val geoCoder : Geocoder = Geocoder(context, Locale.getDefault())
    private lateinit var mAddressListener: AddressListener


    fun getAddressFromGeoCode(){

        //showDialog
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("launch", "ok")
            try {
                var result = ""
                val addressList: List<Address?> = geoCoder.getFromLocation(latitude, longitude, 1)

                if(addressList.isNotEmpty()){
                    val address: Address? = addressList[0]
                    val sb = StringBuilder()
                    for(i in 0..address!!.maxAddressLineIndex){
                        sb.append(address.getAddressLine(i)).append(",")
                    }
                    sb.deleteCharAt(sb.length - 1) //Here we remove the last comma that we have added above form the address
                    result = sb.toString()
                }
                withContext(Dispatchers.Main){
                    try {
                        if(result == ""){
                            mAddressListener.onError()
                        }else{
                            mAddressListener.onAddressFound(result)
                        }
                    }catch (e:Exception){
                        e.printStackTrace()

                    }
                }

            }catch (e: IOException){
                Log.i("HappyPlaces", "Unable connect to Geocoder")
            }
        }
    }

    fun setAddressListener(addressListener: AddressListener) {
        mAddressListener = addressListener
    }


    interface AddressListener{
        fun onAddressFound(address: String?)
        fun onError()
    }


}