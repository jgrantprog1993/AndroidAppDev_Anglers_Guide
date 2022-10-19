package ie.wit.anglersguide.main

import android.app.Application
import ie.wit.anglersguide.models.FishingSpotMemStore
import ie.wit.anglersguide.models.FishingSpotModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val fishingspots = FishingSpotMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Angler's Guide started")







    }
}