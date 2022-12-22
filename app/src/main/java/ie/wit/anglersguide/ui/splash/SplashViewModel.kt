//package ie.wit.anglersguide.ui.splash
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import ie.wit.anglersguide.models.SplashModel
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//class SplashViewModel(application: Application) : AndroidViewModel(application) {
//
//    var liveData: MutableLiveData<SplashModel> = MutableLiveData()
//
//    fun initSplashScreen() {
//        viewModelScope.launch {
//            delay(2000)
//            updateLiveData()
//        }
//    }
//
//    private fun updateLiveData() {
//        val splashModel = SplashModel()
//        liveData.value = splashModel
//    }
//}