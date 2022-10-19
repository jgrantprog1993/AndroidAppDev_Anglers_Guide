package ie.wit.anglersguide.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import ie.wit.anglersguide.R

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, R.string.select_fishingspot_image.toString())
    intentLauncher.launch(chooseFile)
}