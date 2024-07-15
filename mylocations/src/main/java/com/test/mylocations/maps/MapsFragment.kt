package com.test.mylocations.maps

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.test.mylocations.MainViewModel
import com.test.mylocations.R
import com.test.mylocations.presentation.model.LocationPresentationModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MapsFragment : Fragment()
{

    private val vm by lazy {   ViewModelProvider(requireActivity())[MainViewModel::class.java]}
    private val callback = OnMapReadyCallback { googleMap ->
        vm.location.observe(viewLifecycleOwner, Observer {
            it?:return@Observer
            addMarkers(googleMap,it)
        })
        googleMap.setMinZoomPreference(5f)

    }

    private fun addMarkers(
        googleMap: GoogleMap,
        locationPresentationModels: List<LocationPresentationModel>
    )
    {
        var latCum = 0.0
        var longCum = 0.0
        locationPresentationModels.forEach {
            latCum +=  it.latitude
            longCum +=  it.longitude
            googleMap.addMarker(MarkerOptions().position(
                LatLng(it.latitude, it.longitude)
            ) .title(
                 SimpleDateFormat("H:mm:ss dd/MM/yyyy", Locale.US).format(Date(it.timestamp) )
            ))
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(
            LatLng(latCum/locationPresentationModels.size
            ,longCum/locationPresentationModels.size
        )))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}