package com.example.kotlinapp.ui.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinapp.State.Uistate
import com.example.kotlinapp.adapter.BusinessAdapter
import com.example.kotlinapp.databinding.FragmentBusinessBinding
import com.example.kotlinapp.ui.viewmodel.Businessviewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BusinessFragment : Fragment() {

    lateinit var adapter: BusinessAdapter
    val businessviewmodel: Businessviewmodel by viewModels()

    var _binding: FragmentBusinessBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBusinessBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("BusinessFragment", "onViewCreated called")


        adapter= BusinessAdapter(emptyList(),requireContext())

        binding.businessnrecycler.layoutManager= LinearLayoutManager(this.context)

        binding.businessnrecycler.adapter=adapter










        viewLifecycleOwner.lifecycleScope.launch {

            businessviewmodel.businessuistate.flowWithLifecycle(viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED).collect { state ->

           when(state){

               is Uistate.Loading ->{
                   binding.swipeRefreshLayoutbusiness.isRefreshing=true
               }
               is Uistate.Success ->{
                   adapter.updateartical(state.data)
                   binding.swipeRefreshLayoutbusiness.isRefreshing=false
                   Log.d("BusinessFragment", "Articles loaded: ${state.data.size}")

               }
               is Uistate.Error  ->{
                   binding.swipeRefreshLayoutbusiness.isRefreshing = false
                   Toast.makeText(requireContext(), state.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
                   Log.e("BusinessFragment", "Error: ${state.message}")

               }
           }
            }

        }


        businessviewmodel.getbusiness()

        binding.swipeRefreshLayoutbusiness.setOnRefreshListener {
            Log.d("BusinessFragment", "Swipe to refresh triggered")
            businessviewmodel.refresh()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}