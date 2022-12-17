package ie.wit.anglersguide.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class FishingSpotModel(var uid: String? = "",
                          var title: String = "",
                          var description: String = "",
                          var image: String = "",
                          var lat : Double = 0.0,
                          var lng: Double = 0.0,
                          var zoom: Float = 0f,
                            var profilepic: String = "",
                            var email: String = "joe@bloggs.com")
                            : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "title" to title,
            "description" to description,
            "image" to image,
            "lat" to lat,
            "lng" to lng,
            "zoom" to zoom,
            "profilepic" to profilepic,
            "email" to email
        )
    }
}



@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 14f) : Parcelable