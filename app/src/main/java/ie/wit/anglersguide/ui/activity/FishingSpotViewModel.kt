package ie.wit.anglersguide.ui.activity;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.anglersguide.firebase.FirebaseDBManager
import ie.wit.anglersguide.models.FishingSpotModel
import timber.log.Timber
import java.lang.Exception


class FishingSpotViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    private val fishingSpot =
        MutableLiveData<FishingSpotModel>()

    val observableFishingSpot: LiveData<FishingSpotModel>
        get() = fishingSpot

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init { load() }

    fun load() {
        try {
            //DonationManager.findAll(liveFirebaseUser.value?.email!!, donationsList)
            FirebaseDBManager.findById(liveFirebaseUser.value?.uid!!, observableFishingSpot.value?.uid!!, fishingSpot)
            Timber.i("FishingSpot Load Success : ${fishingSpot.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("FishingSpot Load Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            //DonationManager.delete(userid,id)
            FirebaseDBManager.delete(userid,id)
            Timber.i("fishingSpot Delete Success")
        }
        catch (e: Exception) {
            Timber.i("fishingSpots Delete Error : $e.message")
        }
    }

    fun getFishingSpot(userid:String, id: String) {
        try {
            //DonationManager.findById(email, id, donation)
            FirebaseDBManager.findById(userid, id, fishingSpot)
            Timber.i("FishingSpot getFishingSpot() Success : ${
                fishingSpot.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("FishingSpot getFishingSpot() Error : $e.message")
        }
    }

    fun updateFishingSpot(userid:String, id: String,fishingSpot: FishingSpotModel) {
        try {
            //DonationManager.update(email, id, donation)
            FirebaseDBManager.update(userid, id, fishingSpot)
            Timber.i("FishingSpot update() Success : $fishingSpot")
        }
        catch (e: Exception) {
            Timber.i("FishingSpot update() Error : $e.message")
        }
    }

    fun addFishingSpot(firebaseUser: MutableLiveData<FirebaseUser>,
                    fishingSpot: FishingSpotModel) {
        status.value = try {
            //DonationManager.create(donation)
            FirebaseDBManager.create(firebaseUser, fishingSpot)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}

