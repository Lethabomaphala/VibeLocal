package com.vibelocal.app.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.cardview.widget.CardView
import android.graphics.BitmapFactory
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.vibelocal.app.R
import com.vibelocal.app.api.ApiClient
import com.vibelocal.app.model.EventDto
import com.vibelocal.app.util.MockData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapActivity : BaseActivity() {

    private lateinit var mapView: MapView

    private var currentLocation: Point? = null

    private var hasLoadedNearbyEvents = false

    /**
     * Receives the user's location directly from Mapbox.
     */
    private val onIndicatorPositionChangedListener =
        OnIndicatorPositionChangedListener { point ->

            currentLocation = point

            /*
             * Only load nearby events once when the
             * first valid location is received.
             */
            if (!hasLoadedNearbyEvents) {

                hasLoadedNearbyEvents = true

                moveCameraToLocation(point)

                loadNearbyEvents(
                    latitude = point.latitude(),
                    longitude = point.longitude()
                )
            }
        }

    /**
     * Requests location permission from the user.
     */
    private val locationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val fineLocationGranted =
                permissions[
                    Manifest.permission.ACCESS_FINE_LOCATION
                ] == true

            val coarseLocationGranted =
                permissions[
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ] == true

            if (fineLocationGranted || coarseLocationGranted) {

                enableLocation()

            } else {

                Toast.makeText(
                    this,
                    "Location permission was not granted.",
                    Toast.LENGTH_LONG
                ).show()

                loadAllEvents()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Ensure Mapbox is initialized with the token
        MapboxOptions.accessToken = getString(R.string.mapbox_access_token)

        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.mapView)

        /*
         * Load Mapbox's standard map style.
         */
        mapView.mapboxMap.loadStyle(Style.MAPBOX_STREETS) {
            
            // Add marker icon to style
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.vibelocal_logo)
            it.addImage("custom-marker", bitmap)

            checkLocationPermission()
        }

        /*
         * Locate-me button.
         */
        findViewById<CardView>(
            R.id.btnMyLocation
        ).setOnClickListener {
            // ...
        }

        setupBottomNav(findViewById(R.id.bottomNavigation), R.id.nav_map)
    }

    /**
     * Check whether location permissions have
     * already been granted.
     */
    private fun checkLocationPermission() {

        val fineLocationGranted =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (fineLocationGranted || coarseLocationGranted) {

            enableLocation()

        } else {

            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    /**
     * Enable Mapbox's built-in location component.
     */
    private fun enableLocation() {

        mapView.location.apply {

            enabled = true

            pulsingEnabled = true

            addOnIndicatorPositionChangedListener(
                onIndicatorPositionChangedListener
            )
        }
    }

    /**
     * Move the camera to the user's current location.
     */
    private fun moveCameraToLocation(point: Point) {

        val cameraPosition =
            CameraOptions.Builder()
                .center(point)
                .zoom(12.5)
                .build()

        mapView.mapboxMap.setCamera(cameraPosition)
    }

    /**
     * Search for events within 25 km of
     * the user's current location.
     */
    private fun loadNearbyEvents(
        latitude: Double,
        longitude: Double
    ) {

        lifecycleScope.launch {

            try {

                /*
                 * Use YOUR actual ApiClient structure.
                 */
                val api = ApiClient.service(
                    this@MapActivity
                )

                /*
                 * Network request runs on IO.
                 */
                val events = withContext(Dispatchers.IO) {

                    api.search(
                        latitude = latitude,
                        longitude = longitude,
                        distanceKm = 25.0
                    )
                }

                /*
                 * Display number of nearby events.
                 */
                Toast.makeText(
                    this@MapActivity,
                    "${events.size} nearby events found",
                    Toast.LENGTH_SHORT
                ).show()

                showEventsOnMap(events)

            } catch (e: Exception) {
                // Fallback to local events if server is offline
                val events = MockData.events
                showEventsOnMap(events)
                
                Toast.makeText(
                    this@MapActivity,
                    "Using local data (${events.size} events)",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showEventsOnMap(events: List<EventDto>) {
        val annotationApi = mapView.annotations
        val pointAnnotationManager = annotationApi.createPointAnnotationManager()
        pointAnnotationManager.deleteAll()

        events.forEach { event ->
            val point = Point.fromLngLat(event.longitude, event.latitude)
            val pointAnnotationOptions = PointAnnotationOptions()
                .withPoint(point)
                .withIconImage("custom-marker")
                .withIconSize(0.1) // Resize the logo to be marker-sized
            
            pointAnnotationManager.create(pointAnnotationOptions)
        }

        pointAnnotationManager.addClickListener { annotation ->
            val event = events.find { 
                it.latitude == annotation.point.latitude() && 
                it.longitude == annotation.point.longitude() 
            }
            if (event != null) {
                showEventPreview(event)
            }
            true
        }
    }

    private fun showEventPreview(event: EventDto) {
        findViewById<CardView>(R.id.eventPreviewCard).visibility = View.VISIBLE
        findViewById<TextView>(R.id.txtMapEventTitle).text = event.title
        findViewById<TextView>(R.id.txtMapEventLocation).text = event.location
        findViewById<TextView>(R.id.txtMapEventPrice).text = if (event.price <= 0) "Free" else "R${event.price.toInt()}"
        
        findViewById<CardView>(R.id.eventPreviewCard).setOnClickListener {
            startActivity(Intent(this, EventDetailsActivity::class.java).putExtra("eventId", event.eventId))
        }
    }

    /**
     * Load all events when location isn't available.
     */
    private fun loadAllEvents() {

        lifecycleScope.launch {

            try {

                /*
                 * Use YOUR actual ApiClient structure.
                 */
                val api = ApiClient.service(
                    this@MapActivity
                )

                /*
                 * Get all events from the API.
                 */
                val events = withContext(Dispatchers.IO) {

                    api.events()
                }

                Toast.makeText(
                    this@MapActivity,
                    "${events.size} events loaded",
                    Toast.LENGTH_SHORT
                ).show()
                
                showEventsOnMap(events)

            } catch (e: Exception) {
                val events = MockData.events
                showEventsOnMap(events)
                
                Toast.makeText(
                    this@MapActivity,
                    "Using local data (${events.size} events)",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Mapbox lifecycle.
     */
    override fun onStart() {
        super.onStart()

        mapView.onStart()
    }

    override fun onStop() {

        mapView.onStop()

        super.onStop()
    }

    override fun onDestroy() {

        /*
         * Stop receiving location updates.
         */
        mapView.location
            .removeOnIndicatorPositionChangedListener(
                onIndicatorPositionChangedListener
            )

        mapView.onDestroy()

        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()

        mapView.onLowMemory()
    }
}