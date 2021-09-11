package com.umbrella.nasaapiapp.view

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.umbrella.nasaapiapp.R
import com.umbrella.nasaapiapp.databinding.FragmentPictureBinding
import com.umbrella.nasaapiapp.model.AppState
import com.umbrella.nasaapiapp.model.Day
import com.umbrella.nasaapiapp.viewmodel.PictureViewModel
import java.lang.Exception

const val ARG_WIKI_REQUEST = "ARG_WIKI_REQUEST"

class PictureFragment : Fragment() {

    private var _binding: FragmentPictureBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PictureViewModel by lazy {
        ViewModelProvider(this).get(PictureViewModel::class.java)
    }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var isFirstLaunch = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomSheet()
        initInputEditTextEnterKeyListener()
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            with(binding) {
                when (checkedId) {
                    chipBeforeYesterday.id -> viewModel.makeApiCall(Day.BEFORE_YESTERDAY)
                    chipYesterday.id -> viewModel.makeApiCall(Day.YESTERDAY)
                    chipToday.id -> viewModel.makeApiCall(Day.TODAY)
                }
            }
        }
        viewModel.getPictureLiveData().observe(viewLifecycleOwner) { result ->
            result?.let {
                renderData(result)
            }
        }
        if (isFirstLaunch) {
            binding.chipToday.isChecked = true
            isFirstLaunch = false
        }
    }

    private fun initInputEditTextEnterKeyListener() {
        binding.inputEditText.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val bundle = Bundle()
                bundle.putString(ARG_WIKI_REQUEST, binding.inputEditText.text.toString())
                findNavController().navigate(R.id.wikiFragment, bundle)
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun renderData(result: AppState) {
        with(binding) {
            when (result) {

                is AppState.Loading -> {
                    progressBarLayout.root.visibility = View.VISIBLE
                }

                is AppState.Success -> {
                    Picasso.get().load(result.response.url)
                        .into(pictureImageView, object : Callback {
                            override fun onSuccess() {
                                progressBarLayout.root.visibility = View.GONE
                                bottomSheet.bottomSheetDescriptionHeader.text =
                                    result.response.title
                                bottomSheet.bottomSheetDescription.text =
                                    result.response.explanation
                            }

                            override fun onError(error: Exception) {
                                progressBarLayout.progressBar.visibility = View.GONE
                                showToast(error)
                            }
                        })
                    viewModel.clearPictureLiveData()
                }

                is AppState.Error -> {
                    progressBarLayout.progressBar.visibility = View.GONE
                    showToast(result.error)
                    viewModel.clearPictureLiveData()
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