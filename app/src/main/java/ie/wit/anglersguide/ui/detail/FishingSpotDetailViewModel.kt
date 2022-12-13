package ie.wit.anglersguide.ui.detail
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.anglersguide.firebase.FirebaseDBManager
import ie.wit.anglersguide.models.FishingSpotModel

import timber.log.Timber

class FishingSpotDetailViewModel : ViewModel() {
    private val fishingspot = MutableLiveData<FishingSpotModel>()

    var observableFishingSpot: LiveData<FishingSpotModel>
        get() = fishingspot
        set(value) {fishingspot.value = value.value}

    fun getFishingSpot(userid:String, id: String) {
        try {
            //DonationManager.findById(email, id, donation)
            FirebaseDBManager.findById(userid, id, fishingspot)
            Timber.i("Detail getFishingSpot() Success : ${
                fishingspot.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getFishingSpot() Error : $e.message")
        }
    }

    fun updateFishingSpot(userid:String, id: String,fishingspot: FishingSpotModel) {
        try {

            FirebaseDBManager.update(userid, id, fishingspot)
            Timber.i("Detail update() Success : $fishingspot")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}