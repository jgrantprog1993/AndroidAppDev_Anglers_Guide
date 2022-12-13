package ie.wit.anglersguide.ui.detail

import ie.wit.anglersguide.ui.detail.FishingSpotDetailViewModel
import LoggedInViewModel
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.wit.anglersguide.ui.activity.FishingSpotListViewModel

import ie.wit.anglersguide.databinding.FragmentUpdateFishingSpotBinding
import timber.log.Timber


class FishingSpotDetailFragment : Fragment() {

    private lateinit var fishingSpotDetailViewModel: FishingSpotDetailViewModel
    private val args by navArgs<FishingSpotDetailFragmentArgs>()
    private var _fragBinding: FragmentUpdateFishingSpotBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val fishingSpotListViewModel : FishingSpotListViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentUpdateFishingSpotBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        fishingSpotDetailViewModel = ViewModelProvider(this).get(FishingSpotDetailViewModel::class.java)
        fishingSpotDetailViewModel.observableFishingSpot.observe(viewLifecycleOwner, Observer { render() })
        Toast.makeText(context,"FishingSpot ID Selected : ${args.fishingspotId}", Toast.LENGTH_LONG).show()
        fragBinding.editFishingSpotButton.setOnClickListener {
            fishingSpotDetailViewModel.updateFishingSpot(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.fishingspotId, fragBinding.fishingspotvm?.observableFishingSpot!!.value!!)
            findNavController().navigateUp()
        }

        fragBinding.deleteFishingSpotButton.setOnClickListener {
            fishingSpotListViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                fishingSpotDetailViewModel.observableFishingSpot.value?.uid!!)
            findNavController().navigateUp()
        }

        return root
    }

    private fun render() {
        fragBinding.editTitle.setText("title")
        fragBinding.editDesc.setText("A Message")
        fragBinding.editLat.setText("0")
        fragBinding.editLng.setText("0")
        fragBinding.fishingspotvm = fishingSpotDetailViewModel
        Timber.i(" fragBinding.fishingspotvm == $fragBinding.fishingspotvm")
    }

    override fun onResume() {
        super.onResume()
        fishingSpotDetailViewModel.getFishingSpot(loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.fishingspotId)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}