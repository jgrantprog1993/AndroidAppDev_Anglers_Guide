package ie.wit.anglersguide.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class FishingSpotMemStore : FishingSpotStore {

    val fishingspots = ArrayList<FishingSpotModel>()

    override fun findAll(): List<FishingSpotModel> {
        return fishingspots
    }

    override fun create(fishingspot: FishingSpotModel) {
        fishingspot.id = getId()
        fishingspots.add(fishingspot)
        logAll()
    }

    override fun update(fishingspot: FishingSpotModel) {
        var foundFishingspot: FishingSpotModel? = fishingspots.find { p -> p.id == fishingspot.id }
        if (foundFishingspot != null) {
            foundFishingspot.title = fishingspot.title
            foundFishingspot.description = fishingspot.description
            foundFishingspot.image = fishingspot.image
            logAll()
        }
    }

    fun logAll() {
        fishingspots.forEach{ i("${it}") }
    }
}