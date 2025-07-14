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
import com.example.kotlinapp.adapter.GeneralAdapter
import com.example.kotlinapp.adapter.SportAdapter
import com.example.kotlinapp.databinding.FragmentGeneralBinding
import com.example.kotlinapp.databinding.FragmentSportBinding
import com.example.kotlinapp.ui.viewmodel.GeneralVM
import com.example.kotlinapp.ui.viewmodel.SportVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue


@AndroidEntryPoint
class GeneralFragment : Fragment() {

    lateinit var adapter: GeneralAdapter

    val generalviewmodel : GeneralVM by viewModels()
    var _binding: FragmentGeneralBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGeneralBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= GeneralAdapter(emptyList(),requireContext())

        binding.generalrecycler.layoutManager= LinearLayoutManager(this.context)

        binding.generalrecycler.adapter=adapter

        generalviewmodel.getgeneral()


        viewLifecycleOwner.lifecycleScope.launch {

            generalviewmodel.generalstate.flowWithLifecycle(viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED).collect { state ->

                when(state){

                    is Uistate.Loading ->{
                        binding.swipeRefreshLayoutgeneral.isRefreshing=true
                    }
                    is Uistate.Success ->{
                        adapter.updateartical(state.data)
                        binding.swipeRefreshLayoutgeneral.isRefreshing=false
                        binding.noDataTextsport.visibility = if (state.data.isEmpty()) View.VISIBLE else View.GONE
                        Log.d("general", "Articles loaded: ${state.data.size}")

                    }
                    is Uistate.Error  ->{
                        binding.swipeRefreshLayoutgeneral.isRefreshing = false
                        Toast.makeText(requireContext(), state.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
                        Log.e("general", "Error: ${state.message}")

                    }
                }
            }

        }
        binding.swipeRefreshLayoutgeneral.setOnRefreshListener {
            Log.d("sportFragment", "Swipe to refresh triggered")
            generalviewmodel.refresh()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
