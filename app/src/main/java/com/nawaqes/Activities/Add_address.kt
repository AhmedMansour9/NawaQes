package com.nawaqes.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.places.PlaceBuffer
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.nawaqes.Adapter.PlaceAutocompleteAdapter
import com.nawaqes.Model.PlaceInfo
import com.nawaqes.R
import kotlinx.android.synthetic.main.activity_add_address.*
import java.lang.Double

class Add_address : AppCompatActivity() , OnMapReadyCallback,com.google.android.gms.location.LocationListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
   lateinit var marker:Marker
    lateinit var googleMap: GoogleMap
    lateinit var locationReques: LocationRequest
     var status:Boolean=false
    lateinit var placeT: PlaceInfo
    lateinit var placeAutocompleteAdapter: PlaceAutocompleteAdapter
    lateinit var latlongplace: LatLng
    lateinit var latlaang: LatLng
     var latLngBounds: LatLngBounds? = null

    var mGoogleApiClient: GoogleApiClient? = null
     var MY_PERMISSIONS_REQUEST_LOCATION = 99
     val REQUEST_LOCATION_CODE = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        init()
        checkLocationPermission()

    }
    fun init(){
        val mapFragment = fragmentManager.findFragmentById(R.id.MAP) as MapFragment
        mapFragment?.getMapAsync(this)
    }


    override fun onMapReady(p0: GoogleMap?) {
      buildGoogleapiclint()
        googleMap=p0!!

    }

    override fun onLocationChanged(p0: Location?) {
        val l = LatLng(
            p0!!.latitude,
        p0!!.longitude)
        if(!status) {
            val markerOptions = MarkerOptions()
            markerOptions.position(l)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker))
            marker = googleMap.addMarker(markerOptions)
            val currentPlace = CameraPosition.Builder()
                .target(l)
                .bearing(240f).tilt(30f).zoom(14f).build()
            googleMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    currentPlace
                )
            )
        }

    }

    override fun onConnected(p0: Bundle?) {
        locationReques = LocationRequest()
        locationReques.smallestDisplacement = 1f
        locationReques.interval = 1000
//        locationReques.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        val permissionCheck = ContextCompat.checkSelfPermission(
            this!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                locationReques,
                this
            )
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationReques)
            val client = LocationServices.getSettingsClient(this!!)
            val task = client.checkLocationSettings(builder.build())
            task.addOnSuccessListener(this!!,
                OnSuccessListener<LocationSettingsResponse> { })

            task.addOnFailureListener(this!!, OnFailureListener { e ->
                if (e is ResolvableApiException) {
                    try {
                        e.startResolutionForResult(
                            this,
                            REQUEST_LOCATION_CODE
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }

                }
            })
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
            return false
        } else {
            return true
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_LOCATION_CODE -> when (resultCode) {
                Activity.RESULT_OK -> {
                    buildGoogleapiclint()
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                }
                Activity.RESULT_CANCELED -> {
                }
                else -> {
                }
            }// The user was asked to change settings, but chose not to
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val fragments = supportFragmentManager.fragments
        when (requestCode) {
            99 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this!!,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        buildGoogleapiclint()
                    }

                } else {
                }
                return
            }
        }
    }

    @Synchronized
    private fun buildGoogleapiclint() {
                mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .enableAutoManage(this,this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build()
                mGoogleApiClient!!.connect()

        placeAutocompleteAdapter =
            PlaceAutocompleteAdapter(this, mGoogleApiClient, latLngBounds, null)
        E_SearchPlace.setAdapter(placeAutocompleteAdapter)
        E_SearchPlace .setOnItemClickListener(mAutocomplete)

    }


    private val updateplaceCallback =
        ResultCallback<PlaceBuffer> { places ->
            if (!places.status.isSuccess) {
                places.release()
                return@ResultCallback
            }
            val place = places.get(0)
            placeT = PlaceInfo()
            try {
                placeT.setName(place.name.toString())
                placeT.setAddress(place.address!!.toString())
                placeT.setPhoneNumber(place.phoneNumber!!.toString())
                placeT.setId(place.id)
                placeT.setLatlong(place.latLng)
                placeT.setRating(place.rating)

                val currentPlace = CameraPosition.Builder()
                    .target(place.latLng)
                    .bearing(240f).tilt(30f).zoom(16f).build()
                googleMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        currentPlace
                    )
                )

            } catch (e: NullPointerException) {
            }
            places.release()
            latlaang = placeT.getLatlong()!!

        }

    private val mAutocomplete =
        AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val autocompletePrediction = placeAutocompleteAdapter.getItem(i)
            val id = autocompletePrediction!!.getPlaceId()
            val place = Places.GeoDataApi.getPlaceById(mGoogleApiClient!!, id!!)
            place.setResultCallback(updateplaceCallback)
        }

}
