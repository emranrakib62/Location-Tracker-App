package com.example.googlemaps

import android.Manifest
import android.content.pm.PackageManager
import android.health.connect.datatypes.ExerciseRoute
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.example.googlemaps.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.PermissionRequest

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    var isgrantedpermission= MutableLiveData<Boolean>(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()

        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        // Permissions granted, enable location-related features if needed
                        isgrantedpermission.postValue(true)
                    }

                    if (report.isAnyPermissionPermanentlyDenied) {
                        // Redirect user to settings
                        isgrantedpermission.postValue(false)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap



        isgrantedpermission.observe(this){

            if(it){
                var location=getuserloaction()

                val sydney = LatLng(location?.latitude?:0.0,location?.longitude?:0.0)
                mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
                mMap.animateCamera(CameraUpdateFactory.newLatLng(sydney))
            }
        }
        // Add a marker in Sydney and move the camera

    }
    fun getuserloaction(): Location? {
        var location: Location?=null
        var bestlocation: Location?=null

        var locationManager=getSystemService(LOCATION_SERVICE) as LocationManager
val providers=locationManager.getProviders(true)
   for (provider in providers){
       if (ActivityCompat.checkSelfPermission(
               this,
               Manifest.permission.ACCESS_FINE_LOCATION
           ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
               this,
               Manifest.permission.ACCESS_COARSE_LOCATION
           ) != PackageManager.PERMISSION_GRANTED
       ) {
           return null
       }
       location=locationManager.getLastKnownLocation(provider)

       if(location==null){
           continue
       }
       if(bestlocation==null || location.accuracy>bestlocation.accuracy){
           bestlocation=location
       }


   }
        return bestlocation
    }
}
