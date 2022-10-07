package com.my.dicoding_android_intermediate.ui.maps

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.my.dicoding_android_intermediate.R
import com.my.dicoding_android_intermediate.databinding.FragmentMapLocationBinding


class MapLocationFragment : Fragment(), OnMapReadyCallback, MenuProvider {

    lateinit var binding : FragmentMapLocationBinding
    lateinit var gMap : GoogleMap

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
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
//        setupMenu()
    }



//    private fun setupMenu() {
//        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
//            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                menuInflater.inflate(R.menu.map_options, menu)
//            }
//
//            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                return when (menuItem.itemId) {
//                    R.id.normal_type -> {
//                        gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
//                        true
//                    }
//                    R.id.satellite_type -> {
//                        gMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
//                        true
//                    }
//                    R.id.terrain_type -> {
//                        gMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
//                        true
//                    }
//                    R.id.hybrid_type -> {
//                        gMap.mapType = GoogleMap.MAP_TYPE_HYBRID
//                        true
//                    }
//                    else -> {
//                        return true
//                    }
//                }
//            }
//
//        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
//    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.uiSettings.isZoomControlsEnabled = true
        gMap.uiSettings.isIndoorLevelPickerEnabled = true
        gMap.uiSettings.isCompassEnabled = true
        gMap.uiSettings.isMapToolbarEnabled = true
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