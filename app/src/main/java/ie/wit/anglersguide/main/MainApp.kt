package ie.wit.anglersguide.main

//import ie.wit.anglersguide.models.FishingSpotJSONStore
//import ie.wit.anglersguide.models.FishingSpotMemStore
import android.app.Application
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    //lateinit var fishingspots: FishingSpotStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        i("Angler's Guide started")

    }
}