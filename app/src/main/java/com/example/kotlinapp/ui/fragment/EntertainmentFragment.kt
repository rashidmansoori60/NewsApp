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
import com.example.kotlinapp.adapter.EntertainmentAdapter
import com.example.kotlinapp.adapter.SportAdapter
import com.example.kotlinapp.databinding.FragmentEntertainmentBinding
import com.example.kotlinapp.databinding.FragmentSportBinding
import com.example.kotlinapp.ui.viewmodel.EntertainmentVM
import com.example.kotlinapp.ui.viewmodel.SportVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class EntertainmentFragment : Fragment() {

    lateinit var adapter: EntertainmentAdapter

    val entertainmentVM : EntertainmentVM by viewModels()
    var _binding: FragmentEntertainmentBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEntertainmentBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= EntertainmentAdapter(emptyList(),requireContext())

        binding.enterrecycler.layoutManager= LinearLayoutManager(this.context)

        binding.enterrecycler.adapter=adapter

        entertainmentVM.getEntertainment()


        viewLifecycleOwner.lifecycleScope.launch {

            entertainmentVM.entertainmentstate.flowWithLifecycle(viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED).collect { state ->

                when(state){

                    is Uistate.Loading ->{
                        binding.swipeRefreshLayoutenter.isRefreshing=true
                    }
                    is Uistate.Success ->{
                        adapter.updateartical(state.data)
                        binding.swipeRefreshLayoutenter.isRefreshing=false
                        binding.noDataTextsport.visibility = if (state.data.isEmpty()) View.VISIBLE else View.GONE
                        Log.d("sportfragment", "Articles loaded: ${state.data.size}")

                    }
                    is Uistate.Error  ->{
                        binding.swipeRefreshLayoutenter.isRefreshing = false
                        Toast.makeText(requireContext(), state.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
                        Log.e("sportFragment", "Error: ${state.message}")

                    }
                }
            }

        }
        binding.swipeRefreshLayoutenter.setOnRefreshListener {
            Log.d("sportFragment", "Swipe to refresh triggered")
            entertainmentVM.refresh()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
