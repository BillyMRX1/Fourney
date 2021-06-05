package com.bearbrand.fourney.ui.challenge

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bearbrand.fourney.databinding.FragmentChallengeBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar


class ChallengeFragment : Fragment() {

    private var _binding: FragmentChallengeBinding? = null
    private val binding get() = _binding!!
    private lateinit var geofencingClient: GeofencingClient
    private val runningQOrLater = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChallengeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        geofencingClient = LocationServices.getGeofencingClient(requireActivity())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TURN_DEVICE_LOCATION_ON) {
            checkDeviceLocationSettingsAndStartGeofence(false)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        // TODO: Step 5 add code to handle the result of the user's permission
        Log.d(TAG, "onRequestPermissionResult")

        if (
            grantResults.isEmpty() ||
            grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
            (requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE &&
                    grantResults[BACKGROUND_LOCATION_PERMISSION_INDEX] ==
                    PackageManager.PERMISSION_DENIED))
        {
            Toast.makeText(
                context,
                "Izinkan lokasi untuk menggunakan aplikasi ini.",
                Toast.LENGTH_SHORT
            ).apply {
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", "com.bearbrand.fourney", null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }
        } else {
            checkDeviceLocationSettingsAndStartGeofence()
        }
    }

    private fun checkPermissionsAndStartGeofencing() {
        if (foregroundAndBackgroundLocationPermissionApproved()) {
            checkDeviceLocationSettingsAndStartGeofence()
        } else {
            requestForegroundAndBackgroundLocationPermissions()
        }
    }

    private fun checkDeviceLocationSettingsAndStartGeofence(resolve:Boolean = true) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val locationSettingsResponseTask =
            settingsClient.checkLocationSettings(builder.build())
        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve){
                try {
                    exception.startResolutionForResult(requireActivity(),
                        REQUEST_TURN_DEVICE_LOCATION_ON)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(TAG, "Error getting location settings resolution: " + sendEx.message)
                }
            } else {
                Toast.makeText(
                    context,
                    "Layanan lokasi harus aktif untuk menggunakan aplikasi.",
                    Toast.LENGTH_SHORT
                ).apply {
                    checkDeviceLocationSettingsAndStartGeofence()
                }
            }
        }
        locationSettingsResponseTask.addOnCompleteListener {
            if ( it.isSuccessful ) {
                addGeofence()
            }
        }
    }

    @TargetApi(29)
    private fun foregroundAndBackgroundLocationPermissionApproved(): Boolean {
        val foregroundLocationApproved = (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION))
        val backgroundPermissionApproved =
            if (runningQOrLater) {
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }

    @TargetApi(29 )
    private fun requestForegroundAndBackgroundLocationPermissions() {
        if (foregroundAndBackgroundLocationPermissionApproved())
            return
        var permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        val resultCode = when {
            runningQOrLater -> {
                permissionsArray += Manifest.permission.ACCESS_BACKGROUND_LOCATION
                REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
            }
            else -> REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        }
        Log.d(TAG, "Request foreground only location permission")
        ActivityCompat.requestPermissions(
            requireActivity(),
            permissionsArray,
            resultCode
        )
    }

    private fun addGeofence() {
        // TODO: Step 10 add in code to add the geofence
//        if (viewModel.geofenceIsActive()) return
//        val currentGeofenceIndex = viewModel.nextGeofenceIndex()
//        if(currentGeofenceIndex >= GeofencingConstants.NUM_LANDMARKS) {
//            removeGeofences()
//            viewModel.geofenceActivated()
//            return
//        }
//        val currentGeofenceData = GeofencingConstants.LANDMARK_DATA[currentGeofenceIndex]

//        val geofence = Geofence.Builder()
//            .setRequestId(currentGeofenceData.id)
//            .setCircularRegion(currentGeofenceData.latLong.latitude,
//                currentGeofenceData.latLong.longitude,
//                GeofencingConstants.GEOFENCE_RADIUS_IN_METERS
//            )
//            .setExpirationDuration(GeofencingConstants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
//            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
//            .build()
//
//        val geofencingRequest = GeofencingRequest.Builder()
//            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
//            .addGeofence(geofence)
//            .build()
//
//        geofencingClient.removeGeofences(geofencePendingIntent)?.run {
//            addOnCompleteListener {
//                geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)?.run {
//                    addOnSuccessListener {
//                        Toast.makeText(this@HuntMainActivity, R.string.geofences_added,
//                            Toast.LENGTH_SHORT)
//                            .show()
//                        Log.e("Add Geofence", geofence.requestId)
//                        viewModel.geofenceActivated()
//                    }
//                    addOnFailureListener {
//                        Toast.makeText(this@HuntMainActivity, R.string.geofences_not_added,
//                            Toast.LENGTH_SHORT).show()
//                        if ((it.message != null)) {
//                            Log.w(TAG, it.message)
//                        }
//                    }
//                }
//            }
//        }
    }

    private fun removeGeofences() {
//        if (!foregroundAndBackgroundLocationPermissionApproved()) {
//            return
//        }
//        geofencingClient.removeGeofences(geofencePendingIntent)?.run {
//            addOnSuccessListener {
//                Log.d(TAG, getString(R.string.geofences_removed))
//                Toast.makeText(applicationContext, R.string.geofences_removed, Toast.LENGTH_SHORT)
//                    .show()
//            }
//            addOnFailureListener {
//                Log.d(TAG, getString(R.string.geofences_not_removed))
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
        private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
        private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
        private const val TAG = "ChallengeFragment"
        private const val LOCATION_PERMISSION_INDEX = 0
        private const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1
    }
}