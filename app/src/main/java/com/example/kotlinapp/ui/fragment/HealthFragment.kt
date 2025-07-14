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
import com.example.kotlinapp.adapter.HealthAdapter
import com.example.kotlinapp.adapter.SportAdapter
import com.example.kotlinapp.databinding.FragmentHealthBinding
import com.example.kotlinapp.databinding.FragmentSportBinding
import com.example.kotlinapp.ui.viewmodel.HealthVm
import com.example.kotlinapp.ui.viewmodel.SportVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class HealthFragment : Fragment() {

    lateinit var adapter: HealthAdapter

    val healthVm : HealthVm by viewModels()
    var _binding: FragmentHealthBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHealthBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= HealthAdapter(emptyList(),requireContext())

        binding.healthrecycler.layoutManager= LinearLayoutManager(this.context)

        binding.healthrecycler.adapter=adapter

        healthVm.gethealth()


        viewLifecycleOwner.lifecycleScope.launch {

            healthVm.healthstate.flowWithLifecycle(viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED).collect { state ->

                when(state){

                    is Uistate.Loading ->{
                        binding.swipeRefreshLayouthealth.isRefreshing=true
                    }
                    is Uistate.Success ->{
                        adapter.updateartical(state.data)
                        binding.swipeRefreshLayouthealth.isRefreshing=false
                        binding.noDataTextsport.visibility = if (state.data.isEmpty()) View.VISIBLE else View.GONE
                        Log.d("health", "Articles loaded: ${state.data.size}")

                    }
                    is Uistate.Error  ->{
                        binding.swipeRefreshLayouthealth.isRefreshing = false
                        Toast.makeText(requireContext(), state.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
                        Log.e("health", "Error: ${state.message}")

                    }
                }
            }

        }
        binding.swipeRefreshLayouthealth.setOnRefreshListener {
            Log.d("health", "Swipe to refresh triggered")
            healthVm.refresh()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
