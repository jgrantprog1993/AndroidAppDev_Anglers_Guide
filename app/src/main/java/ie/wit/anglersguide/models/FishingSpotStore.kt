package ie.wit.anglersguide.models


interface FishingSpotStore{
    fun findAll(): List<FishingSpotModel>
    fun create(fishingspot: FishingSpotModel)
    fun update(fishingspot: FishingSpotModel) {}
}