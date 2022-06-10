package com.example.orderbike.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.orderbike.R
import com.example.orderbike.databinding.FragmentDetailedBinding
import com.example.orderbike.model.Feature
import com.example.orderbike.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.launch

class DetailedFragment : Fragment(),OnMapReadyCallback {

    private var _binding: FragmentDetailedBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: MainViewModel by activityViewModels()
    private var feature: List<Feature>? = null
    val args: DetailedFragmentArgs by navArgs()

    var bikelatitude: Double? = 0.0
    var bikelongitude: Double? = 0.0
    var noOfBikes: String = ""

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailedBinding.inflate(inflater, container, false)

        //Initialize map support fragment
        mapFragment =
            childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        Log.i("argID", args.id)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.bikeListResponse.observe(requireActivity(), Observer {
            lifecycleScope.launch {
                // filter by feature ID argument received
                Log.i("mapInfo", it.toString())

                feature = it?.features?.filter {
                    it.id == args.id

                }

                binding.label.text = feature?.get(0)?.properties?.label
                binding.noOfBike.text = feature?.get(0)?.properties?.bikes
                binding.noOfPlace.text = feature?.get(0)?.properties?.free_racks
                binding.distance.text = feature?.get(0)?.distance.toString()

                bikelatitude = feature?.get(0)?.geometry?.coordinates?.get(0)
                bikelongitude = feature?.get(0)?.geometry?.coordinates?.get(1)
                noOfBikes = feature?.get(0)?.properties?.bikes.toString()


            }
        })

    }


    override fun onMapReady(map: GoogleMap) {
        mMap = map
        //creating a bike location coordinate object and adding a marker
        val bikeLocation = bikelatitude?.let { bikelongitude?.let { it1 -> LatLng(it, it1) } }

        bikeLocation?.let {
            MarkerOptions().position(it).title(noOfBikes).icon(
                BitmapFromVector(requireContext(), R.drawable.ic_bike)
            )
        }?.let { mMap.addMarker(it) }
        mMap.isMyLocationEnabled = true

        bikeLocation?.let { CameraUpdateFactory.newLatLng(it) }?.let { mMap.moveCamera(it) }

        sharedViewModel.currentLocationCord.observe(requireActivity(), Observer {
            lifecycleScope.launch {
                if(it!=null){
                    val currentLocation = LatLng(it.latitude, it.longitude)
                    mMap.addMarker(MarkerOptions().position(currentLocation).title("Current Location"))
                }
            }
        })

    }

    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        // below line is use to set bounds to our vector drawable.
        vectorDrawable!!.setBounds(
            20,
            20,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        // below line is use to create a bitmap for our
        // drawable which we have added.
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas)

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }






}