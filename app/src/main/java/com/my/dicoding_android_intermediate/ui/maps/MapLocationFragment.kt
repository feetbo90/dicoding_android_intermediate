package com.my.dicoding_android_intermediate.ui.maps

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.my.dicoding_android_intermediate.R
import com.my.dicoding_android_intermediate.data.remote.response.StoryResponse
import com.my.dicoding_android_intermediate.data.remote.response.StoryResponseItem
import com.my.dicoding_android_intermediate.data.result.MyResult
import com.my.dicoding_android_intermediate.databinding.FragmentMapLocationBinding
import com.my.dicoding_android_intermediate.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapLocationFragment : Fragment(), OnMapReadyCallback, MenuProvider {

    lateinit var binding: FragmentMapLocationBinding
    lateinit var gMap: GoogleMap
    private val mapLocationViewModel: MapLocationViewModel by viewModels()
    private var token: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        token = requireActivity().intent.getStringExtra(Utils.TOKEN) ?: ""

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setViewMapModel()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            gMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setViewMapModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            launch {
                mapLocationViewModel.stories.collect { result ->
                    onFollowsResultReceived(result)
                }
            }
            launch {
                mapLocationViewModel.isLoaded.collect { loaded ->
                    if (!loaded) {
                        mapLocationViewModel.getMapStories(token)
                    }
                }
            }
        }
    }

    private fun onFollowsResultReceived(result: MyResult<StoryResponse>) {
        when (result) {
            is MyResult.Success -> {
                showStories(result.data.storyResponseItems)
            }
            else -> {}
        }
    }

    private fun showStories(stories: ArrayList<StoryResponseItem>) {
        if (stories.size > 0) {
            for (i in 0 until stories.size) {
                val place = LatLng(stories[i].lat!!, stories[i].lon!!)
                gMap.addMarker(
                    MarkerOptions().position(place).title(stories[i].name)
                        .snippet(stories[i].description)
                )
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.uiSettings.isZoomControlsEnabled = true
        gMap.uiSettings.isIndoorLevelPickerEnabled = true
        gMap.uiSettings.isCompassEnabled = true
        gMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()
        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            val success =
                gMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.map_style
                    )
                )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.map_options, menu)
    }


    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.normal_type -> {
                gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                gMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                gMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                gMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                return true
            }
        }
    }
}