package com.test.mylocations

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.test.mylocations.presentation.model.LocationPresentationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.random.Random

const val COLLECTION_NAME = "mylocations"

class MainViewModel :ViewModel()
{
    private val _location = MutableLiveData<List<LocationPresentationModel>>()
    val location : LiveData<List<LocationPresentationModel>>
        get() = _location

    private val db = Firebase.firestore

    val _msgs = MutableLiveData<String?>()
    val msgs: LiveData<String?>
        get() = _msgs

    /**
     * Solicita la ubicacion y espera el resultado
     */
    // se espera que ya se hayan pedido los permisos necesarios
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(fusedLocationClient: FusedLocationProviderClient)
    {
        val locationRequest = LocationRequest.Builder(
            LocationRequest.PRIORITY_HIGH_ACCURACY
            ,TimeUnit.MINUTES.toMillis(2)
        ).build()
        fusedLocationClient.requestLocationUpdates(locationRequest,Dispatchers.IO.asExecutor(),object :LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult)
            {
                for (location in locationResult.locations) {
                  Log.d("LOCATION", "Location: ${location.latitude}, ${location.longitude}")
                  uploadGeo(
                      19.304929 + Random.nextDouble(-0.02,0.02)
                      ,-99.203578 + Random.nextDouble(-0.02,0.02)
                  )
                }
            }
        })
    }

    fun uploadGeo( latitude :Double,longitude:Double , callback: (String) -> Unit = {})
    {
        viewModelScope.launch {
            db.collection(COLLECTION_NAME).add(hashMapOf(
                "timestamp" to System.currentTimeMillis()
                ,"geo" to GeoPoint(latitude,longitude)
            ))
                .addOnSuccessListener {
                    callback(it.id)
                    fetchLocations()
                }.addOnFailureListener {
                    showMsgFromException(it)
                }
        }
    }

    private fun fetchLocations()
    {
        db.collection(COLLECTION_NAME).get()
            .addOnSuccessListener {snap->
                val list = snap.documents.map {doc->
                    val geo = doc.data?.get("geo") as? GeoPoint
                    geo?.let {
                        LocationPresentationModel(it.latitude,it.longitude,
                                                  (doc.data?.get("timestamp") as? Long) ?: 0L
                        )
                    }
                }
                _location.postValue(list.filterNotNull())
            }.addOnFailureListener {
                showMsgFromException(it)
            }
    }

    private fun showMsgFromException(ex: Throwable)
    {
        Log.e(this::class.java.name, ex.message.orEmpty(), ex)
        ex.message ?: return
        ex.message!!.takeIf { it.isNotBlank() }?.let { msg ->
            _msgs.postValue(msg)
        }
    }

    fun clearError()
    {
        _msgs.value = null
    }
}