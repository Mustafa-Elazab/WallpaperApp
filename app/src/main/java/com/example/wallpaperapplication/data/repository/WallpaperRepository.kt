package com.example.wallpaperapplication.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.wallpaperapplication.model.UnsplashPhoto

interface WallpaperRepository {

    fun getSearchPhotos(query: String): LiveData<PagingData<UnsplashPhoto>>
}