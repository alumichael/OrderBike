package com.example.orderbike.ui


import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.orderbike.viewmodel.MainViewModel
import com.example.orderbike.R
import com.example.orderbike.adapter.BikeListAdapter
import com.example.orderbike.databinding.FragmentBikeListBinding
import com.example.orderbike.visible
import kotlinx.coroutines.launch


/**
 *BikeListFragment handles the list population and itemClick
 */
class BikeListFragment : Fragment() {
    private var _binding:FragmentBikeListBinding? = null
    private val binding get() = _binding!!
    lateinit var bikeListAdapter : BikeListAdapter

    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBikeListBinding.inflate(inflater, container, false)
        bikeListAdapter = BikeListAdapter()
        binding.recyclerView.adapter = bikeListAdapter

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //request bike json data online
        sharedViewModel.getBikeList()
        visible(binding.progressBar,true)

        //observe the bike list response livedata
        sharedViewModel.bikeListResponse.observe(requireActivity(), Observer {
            lifecycleScope.launch {
                with(binding){
                    //hide progress bar
                    visible(binding.progressBar,false)

                    //submit to recyclerView Adapter
                    if(it?.features?.size!=0){

                        //compute distance between current location
                        //and the defined bike location

                        if(sharedViewModel.currentLocationCord.value!=null){
                            it?.features?.forEach {
                                it.distance = it.geometry?.coordinates?.get(0)?.let { start ->
                                    it.geometry?.coordinates?.get(1)?.let { end ->
                                        computeDistance(
                                            sharedViewModel.currentLocationCord.value!!,
                                            start,
                                            end
                                        )
                                    }
                                }
                            }
                        }


                        //Submits a new list to be diffed, and displayed.
                        bikeListAdapter.submitList(it?.features)

                        //on click itemList
                        bikeListAdapter.onItemClick={it->
                            it.id?.let { it1 -> Log.i("sentArgID", it1) }

                            //navigate to the detail location map view
                            //passed in bike coordinate as argument through naviagtionController
                            val action = it.id?.let { it1 ->
                                BikeListFragmentDirections.actionBikeListFragmentToDetailedFragment(
                                    it1
                                )
                            }
                            if (action != null) {
                                view.findNavController().navigate(action)
                            }

                        }

                    }else if(it.features?.size==0){
                        Toast.makeText(requireActivity(), "Empty List", Toast.LENGTH_LONG).show()
                    }else{
                        sharedViewModel.errorMessage.observe(requireActivity()) {
                            Toast.makeText(requireActivity(), it, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
    }

    private fun computeDistance(currentLocationCord: Location,
                                latDestination:Double,
                                longDestination:Double): Float {

        val bikeLocation = Location(LocationManager.NETWORK_PROVIDER)
        bikeLocation.latitude = latDestination
        bikeLocation.longitude = longDestination

        return  currentLocationCord.distanceTo(bikeLocation).div(1000)
    }
}