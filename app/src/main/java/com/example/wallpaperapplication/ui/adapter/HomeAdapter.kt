package com.example.wallpaperapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.wallpaperapplication.databinding.WallpaperItemLayoutBinding
import com.example.wallpaperapplication.model.UnsplashPhoto
import com.example.wallpaperapplication.ui.fragment.HomeFragmentDirections
import com.example.wallpaperapplication.utils.loadImage

class HomeAdapter :
    PagingDataAdapter<UnsplashPhoto, HomeAdapter.HomeViewHolder>(WALLPAPER_COMPARATOR) {

    class HomeViewHolder(private val binding: WallpaperItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UnsplashPhoto) {

            binding.imgItem.apply {
                transitionName = item.urls.small
                loadImage(item.urls.small)
            }
        }

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }

        holder.itemView.setOnClickListener {
            var data =
                HomeFragmentDirections.actionHomeFragmentToDetailFragment(model = currentItem!!)
            Navigation.findNavController(it).navigate(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {

        return HomeViewHolder(
            WallpaperItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    companion object {
        private val WALLPAPER_COMPARATOR = object : DiffUtil.ItemCallback<UnsplashPhoto>() {
            override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: UnsplashPhoto,
                newItem: UnsplashPhoto
            ) = oldItem == newItem

        }
    }

}