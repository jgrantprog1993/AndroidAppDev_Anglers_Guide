package ie.wit.anglersguide.models


interface FishingSpotStore{
    fun findAll(): MutableList<FishingSpotModel>
    fun create(fishingspot: FishingSpotModel)
    fun update(fishingspot: FishingSpotModel) {}
    fun delete (fishingspot: FishingSpotModel) {}
}