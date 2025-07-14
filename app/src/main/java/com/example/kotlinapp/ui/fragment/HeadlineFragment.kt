package com.example.kotlinapp.ui.fragment

import android.R
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import com.example.kotlinapp.State.Uistate
import com.example.kotlinapp.adapter.HeadlineAdapter
import com.example.kotlinapp.databinding.FragmentHeadlineBinding
import com.example.kotlinapp.model.ArticleOr
import com.example.kotlinapp.ui.viewmodel.NewsViewModel
import com.example.kotlinapp.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class HeadlineFragment : Fragment() {

    private val headlineViewModel: NewsViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var adapter: HeadlineAdapter
    private var _binding: FragmentHeadlineBinding? = null
    private val binding get() = _binding!!

    private var currentIsSearching: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeadlineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupObservers()


        // Initial fetch
        headlineViewModel.getNews()





    }

    private fun setupRecyclerView() {
        adapter = HeadlineAdapter(emptyList(), requireContext())
        binding.headlinrecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.headlinrecycler.adapter = adapter
    }

    private fun setupListeners() {
        binding.clearbtn.setOnClickListener {
            binding.searchEditText.setText("")
            searchViewModel.clearSearch()
            headlineViewModel.getNews()
            binding.clearbtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.holo_red_light
                )
            )
        }

        binding.searchEditText.setOnEditorActionListener { v, actionId, event ->
            binding.swipeRefreshLayout.isRefreshing = true
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchEditText.text.toString().trim()
                if (query.isNotEmpty()) {
                    searchViewModel.getsearch(query)
                    binding.clearbtn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.holo_red_dark
                        )
                    )
                    binding.swipeRefreshLayout.isRefreshing = false
                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
                }
                true
            } else {
                binding.swipeRefreshLayout.isRefreshing = false

                false
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            if (binding.searchEditText.text.isEmpty()) {
                headlineViewModel.refresh()
            } else {
                searchViewModel.getsearch(binding.searchEditText.text.toString().trim())
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    searchViewModel.isSearching.collect { isSearching ->
                        currentIsSearching = isSearching // update a flag
                    }
                }

                launch {
                    searchViewModel.searchstate.collect { state ->
                        if (currentIsSearching) {
                            handleUiState(state)
                        }
                    }
                }

                launch {
                    headlineViewModel.newsstate.collect { state ->
                        if (!currentIsSearching) {
                            handleUiState(state)
                        }
                    }
                }
            }
        }
    }

    private fun handleUiState(state: Uistate<List<ArticleOr>>) {
        when (state) {
            is Uistate.Loading -> {
                binding.swipeRefreshLayout.isRefreshing = true
            }

            is Uistate.Success -> {
                adapter.updateartical(state.data)
                binding.noDataText.visibility = if (state.data.isEmpty()) View.VISIBLE else View.GONE
                binding.headlinrecycler.scrollToPosition(0)
                binding.swipeRefreshLayout.isRefreshing = false
            }

            is Uistate.Error -> {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
