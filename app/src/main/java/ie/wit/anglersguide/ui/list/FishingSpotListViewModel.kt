package ie.wit.anglersguide.ui.activity;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.anglersguide.firebase.FirebaseDBManager
import ie.wit.anglersguide.models.FishingSpotModel
import timber.log.Timber
import java.lang.Exception


class FishingSpotListViewModel : ViewModel() {

    private val fishingSpotsList =
        MutableLiveData<List<FishingSpotModel>>()

    val observableFishingSpotsList: LiveData<List<FishingSpotModel>>
        get() = fishingSpotsList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    var readOnly = MutableLiveData(false)

    init { load() }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(fishingSpotsList)
            Timber.i("Report LoadAll Success : ${fishingSpotsList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }

    fun load() {
        readOnly.value = false
        try {
            //DonationManager.findAll(liveFirebaseUser.value?.email!!, donationsList)
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!,fishingSpotsList)
            Timber.i("Load Success : ${fishingSpotsList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Load Error : $e.message")
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
}

