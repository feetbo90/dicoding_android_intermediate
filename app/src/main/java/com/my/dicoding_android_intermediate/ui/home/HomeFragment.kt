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
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.dicoding_android_intermediate.R
import com.my.dicoding_android_intermediate.adapter.LoadingStateAdapter
import com.my.dicoding_android_intermediate.adapter.StoriesListAdapter
import com.my.dicoding_android_intermediate.adapter.StoryListAdapter
import com.my.dicoding_android_intermediate.data.entities.Story
import com.my.dicoding_android_intermediate.data.remote.response.StoryResponse
import com.my.dicoding_android_intermediate.data.remote.response.StoryResponseItem
import com.my.dicoding_android_intermediate.data.result.MyResult
import com.my.dicoding_android_intermediate.databinding.FragmentHomeBinding
import com.my.dicoding_android_intermediate.ui.create.CreateStoryActivity
import com.my.dicoding_android_intermediate.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var token: String = ""
    private val homeViewModel: HomeViewModel by viewModels()

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
        setViewModel()
        setSwipeRefreshLayout()

        binding?.fabCreateStory?.setOnClickListener {
            Intent(requireContext(), CreateStoryActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }
    }

    private fun setViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            launch {
                homeViewModel.stories.collect { result ->
                    onFollowsResultReceived(result)
                }
            }
            launch {
                homeViewModel.isLoaded.collect { loaded ->
                    if (!loaded) {
                        homeViewModel.getStories(token)
                        homeViewModel.setLoaded(false)
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun setRefresh() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                homeViewModel.stories.collect { result ->
                    onFollowsResultReceived(result)
                }
            }
            launch {
                homeViewModel.isLoaded.collect { loaded ->
                    if (!loaded) {
                        homeViewModel.getStories(token)
                        homeViewModel.setLoaded(false)
                        binding.swipeRefresh.isRefreshing = false
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun setSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            setRefresh()
        }
    }


    private fun onFollowsResultReceived(result: MyResult<StoryResponse>) {
        when (result) {
            is MyResult.Loading -> showLoading(true)
            is MyResult.Error -> {
                binding.status.visibility = View.VISIBLE
                binding.status.text = resources.getString(R.string.noStoriesFound)
                binding.swipeRefresh.isRefreshing = false
                showLoading(false)
            }
            is MyResult.Success -> {
                binding.swipeRefresh.isRefreshing = false
                showStories(result.data.storyResponseItems)
                showLoading(false)
            }
            else -> {}
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.loading.visibility = View.VISIBLE
        else binding.loading.visibility = View.GONE
    }

    private fun showStories(stories: ArrayList<StoryResponseItem>) {
        if (stories.size > 0) {
            val linearLayoutManager = LinearLayoutManager(requireContext())
            val listAdapter = StoriesListAdapter()

            binding.stories.apply {
                layoutManager = linearLayoutManager
                adapter = listAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        listAdapter.retry()
                    }
                )
                setHasFixedSize(true)
            }

            listAdapter.setOnItemClickCallback(object :
                StoriesListAdapter.OnItemClickCallback {
                override fun onItemClicked(data: StoryResponseItem) {
                }
            })
        } else binding.status.visibility = View.VISIBLE
    }

    private fun updateTheStories(stories: PagingData<Story>) {
        val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()

        listAdapter.submitData(lifecycle, stories)

        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

}