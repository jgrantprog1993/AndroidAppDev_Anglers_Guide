package ie.wit.anglersguide.fragments

import android.icu.text.DateFormat
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import ie.wit.anglersguide.R
import ie.wit.anglersguide.databinding.FragmentSummariseBinding
import ie.wit.anglersguide.main.MainApp
import ie.wit.anglersguide.models.SummaryModel
import ie.wit.anglersguide.models.generateRandomId

class SummariseFragment: Fragment() {

    lateinit var app: MainApp
    var totalDonated = 0
    private var _fragBinding: FragmentSummariseBinding? = null
    private val fragBinding get() = _fragBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        app = activity?.application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentSummariseBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_summarise)

        //fragBinding.progressBar.max = 10000
        //fragBinding.amountPicker.minValue = 1
        //fragBinding.amountPicker.maxValue = 1000

        //fragBinding.amountPicker.setOnValueChangedListener { _, _, newVal ->
            //Display the newly selected number to paymentAmount
        //    fragLayout.paymentAmount.setText("$newVal")
        //}
        return root;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()

    }

    fun setButtonListener(layout: FragmentSummariseBinding) {
        layout.addSumButton.setOnClickListener{
            if( layout.locationSelected.text.isEmpty())
            {
                Toast.makeText(context, "No Location Entered", Toast.LENGTH_LONG).show()
            }
//            else if (layout.selectedDate.text.isEmpty()){
//                Toast.makeText(context, "No Date Selected", Toast.LENGTH_LONG).show()
//            }
            else if (layout.weatherSum.text.isEmpty()) {
                Toast.makeText(context, "No Weather Conditions Entered Entered", Toast.LENGTH_LONG).show()
            }
            else if (layout.amountFish.text.isEmpty()){
                Toast.makeText(context, "How Many fish did you catch?", Toast.LENGTH_LONG).show()
            }
            else if (layout.ratingBar.rating < 0.5){
                Toast.makeText(context, "Rate the Overall Session", Toast.LENGTH_LONG).show()
            }
            else {
                val tideConditions = if(layout.tide.checkedRadioButtonId == R.id.TideIn) "Tide In" else "Tide out"
                val locationSelected = layout.locationSelected.text.toString()
                //val dateSelected = layout.selectedDate.toString()
                app.summaryStore.create(SummaryModel(id = generateRandomId(),
                                                    location =locationSelected,
                                                    // date  = dateSelected.,
                                                    tide = tideConditions,
                                                    weather =layout.weatherSum.text.toString(),
                                                    amountOfFish = layout.amountFish.text.toString().toInt(),
                                                    rating = layout.ratingBar.rating.toDouble()))
            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SummariseFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}



