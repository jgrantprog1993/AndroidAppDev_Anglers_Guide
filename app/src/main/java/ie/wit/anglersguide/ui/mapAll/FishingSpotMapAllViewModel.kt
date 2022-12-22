package ie.wit.anglersguide.ui.mapAll

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.auth.FirebaseUser
import ie.wit.anglersguide.firebase.FirebaseDBManager
import ie.wit.anglersguide.models.FishingSpotModel
import ie.wit.anglersguide.models.Location
import timber.log.Timber

class FishingSpotMapAllViewModel : ViewModel() {

    val fishingSpotsList = MutableLiveData<List<FishingSpotModel>>()

    val observableMapsSpots: LiveData<List<FishingSpotModel>>
        get() = fishingSpotsList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    lateinit var map: GoogleMap
    var currentLocation = MutableLiveData<Location>()

    init {
        load()
    }
    fun loadAll() {
        try {

            FirebaseDBManager.findAll(fishingSpotsList)
            Timber.i("Report LoadAll Success : ${fishingSpotsList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }
    fun load() {

        try {

            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!,fishingSpotsList)
            Timber.i("Load Success : ${fishingSpotsList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Load Error : $e.message")
        }
    }
}