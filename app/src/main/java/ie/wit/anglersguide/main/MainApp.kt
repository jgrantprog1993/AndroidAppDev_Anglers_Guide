package ie.wit.anglersguide.main

import android.app.Application
import ie.wit.anglersguide.models.FishingSpotJSONStore
import ie.wit.anglersguide.models.FishingSpotMemStore
import ie.wit.anglersguide.models.FishingSpotModel
import ie.wit.anglersguide.models.FishingSpotStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var fishingspots: FishingSpotStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        fishingspots = FishingSpotJSONStore(applicationContext)
        i("Angler's Guide started")

    }
}