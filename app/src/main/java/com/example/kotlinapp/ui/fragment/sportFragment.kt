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
import com.example.kotlinapp.adapter.BusinessAdapter
import com.example.kotlinapp.adapter.SportAdapter
import com.example.kotlinapp.databinding.FragmentBusinessBinding
import com.example.kotlinapp.databinding.FragmentSportBinding
import com.example.kotlinapp.ui.viewmodel.SportVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class sportFragment : Fragment() {

    lateinit var adapter: SportAdapter

    val sportviewmodel : SportVM by viewModels()
    var _binding: FragmentSportBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSportBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= SportAdapter(emptyList(),requireContext())

        binding.sportrecycler.layoutManager= LinearLayoutManager(this.context)

        binding.sportrecycler.adapter=adapter

        sportviewmodel.getsport()


        viewLifecycleOwner.lifecycleScope.launch {

            sportviewmodel.sportstate.flowWithLifecycle(viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED).collect { state ->

                when(state){

                    is Uistate.Loading ->{
                        binding.swipeRefreshLayoutsport.isRefreshing=true
                    }
                    is Uistate.Success ->{
                        adapter.updateartical(state.data)
                        binding.swipeRefreshLayoutsport.isRefreshing=false
                        binding.noDataTextsport.visibility = if (state.data.isEmpty()) View.VISIBLE else View.GONE
                        Log.d("sportfragment", "Articles loaded: ${state.data.size}")

                    }
                    is Uistate.Error  ->{
                        binding.swipeRefreshLayoutsport.isRefreshing = false
                        Toast.makeText(requireContext(), state.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
                        Log.e("sportFragment", "Error: ${state.message}")

                    }
                }
            }

        }
        binding.swipeRefreshLayoutsport.setOnRefreshListener {
            Log.d("sportFragment", "Swipe to refresh triggered")
            sportviewmodel.refresh()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    }
