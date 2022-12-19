# __Angler's Guide App__ Ass2_Release
## New Features
- MVVM Architecture
- Swipe to Edit
- Google Sign-in Auth
- Toggle Button on List view for logged in User locations / All locations
- Show on Map Users locations on map (via toggle)
- Show all locations on map (via toggle)

## Features to ADD
- Filtering Cards view
- Mark As Fav
- Show Favs on Map

## Items to Fix
- Set Location


## References
 - Color for Markers (https://stackoverflow.com/questions/49566693/how-to-change-the-google-maps-marker-color-to-grey-in-android)
 - Clear Markers from Map (https://stackoverflow.com/questions/16853182/android-how-to-remove-all-markers-from-google-map-v2)
# __Angler's Guide App__ Ass1_Release
__This is an app developed in Kotlin (Android Studio)__
![img.png](img.png)
![img_1.png](img_1.png)
![img_2.png](img_2.png)
![img_3.png](img_3.png)
![img_4.png](img_4.png)
![img_5.png](img_5.png)
![img_6.png](img_6.png)

## Current Features
- Add Fishing Spot (Title, Desc, Image)
- Show via Card
- Edit Fishing Spot (Title, Desc, Image)
- Delete Fishing Spot (with Swipe Functionality)
- Temp Storage
- Google Maps Feature
- Persistence via json data in file
- Take photo from app and add to FishingSpot image
- User Register / Login / Signout 
- Authentication via FireBase

## Features to ADD
- UI Upgrades
- Categories in the Location Model 'Sea / Freshwater etc' (help with filter later)
- Filtering
- Testing
- Undo when deleted 
- About Us Page

## Items to Fix
- Update Bin/Delete Graphic in Delete Swipe
- Implement option to undo delete just after swiping.
- Image view size after taking picture
- Show Logged in User on Nav Drawer


## References 
- Set Result in fragment -> https://stackoverflow.com/questions/36495842/is-there-a-method-like-setresult-in-fragment
- Fragment to not save text -> https://stackoverflow.com/questions/59586068/why-does-edittext-in-fragment-show-the-same-value-after-navigating-to-another-fr
- Delete on card Swipe -> https://www.youtube.com/watch?v=eEonjkmox-0
- Cannot load Google map in Kotlin Fragment? -> https://stackoverflow.com/questions/69332790/cannot-load-google-map-in-kotlin-fragment
- Camera Integration BitMap get path etc -> https://developer.android.com/reference/android/graphics/BitmapFactory
- FireBase Auth -> https://www.youtube.com/watch?v=Gf-bttCyt7c&list=PLN8KRetF_zntkNhZxnIjGl7TjbGO7Egus&index=5