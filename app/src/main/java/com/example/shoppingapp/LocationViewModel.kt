package com.example.shoppingapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LocationViewModel:ViewModel() {
    private val _location=mutableStateOf<LocationData?>(null)
    val location: State<LocationData?> = _location

    private val _address = mutableStateOf(listOf<GeocodingResult>())
    val address : State<List<GeocodingResult>> = _address

    fun updateLocation(newLocation:LocationData){
        _location.value=newLocation
    }

    fun fetchAddress(latlng:String){
        try {
            viewModelScope.launch {
                val result= RetrofitClient.create().getAddress(latlng, "AIzaSyBpZ2_-UnN52FNZLt7bPeXgLw2ihn-G4EU")
                _address.value = result.results
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}