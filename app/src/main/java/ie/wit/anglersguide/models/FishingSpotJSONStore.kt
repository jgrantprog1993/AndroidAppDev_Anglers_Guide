package ie.wit.anglersguide.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.anglersguide.helpers.exists
import ie.wit.anglersguide.models.FishingSpotModel
import ie.wit.anglersguide.models.FishingSpotStore
import ie.wit.anglersguide.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "fishingspots.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<FishingSpotModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class FishingSpotJSONStore(private val context: Context) : FishingSpotStore {

    var fishingspots = mutableListOf<FishingSpotModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<FishingSpotModel> {
        logAll()
        return fishingspots
    }

    override fun create(fishingspot: FishingSpotModel) {
        fishingspot.id = generateRandomId()
        fishingspots.add(fishingspot)
        serialize()
    }


    override fun update(fishingspot: FishingSpotModel) {
        val fishingspotsList = findAll() as ArrayList<FishingSpotModel>
        var foundFishingspot: FishingSpotModel? = fishingspotsList.find { p -> p.id == fishingspot.id }
        if (foundFishingspot != null) {
            foundFishingspot.title = fishingspot.title
            foundFishingspot.description = fishingspot.description
            foundFishingspot.image = fishingspot.image
            foundFishingspot.lat = fishingspot.lat
            foundFishingspot.lng = fishingspot.lng
            foundFishingspot.zoom = fishingspot.zoom
        }
        serialize()
    }

    override fun delete(fishingspot: FishingSpotModel) {
        fishingspots.remove(fishingspot)
        serialize()
    }


    private fun serialize() {
        val jsonString = gsonBuilder.toJson(fishingspots, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        fishingspots = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        fishingspots.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}