package com.my.dicoding_android_intermediate.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.dicoding_android_intermediate.adapter.LoadingStateAdapter
import com.my.dicoding_android_intermediate.adapter.StoriesListAdapter
import com.my.dicoding_android_intermediate.data.entities.Story
import com.my.dicoding_android_intermediate.databinding.FragmentHomeBinding
import com.my.dicoding_android_intermediate.ui.create.CreateStoryActivity
import com.my.dicoding_android_intermediate.ui.maps.MapLocationActivity
import com.my.dicoding_android_intermediate.utils.Utils
import com.my.dicoding_android_intermediate.utils.animateVisibility
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPagingApi
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var token: String = ""
    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var listAdapter: StoriesListAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(LayoutInflater.from(requireActivity()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        token = requireActivity().intent.getStringExtra(Utils.TOKEN) ?: ""
        setRecyclerView()
        setSwipeRefreshLayout()
        getStories()

        binding?.fabCreateStory?.setOnClickListener {
            Intent(requireContext(), CreateStoryActivity::class.java).also { intent ->
                intent.putExtra(Utils.TOKEN, token)
                startActivity(intent)
            }
        }

        binding?.fabMap?.setOnClickListener {
            Intent(requireContext(), MapLocationActivity::class.java).also { intent ->
                intent.putExtra(Utils.TOKEN, token)
                startActivity(intent)
            }
        }
    }


    private fun getStories() {
        homeViewModel.getStoriesTwo(token).observe(viewLifecycleOwner) { result ->
            updateTheStories(result)
        }
    }

    private fun setSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            getStories()
        }
    }

    private fun setRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        listAdapter = StoriesListAdapter()

        listAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.Loading) {
                binding.apply {
                    status.animateVisibility(true)
                    ivNotFoundError.animateVisibility(true)
                    stories.animateVisibility(false)
                }
            } else {
                binding.apply {
                    status.animateVisibility(false)
                    ivNotFoundError.animateVisibility(false)
                    stories.animateVisibility(true)
                }
            }

            binding.swipeRefresh.isRefreshing = loadState.source.refresh is LoadState.Loading
        }

        try {
            recyclerView = binding.stories
            recyclerView.apply {
                adapter = listAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        listAdapter.retry()
                    }
                )
                layoutManager = linearLayoutManager
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun updateTheStories(stories: PagingData<Story>) {
        val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()

        listAdapter.submitData(lifecycle, stories)

        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

}