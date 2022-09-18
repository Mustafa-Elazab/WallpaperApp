package com.example.wallpaperapplication.ui.fragment

import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.IntentFilter
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.wallpaperapplication.R
import com.example.wallpaperapplication.databinding.FragmentDetailBinding
import com.example.wallpaperapplication.utils.*


class DetailFragment : Fragment() {

    val photoModel by navArgs<DetailFragmentArgs>()
    lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addNoLimitFlag()
        fabListener()
        loadImageView()
        setImageWallpaper()
        downloadImageListener()

    }

    private fun downloadImageListener() {
        binding.floatDownload.setOnClickListener {
            checkPermissionAndDownload(photoModel.model.urls.regular)
        }
    }

    private fun loadImageView() {

        binding.imgMain.apply {
            transitionName = photoModel.model.urls.small
            loadImage(photoModel.model.urls.regular)
        }
    }

    private fun fabListener() {

        binding.floatingActionButton.setOnClickListener {

            hideAndShowFABs(binding.linear.isVisible)

        }
    }

    private fun hideAndShowFABs(state: Boolean) {
        binding.floatingActionButton.setImageResource(
            if (state)
                R.drawable.ic_add_24
            else
                R.drawable.ic_close_24
        )
        binding.linear.isVisible = !state
    }

    private fun setImageWallpaper() {
        binding.floatPreview.setOnClickListener {
            val bmpImg = (binding.imgMain.drawable as TransitionDrawable).toBitmap()
            val wallManager =
                WallpaperManager.getInstance(requireActivity().applicationContext)
            showSetImageDialog(wallManager, bmpImg)
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(
            onComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(onComplete)
    }
}