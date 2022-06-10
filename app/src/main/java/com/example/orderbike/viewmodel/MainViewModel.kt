package com.example.orderbike.viewmodel


import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderbike.isOnline
import com.example.orderbike.model.BikeModel
import com.example.orderbike.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel : ViewModel(){

    private var bikelist_data : BikeModel?=null
    private  var error_msg: String=""

    //bike response list in livedata
    private val _bikeItemlist= MutableLiveData<BikeModel?>()
    val bikeListResponse: LiveData<BikeModel?> get() = _bikeItemlist

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> get() = _errorMessage

    private val _currentLocationCord = MutableLiveData<Location>()
    val currentLocationCord : LiveData<Location> get() = _currentLocationCord

    private val _currentDistance = MutableLiveData<Float>()
    val currentDistance : LiveData<Float> get() = _currentDistance


    fun getLocationChange(location:Location){
        Log.i("currentLocationCord","Yes")
        _currentLocationCord.value=location
        Log.i("currentLocationCord",_currentLocationCord.value.toString())
    }

    //The suspend function @withContext is use to manage the suspend block
    //within a the subrcibe context - i.e mainActivity,and other fragments
    fun getBikeList() = viewModelScope.launch {

        withContext(Dispatchers.IO){
            try {
                bikelist_data = ApiService.getInstance().getBikeItem()
            } catch (e: Exception) {
                 error_msg = e.message.toString()
            }
        }
            //update the live data value from IO thread result
            _bikeItemlist.value = bikelist_data
            _errorMessage.value = error_msg

    }







}