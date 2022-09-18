package com.example.wallpaperapplication.data.repository

import androidx.paging.PagingSource
import com.example.wallpaperapplication.data.remote.WallpaperApi
import com.example.wallpaperapplication.model.UnsplashPhoto
import retrofit2.HttpException
import java.io.IOException



const val FIRST_WALLPAPER_INDEX = 1
const val EMPTY_LIST = "empty List"

class WallpaperPagingSource(
    private val wallpaperApi: WallpaperApi,
    private val query: String
) : PagingSource<Int, UnsplashPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val pos = params.key ?: FIRST_WALLPAPER_INDEX

        return try {
            val response = wallpaperApi.searchPhotos(query, pos, params.loadSize)
            val wallpapers = response.results

            if (wallpapers.isEmpty())
                throw IOException(EMPTY_LIST)
            else
                LoadResult.Page(
                    data = wallpapers,
                    prevKey = if (pos == FIRST_WALLPAPER_INDEX) null else pos - 1,
                    nextKey = if (wallpapers.isEmpty()) null else pos + 1
                )
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        }
    }
}