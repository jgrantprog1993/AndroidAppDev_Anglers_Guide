package ie.wit.anglersguide.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.wit.anglersguide.models.FishingSpotModel
import ie.wit.anglersguide.models.FishingSpotStore
import timber.log.Timber

object FirebaseDBManager : FishingSpotStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference


    override fun findAll(fishingspotsList: MutableLiveData<List<FishingSpotModel>>) {
        database.child("fishingspots")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase FishingSpot error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<FishingSpotModel>()
                    val children = snapshot.children
                    children.forEach {
                        val fishingspot = it.getValue(FishingSpotModel::class.java)
                        localList.add(fishingspot!!)
                    }
                    database.child("fishingspots")
                        .removeEventListener(this)

                    fishingspotsList.value = localList
                }
            })
    }

    override fun findAll(userid: String, fishingspotsList: MutableLiveData<List<FishingSpotModel>>) {

        database.child("user-fishingspots").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Fishingspot error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<FishingSpotModel>()
                    val children = snapshot.children
                    children.forEach {
                        val fishingspot = it.getValue(FishingSpotModel::class.java)
                        localList.add(fishingspot!!)
                    }
                    database.child("user-fishingspots").child(userid)
                        .removeEventListener(this)

                    fishingspotsList.value = localList
                }
            })
    }

    override fun findById(userid: String, fishingspotid: String, fishingspot: MutableLiveData<FishingSpotModel>) {

        database.child("user-fishingspots").child(userid)
            .child(fishingspotid).get().addOnSuccessListener {
                fishingspot.value = it.getValue(FishingSpotModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, fishingspot: FishingSpotModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("fishingspots").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        fishingspot.uid = key
        val fishingspotValues = fishingspot.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/fishingspots/$key"] = fishingspotValues
        childAdd["/user-fishingspots/$uid/$key"] = fishingspotValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, fishingspotid: String) {

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/fishingspots/$fishingspotid"] = null
        childDelete["/user-fishingspots/$userid/$fishingspotid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, fishingspotid: String, fishingspot: FishingSpotModel) {

        val fishingspotValues = fishingspot.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["fishingspots/$fishingspotid"] = fishingspotValues
        childUpdate["user-fishingspots/$userid/$fishingspotid"] = fishingspotValues

        database.updateChildren(childUpdate)
    }

    fun updateImageRef(userid: String,imageUri: String) {

        val userDonations = database.child("user-donations").child(userid)
        val allDonations = database.child("donations")

        userDonations.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        //Update Users imageUri
                        it.ref.child("profilepic").setValue(imageUri)
                        //Update all fishingspots that match 'it'
                        val donation = it.getValue(FishingSpotModel::class.java)
                        allDonations.child(donation!!.uid!!)
                            .child("profilepic").setValue(imageUri)
                    }
                }
            })
    }
}