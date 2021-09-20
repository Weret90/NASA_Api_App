package com.umbrella.nasaapiapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.umbrella.nasaapiapp.databinding.FragmentPhotoFromMarsBinding
import com.umbrella.nasaapiapp.model.Photo
import com.umbrella.nasaapiapp.model.PhotosFromMarsLoadState
import com.umbrella.nasaapiapp.view.hide
import com.umbrella.nasaapiapp.view.invisible
import com.umbrella.nasaapiapp.view.show
import com.umbrella.nasaapiapp.view.showToast
import com.umbrella.nasaapiapp.viewmodel.PhotosFromMarsViewModel

class PhotoFromMarsFragment : Fragment() {

    private var _binding: FragmentPhotoFromMarsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PhotosFromMarsViewModel by lazy {
        ViewModelProvider(this).get(PhotosFromMarsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoFromMarsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPhotosLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }
        binding.photosErrorMessage.buttonReload.setOnClickListener {
            viewModel.makeApiCall()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (binding.appCompatImageView.drawable == null)
                viewModel.makeApiCall()
        }
    }

    private fun renderData(loadState: PhotosFromMarsLoadState) {
        when (loadState) {
            is PhotosFromMarsLoadState.Loading -> {
                binding.photosErrorMessage.root.hide()
                binding.photosProgressBar.show()
            }
            is PhotosFromMarsLoadState.Success -> {
                loadPictureWithPicasso(loadState.response.photos, 0)
                initNextAndPreviousButtons(loadState.response.photos)
            }
            is PhotosFromMarsLoadState.Error -> {
                binding.photosErrorMessage.root.show()
                binding.photosProgressBar.hide()
                binding.photosErrorMessage.errorMessage.text = loadState.error.message
                context?.showToast(loadState.error)
            }
        }
    }

    private fun initNextAndPreviousButtons(photos: List<Photo>) {
        var photoIndex = 0
        binding.appCompatImageButtonNext.setOnClickListener {
            binding.photosProgressBar.show()
            photoIndex++
            loadPictureWithPicasso(photos, photoIndex)
        }
        binding.appCompatImageButtonPrevious.setOnClickListener {
            binding.photosProgressBar.show()
            photoIndex--
            loadPictureWithPicasso(photos, photoIndex)
        }
    }

    private fun loadPictureWithPicasso(photos: List<Photo>, index: Int) {
        Picasso.get().load(photos[index].imgSrc.replace("http", "https"))
            .into(binding.appCompatImageView, object : Callback {
                override fun onSuccess() {
                    checkButtonsVisibility(index, photos)
                    binding.photosProgressBar.hide()
                    binding.photosErrorMessage.root.hide()
                }

                override fun onError(e: Exception) {
                    binding.photosErrorMessage.root.show()
                    binding.photosProgressBar.hide()
                    binding.photosErrorMessage.errorMessage.text = e.message
                    context?.showToast(e)
                }
            })
    }

    private fun checkButtonsVisibility(photoIndex: Int, photos: List<Photo>) {
        if (photoIndex == 0) {
            binding.appCompatImageButtonPrevious.invisible()
        } else {
            binding.appCompatImageButtonPrevious.show()
        }
        if (photoIndex == photos.lastIndex) {
            binding.appCompatImageButtonNext.invisible()
        } else {
            binding.appCompatImageButtonNext.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}