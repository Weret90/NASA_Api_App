package com.umbrella.nasaapiapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.umbrella.nasaapiapp.databinding.FragmentPictureBinding
import com.umbrella.nasaapiapp.model.AppState
import com.umbrella.nasaapiapp.viewmodel.PictureViewModel
import java.lang.Exception

class PictureFragment : Fragment() {

    private var _binding: FragmentPictureBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PictureViewModel by lazy {
        ViewModelProvider(this).get(PictureViewModel::class.java)
    }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomSheet()
        viewModel.getPictureLiveData().observe(viewLifecycleOwner) { result ->
            renderData(result)
        }
        viewModel.makeApiCall()
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun renderData(result: AppState) {
        with(binding) {
            when (result) {
                is AppState.Loading -> progressBar.visibility = View.VISIBLE
                is AppState.Success -> {
                    Picasso.get().load(result.response.url)
                        .into(pictureImageView, object : Callback {
                            override fun onSuccess() {
                                progressBar.visibility = View.GONE
                                bottomSheet.root.visibility = View.VISIBLE
                                bottomSheet.bottomSheetDescriptionHeader.text = result.response.title
                                bottomSheet.bottomSheetDescription.text = result.response.explanation
                            }
                            override fun onError(error: Exception) {
                                progressBar.visibility = View.GONE
                                showToast(error)
                            }
                        })
                }
                is AppState.Error -> {
                    progressBar.visibility = View.GONE
                    showToast(result.error)
                }
            }
        }
    }

    private fun showToast(error: Throwable) {
        Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}