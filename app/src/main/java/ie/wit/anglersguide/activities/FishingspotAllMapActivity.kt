package ie.wit.anglersguide.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.anglersguide.R
import ie.wit.anglersguide.databinding.ActivityAllmarkersmapBinding
import ie.wit.anglersguide.databinding.ActivityFishingspotBinding
import ie.wit.anglersguide.databinding.ActivityFishingspotListBinding

import ie.wit.anglersguide.databinding.ActivityMapBinding
import ie.wit.anglersguide.main.MainApp
import ie.wit.anglersguide.models.FishingSpotJSONStore
import ie.wit.anglersguide.models.FishingSpotModel
import ie.wit.anglersguide.models.Location
import timber.log.Timber.i

class FishingspotAllMapActivity : AppCompatActivity(), OnMapReadyCallback,
                                                    GoogleMap.OnMarkerClickListener{


    private lateinit var map: GoogleMap
    var location = Location()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allmarkersmap)
        app = application as MainApp
        //location = intent.extras?.getParcelable<Location>("location")!!
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapAllMarkers) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        for (location in app.fishingspots.findAll()) {
            val loc = LatLng(location.lat, location.lng)
            val locTitle = location.title
            val locLat = location.lat
            val locLng = location.lng
            val options = MarkerOptions()
                .title("Name : $locTitle")
                .snippet("Lat, Lng : $locLat, $locLng")
                .draggable(true)
                .position(loc)
            map.addMarker(options)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
            map.setOnMarkerClickListener(this)

        }

    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//    }
    override fun onMarkerClick(marker: Marker): Boolean {

        //val loc = LatLng(marker.)
       // marker.snippet = "GPS : $loc"
        return false
    }
}