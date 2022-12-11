package ie.wit.anglersguide.ui.mapAll

import android.os.Bundle
import androidx.fragment.app.Fragment
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.anglersguide.R
import ie.wit.anglersguide.databinding.FragmentFishingSpotListBinding
import ie.wit.anglersguide.firebase.FirebaseDBManager
//import ie.wit.anglersguide.models.FishingSpotManager.findAll
import timber.log.Timber


class MapAllFishingSpotsFragment : Fragment(), OnMapReadyCallback,
                                            GoogleMap.OnMarkerClickListener{

    private lateinit var fishingSpotMapAllViewModel: FishingSpotMapAllViewModel
    private var _fragBinding: FragmentFishingSpotListBinding? = null
    private val fragBinding get() = _fragBinding!!

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("Geting into 2")
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_map_all_fishing_spots, container, false)

        mapFragment =
            childFragmentManager.findFragmentById(R.id.mapAllMarkers) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        fishingSpotMapAllViewModel =
            ViewModelProvider(this).get(FishingSpotMapAllViewModel::class.java)
        fishingSpotMapAllViewModel.observableMapsSpots.observe(
            viewLifecycleOwner,
            Observer { render() })
        return root
    }
    private fun render(/*donation: DonationModel*/) {
        // fragBinding.editAmount.setText(donation.amount.toString())
        // fragBinding.editPaymenttype.text = donation.paymentmethod
       // fragBinding.editMessage.setText("A Message")
//        fragBinding.editUpvotes.setText("0")
//        fragBinding.donationvm = detailViewModel
    }
       override fun onMapReady(googleMap: GoogleMap) {
//            map = googleMap!!
//            for (location in FirebaseDBManager.findAll()) {
//                val loc = LatLng(location.lat, location.lng)
//                val locTitle = location.title
//                val locLat = location.lat
//                val locLng = location.lng
//                val options = MarkerOptions()
//                    .title("Name : $locTitle")
//                    .snippet("Lat, Lng : $locLat, $locLng")
//                    .draggable(true)
//                    .position(loc)
//                map.addMarker(options)
//                map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
//
//            }
      }

        override fun onResume() {
            super.onResume()
//            FishingSpotManager.findAll()
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _fragBinding = null
        }

        companion object {
            var mapFragment : SupportMapFragment?=null

            @JvmStatic
            fun newInstance() =
                MapAllFishingSpotsFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
        }

    override fun onMarkerClick(p0: Marker): Boolean {
        TODO("Not yet implemented")
    }
}
