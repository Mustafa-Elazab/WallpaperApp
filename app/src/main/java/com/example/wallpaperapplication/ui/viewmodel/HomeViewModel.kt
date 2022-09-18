package com.example.wallpaperapplication.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.wallpaperapplication.data.repository.WallpaperRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val wallpaperRepositoryImpl: WallpaperRepositoryImpl) :
    ViewModel() {

    private var lastQuery = "all"
    private val defaultQuery = MutableLiveData(lastQuery)

    val wallpapers = defaultQuery.switchMap { query ->
        wallpaperRepositoryImpl.getSearchPhotos(query).cachedIn(viewModelScope)
    }


    fun searchQuery(query: String = lastQuery) {
        lastQuery = query
        defaultQuery.value = lastQuery
    }
}