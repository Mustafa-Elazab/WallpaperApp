package com.example.wallpaperapplication.ui.fragment

import RecentLoadStatesAdapter
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.example.wallpaperapplication.R
import com.example.wallpaperapplication.data.repository.EMPTY_LIST
import com.example.wallpaperapplication.databinding.FragmentHomeBinding
import com.example.wallpaperapplication.model.UnsplashPhoto
import com.example.wallpaperapplication.ui.adapter.HomeAdapter
import com.example.wallpaperapplication.ui.viewmodel.HomeViewModel
import com.example.wallpaperapplication.utils.changeMode
import com.example.wallpaperapplication.utils.clearNoLimitFlag
import com.example.wallpaperapplication.utils.getLastThemeMode
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    private val wallpaperAdapter by lazy {
        HomeAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clearNoLimitFlag()
        checkThemeMode()
        initRecyclerView()
        refreshListener()
        observeObservers()
        loadStatesListener()
        searchListener(view.context)

    }

    private fun searchListener(context: Context) {
        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && binding.edtSearch.text.isNotEmpty()) {
                viewModel.searchQuery(binding.edtSearch.text.toString())
                Log.i("###", viewModel.searchQuery(binding.edtSearch.text.toString()).toString())
                closeSearch(context)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun closeSearch(context: Context) {
        binding.edtSearch.text.clear()
        binding.edtSearch.clearFocus()
        val input = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        input.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun loadStatesListener() {
        fun loadStatesListener() {
            wallpaperAdapter.addLoadStateListener { state ->
                with(state.source.refresh) {
                    binding.recentSwipeRefresh.isRefreshing = this is LoadState.Loading
                    binding.recentImagesRV.isVisible = this is LoadState.NotLoading
                    binding.searchLayout.root.isVisible =
                        this is LoadState.Error && this.error.message == EMPTY_LIST
                    binding.noInternetLayout.root.isVisible =
                        this is LoadState.Error && this.error.message != EMPTY_LIST

                    if (this is LoadState.NotLoading &&
                        state.append.endOfPaginationReached &&
                        wallpaperAdapter.itemCount < 1
                    ) {
                        binding.recentImagesRV.isVisible = false
                    }
                }
            }
        }
    }

    private fun observeObservers() {
        viewModel.wallpapers.observe(viewLifecycleOwner) {
            wallpaperAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun refreshListener() {

        binding.recentSwipeRefresh.setOnRefreshListener {
            if (binding.searchLayout.root.isVisible)
                viewModel.searchQuery("all")
            else
                viewModel.searchQuery()
        }
    }

    private fun initRecyclerView() {

        binding.recentImagesRV.apply {

            itemAnimator = null
            setHasFixedSize(true)
            adapter = wallpaperAdapter.withLoadStateHeaderAndFooter(
                header = RecentLoadStatesAdapter {
                    wallpaperAdapter.retry()
                },
                footer = RecentLoadStatesAdapter {
                    wallpaperAdapter.retry()
                }

            )
        }

    }

    private fun checkThemeMode() {

        val isLight = activity?.getLastThemeMode()
        binding.btnTheme.setImageResource(
            if (isLight!!) {
                R.drawable.ic_light_24
            } else {
                R.drawable.ic_dark_24
            }
        )
        binding.btnTheme.setOnClickListener {
            changeMode(requireActivity().getPreferences(Context.MODE_PRIVATE))
            requireActivity().recreate()
        }
    }





}