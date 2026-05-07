package com.flosun.pomodoro.core.services

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.dropUnlessResumed
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String? = null,
)

@Singleton
class LocationService @Inject constructor(
    private val application: Application,
) : CoroutineService(Dispatchers.IO) {
    private val geocoder = Geocoder(application, Locale.getDefault())
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _location = MutableStateFlow<Location?>(null)
    val location = _location.asStateFlow()

    init {
        initialize()
    }

    fun initialize() {
        coroutineScope.launch {
            try {
                val lastLocation = retrieveLastLocation()
                _location.value = lastLocation
            } catch (e: Exception) {
                // Handle exceptions (e.g., permissions not granted, location services disabled)
                e.printStackTrace()
            }
        }
    }

    fun findAddressBaseOnLngLat(latitude: Double, longitude: Double): String? {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        val address = addresses?.firstOrNull()?.getAddressLine(0)
        return address
    }

    suspend fun retrieveLastLocation(): Location = suspendCancellableCoroutine { continuation ->
        if (ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            continuation.resumeWithException(SecurityException("Location permission not granted"))
            return@suspendCancellableCoroutine
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val address = addresses?.firstOrNull()?.getAddressLine(0)

                val result = Location(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    address = address,
                )

                continuation.resume(result)
            }
            .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
    }
}