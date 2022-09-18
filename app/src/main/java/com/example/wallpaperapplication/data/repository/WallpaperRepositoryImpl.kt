package com.example.wallpaperapplication.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.wallpaperapplication.data.remote.WallpaperApi
import javax.inject.Inject

class WallpaperRepositoryImpl @Inject constructor(private val wallpaperApi: WallpaperApi) :
    WallpaperRepository {

    override fun getSearchPhotos(query: String) = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            WallpaperPagingSource(
                wallpaperApi = wallpaperApi,
                query = query
            )
        }
    ).liveData
}