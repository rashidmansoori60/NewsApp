package com.example.kotlinapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinapp.R
import com.example.kotlinapp.State.Uistate
import com.example.kotlinapp.adapter.ScienceAdapter
import com.example.kotlinapp.adapter.SportAdapter
import com.example.kotlinapp.databinding.FragmentScienceBinding
import com.example.kotlinapp.databinding.FragmentSportBinding
import com.example.kotlinapp.ui.viewmodel.ScienceVM
import com.example.kotlinapp.ui.viewmodel.SportVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class ScienceFragment : Fragment() {

    lateinit var adapter: ScienceAdapter

    val sciencevm : ScienceVM by viewModels()
    var _binding: FragmentScienceBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentScienceBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= ScienceAdapter(emptyList(),requireContext())

        binding.sciencerecycler.layoutManager= LinearLayoutManager(this.context)

        binding.sciencerecycler.adapter=adapter

        sciencevm.getscience()


        viewLifecycleOwner.lifecycleScope.launch {

            sciencevm.sciencestate.flowWithLifecycle(viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED).collect { state ->

                when(state){

                    is Uistate.Loading ->{
                        binding.swipeRefreshLayoutscience.isRefreshing=true
                    }
                    is Uistate.Success ->{
                        adapter.updateartical(state.data)
                        binding.swipeRefreshLayoutscience.isRefreshing=false
                        binding.noDataTextsport.visibility = if (state.data.isEmpty()) View.VISIBLE else View.GONE
                        Log.d("science", "Articles loaded: ${state.data.size}")

                    }
                    is Uistate.Error  ->{
                        binding.swipeRefreshLayoutscience.isRefreshing = false
                        Toast.makeText(requireContext(), state.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
                        Log.e("sportFragment", "Error: ${state.message}")

                    }
                }
            }

        }
        binding.swipeRefreshLayoutscience.setOnRefreshListener {
            Log.d("science", "Swipe to refresh triggered")
            sciencevm.refresh()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
