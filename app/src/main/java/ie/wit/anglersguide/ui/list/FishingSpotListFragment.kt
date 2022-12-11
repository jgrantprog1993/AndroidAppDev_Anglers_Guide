package ie.wit.anglersguide.ui.list


import LoggedInViewModel
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.anglersguide.R
import ie.wit.anglersguide.adaptors.FishingSpotAdapter
import ie.wit.anglersguide.databinding.FragmentFishingSpotListBinding
import ie.wit.anglersguide.main.MainApp
import ie.wit.anglersguide.adaptors.FishingSpotListener
import ie.wit.anglersguide.firebase.FirebaseDBManager
import ie.wit.anglersguide.models.FishingSpotModel
import ie.wit.anglersguide.ui.activity.FishingSpotListViewModel
import ie.wit.anglersguide.utils.createLoader
import ie.wit.anglersguide.utils.hideLoader
import ie.wit.anglersguide.utils.showLoader
import ie.wit.anglersguide.utils.SwipeToDeleteCallback
import ie.wit.anglersguide.utils.SwipeToEditCallback

class FishingSpotListFragment: Fragment(), FishingSpotListener {

    private var _fragBinding: FragmentFishingSpotListBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var loader : AlertDialog
    private val fishingSpotListViewModel: FishingSpotListViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()


    //private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    //private lateinit var deleteIcon: Drawable
    //var fishingSpot = FishingSpotModel()
    //private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentFishingSpotListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        loader = createLoader(requireActivity())
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        showLoader(loader,"Downloading FishingSpots")

        fishingSpotListViewModel.observableFishingSpotsList.observe(viewLifecycleOwner, Observer {
                fishingspots ->
                fishingspots?.let {
                    render(fishingspots as ArrayList<FishingSpotModel>)
                    hideLoader(loader)
                    //checkSwipeRefresh()
                }
        })
        //setSwipeRefresh()
        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showLoader(loader,"Deleting FishingSpot")
                val adapter = fragBinding.recyclerView.adapter as FishingSpotAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                fishingSpotListViewModel.delete(fishingSpotListViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as FishingSpotModel).uid!!)

                hideLoader(loader)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onFishingSpotClick(viewHolder.itemView.tag as FishingSpotModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)

        return root
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)


            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(
                    menuItem,
                    requireView().findNavController()
                )
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

        fun render(fishingSpots: List<FishingSpotModel>) {
            fragBinding.recyclerView.adapter =
                FishingSpotAdapter(fishingSpots as MutableList<FishingSpotModel>, this)
            if (fishingSpots.isEmpty()) {
                fragBinding.recyclerView.visibility = View.GONE
                fragBinding.FishingSpotsNotFound.visibility = View.VISIBLE
            } else {
                fragBinding.recyclerView.visibility = View.VISIBLE
                fragBinding.FishingSpotsNotFound.visibility = View.GONE
            }
        }




    companion object {
        @JvmStatic
        fun newInstance() =
            FishingSpotListFragment().apply {
                arguments = Bundle().apply {  }
            }
    }

    override fun onFishingSpotClick(fishingspot: FishingSpotModel) {
        val action = FishingSpotListFragmentDirections.actionFishingSpotListFragmentToFishingSpotFragment(fishingspot.uid!!)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader, "Downloading FishingSpots")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                fishingSpotListViewModel.liveFirebaseUser.value = firebaseUser
                fishingSpotListViewModel.load()
            }
        })
        //hideLoader(loader)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }


//    private fun registerRefreshCallback() {
//        refreshIntentLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//            { loadFishingSpots() }
//    }
//
//    private fun loadFishingSpots() {
//        showFishingSpots(app.fishingspots.findAll())
//    }
//
//    fun showFishingSpots (fishingspots: MutableList<FishingSpotModel>) {
//        _fragBinding?.recyclerView?.adapter = FishingSpotAdapter(fishingspots, this)
//        _fragBinding?.recyclerView?.adapter?.notifyDataSetChanged()
//    }

   /// override fun onCreateOptionsMenu(menu: Menu): Boolean {
       // menuInflater.inflate(R.menu.menu_main, menu)
       // return super.onCreateOptionsMenu(menu)
   // }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.item_add -> {
//                val launcherIntent = Intent(this@FishingSpotListFragment.context, FishingspotActivity::class.java)
//                startActivityForResult(launcherIntent,0)
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

}

