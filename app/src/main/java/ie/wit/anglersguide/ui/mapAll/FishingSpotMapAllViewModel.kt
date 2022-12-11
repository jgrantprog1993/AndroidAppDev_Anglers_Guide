package ie.wit.anglersguide.ui.mapAll

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.anglersguide.firebase.FirebaseDBManager
import ie.wit.anglersguide.models.FishingSpotModel
import timber.log.Timber
import java.lang.Exception

class FishingSpotMapAllViewModel : ViewModel() {

    private val fishingSpotList = MutableLiveData<List<FishingSpotModel>>()

    val observableMapsSpots: LiveData<List<FishingSpotModel>>
        get() = fishingSpotList

    init {
        load()
    }

    fun load() {
        try {
       FirebaseDBManager.findAll(fishingSpotList)
            Timber.i("Find Load Success : ${fishingSpotList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Find Loadß Error : $e.message")
        }
    }
}