package ie.wit.anglersguide.models

import android.os.Parcel
import android.os.Parcelable

class SplashModel()
    : Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SplashModel> {
        override fun createFromParcel(parcel: Parcel): SplashModel {
            return SplashModel(parcel)
        }

        override fun newArray(size: Int): Array<SplashModel?> {
            return arrayOfNulls(size)
        }
    }
}