package com.example.shoppingapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import okhttp3.Callback

class LocationUtils (context: Context){



    fun hasLocationPermission(context:Context) : Boolean{
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
    }

    //fused reguest from the user. For translating address
    private val _fetch:FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    //it is update information
    fun updateInformation(viewModel: LocationViewModel){
        val locationCallback=object:LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    val location=LocationData(latitude = it.latitude, longitude = it.longitude )
                    viewModel.updateLocation(location)
                }
            }
        }

    }
}