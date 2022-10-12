package com.my.dicoding_android_intermediate.ui.setting

import com.my.dicoding_android_intermediate.ui.home.HomeViewModel

package com.my.dicoding_android_intermediate.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.dicoding_android_intermediate.adapter.LoadingAdapter
import com.my.dicoding_android_intermediate.adapter.StoryListAdapter
import com.my.dicoding_android_intermediate.data.local.entity.Story
import com.my.dicoding_android_intermediate.databinding.FragmentHomeBinding
import com.my.dicoding_android_intermediate.ui.create.CreateStoryActivity
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
    private lateinit var listAdapter: StoryListAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                startActivity(intent)
            }
        }
    }


    private fun setSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            getStories()
        }
    }

    private fun getStories() {
        homeViewModel.getStoriesTwo(token).observe(viewLifecycleOwner) { result ->
            updateTheStories(result)
        }
    }

    private fun setRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        listAdapter = StoryListAdapter()

        listAdapter.addLoadStateListener { loadState ->
            if ((loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && listAdapter.itemCount < 1) || loadState.source.refresh is LoadState.Error) {
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
                    footer = LoadingAdapter {
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