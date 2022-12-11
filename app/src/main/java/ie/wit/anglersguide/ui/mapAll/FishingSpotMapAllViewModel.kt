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
import java.lang.Exception

class FishingSpotMapAllViewModel : ViewModel() {

    val fishingSpotList = MutableLiveData<List<FishingSpotModel>>()

    val observableMapsSpots: LiveData<List<FishingSpotModel>>
        get() = fishingSpotList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    lateinit var map: GoogleMap
    var currentLocation = MutableLiveData<Location>()

    init {
        load()
    }

    fun load() {
        try {
       FirebaseDBManager.findAll(fishingSpotList)
            Timber.i("Find Load Success : ${fishingSpotList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Find Load Error : $e.message")
        }
    }
}