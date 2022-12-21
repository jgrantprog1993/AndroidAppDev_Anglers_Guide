package ie.wit.anglersguide.ui.list


import LoggedInViewModel
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.anglersguide.R
import ie.wit.anglersguide.adaptors.FishingSpotAdapter
import ie.wit.anglersguide.adaptors.FishingSpotListener
import ie.wit.anglersguide.databinding.FragmentFishingSpotListBinding
import ie.wit.anglersguide.models.FishingSpotModel
import ie.wit.anglersguide.ui.activity.FishingSpotListViewModel
import ie.wit.anglersguide.utils.*
import timber.log.Timber

class FishingSpotListFragment: Fragment(), FishingSpotListener {

    private var _fragBinding: FragmentFishingSpotListBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var loader : AlertDialog
    private val fishingSpotListViewModel: FishingSpotListViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    lateinit var searchView: SearchView
    var toggle: Boolean = false
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
        fishingSpotListViewModel.observableFishingSpotsList.observe(viewLifecycleOwner,
            Observer {
                    fishingspots ->
                        fishingspots?.let {

                            render(fishingspots as List<FishingSpotModel>)
                            hideLoader(loader)
                        }
            })


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
            }


            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
                val item = menu.findItem(R.id.toggleFishingSpots) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                val toggleFishingSpots: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
                toggleFishingSpots.isChecked = false
                toggle = toggleFishingSpots.isChecked
                val search = menu?.findItem(R.id.action_search)
                val searchView = search?.actionView as? SearchView
                searchView?.setQueryHint("Search Title")
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(query: String?): Boolean {
                        if(query !=null)
                        {
                            searchDB(query)
                        }
                        Timber.i("QUERY ->"+query)

                        return false
                    }

                })

                toggleFishingSpots.setOnCheckedChangeListener { _, isChecked ->
                    toggle = if (isChecked) {
                        fishingSpotListViewModel.loadAll()
                        true
                    } else {
                        fishingSpotListViewModel.load()
                        false
                    }
                }
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
            Timber.i("Rendering ->" + fishingSpots)
            fragBinding.recyclerView.adapter =
                FishingSpotAdapter(fishingSpots as MutableList<FishingSpotModel>, this, fishingSpotListViewModel.readOnly.value!!)
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
        val action = FishingSpotListFragmentDirections.actionFishingSpotListFragmentToFishingSpotDetailFragment(fishingspot.uid!!)
        if(!fishingSpotListViewModel.readOnly.value!!) {
            findNavController().navigate(action)
        }
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    fun getFilteredList(s: String) {


    }

    @SuppressLint("BinaryOperationInTimber")
    fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query !=null)
        {
            searchDB(query)
        }
        Timber.i("QUERY1 ->"+query)
        return true
    }

    private fun searchDB(query:String)
    {

        val searchQuery = "$query"
        Timber.i("toggle ->%s", toggle)
        if(!toggle) {
            fishingSpotListViewModel.loadFiltered(searchQuery)
        }
        else{
            fishingSpotListViewModel.loadAllFiltered(searchQuery)
        }

    }
}

private fun SearchView?.setOnQueryTextListener(onQueryTextListener: SearchView.OnQueryTextListener) {

}

private fun SearchView?.setOnQueryTextListener(searchView: SearchView) {

}

