package ie.wit.anglersguide.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser



interface FishingSpotStore{
    fun findAll(fishingspotsList:
                MutableLiveData<List<FishingSpotModel>>)
    fun findAll(userid:String,
                fishingSpotList: MutableLiveData<List<FishingSpotModel>>)
    fun findById(userid:String, fishingSpotId: String,
                 fishingspot: MutableLiveData<FishingSpotModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, fishingspot: FishingSpotModel)
    fun delete(userid:String, fishingspotid: String)
    fun update(userid:String, fishingspotid: String, fishingspot: FishingSpotModel)
}