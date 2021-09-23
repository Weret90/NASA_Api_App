package com.umbrella.nasaapiapp.ui.view.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.umbrella.nasaapiapp.R
import com.umbrella.nasaapiapp.databinding.FragmentPictureBinding
import com.umbrella.nasaapiapp.model.Day
import com.umbrella.nasaapiapp.model.PictureLoadState
import com.umbrella.nasaapiapp.ui.view.MainActivity
import com.umbrella.nasaapiapp.ui.view.hide
import com.umbrella.nasaapiapp.ui.view.show
import com.umbrella.nasaapiapp.ui.view.showToast
import com.umbrella.nasaapiapp.ui.viewmodel.PictureViewModel

const val ARG_WIKI_REQUEST = "ARG_WIKI_REQUEST"
private const val ARG_IS_DAY_WAS_CHOSEN = "ARG_IS_DAY_WAS_CHOSEN"
const val ARG_IS_RED_THEME = "ARG_IS_RED_THEME"

class PictureFragment : Fragment() {

    private var _binding: FragmentPictureBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PictureViewModel by lazy {
        ViewModelProvider(this).get(PictureViewModel::class.java)
    }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var isDayWasChosen = false

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

        savedInstanceState?.let {
            isDayWasChosen = it.getBoolean(ARG_IS_DAY_WAS_CHOSEN)
        }

        setBottomAppBar(view)

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
        if (!isDayWasChosen) {
            binding.chipToday.isChecked = true
            isDayWasChosen = true
        }

        binding.errorLayout.buttonReload.setOnClickListener {
            viewModel.makeApiCall(Day.TODAY)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(ARG_IS_DAY_WAS_CHOSEN, isDayWasChosen)
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

    private fun renderData(result: PictureLoadState) {
        with(binding) {
            when (result) {

                is PictureLoadState.Loading -> {
                    progressBarLayout.root.show()
                    errorLayout.root.hide()
                    bottomSheet.root.hide()
                    bottomAppBar.hide()
                }

                is PictureLoadState.Success -> {
                    if (result.response.mediaType != "video") {
                        Picasso.get().load(result.response.url)
                            .into(pictureImageView, PicassoCallback(result))
                        pictureImageView.isEnabled = false
                        iconPlay.hide()
                    } else {
                        Picasso.get().load(result.response.thumbnailUrl)
                            .into(pictureImageView, PicassoCallback(result))
                        iconPlay.show()
                        pictureImageView.isEnabled = true
                        pictureImageView.setOnClickListener {
                            startActivity(Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(result.response.url)
                            })
                        }
                    }
                    viewModel.clearPictureLiveData()
                }

                is PictureLoadState.Error -> {
                    binding.progressBarLayout.root.hide()
                    binding.errorLayout.root.show()
                    bottomSheet.root.hide()
                    bottomAppBar.hide()
                    errorLayout.errorMessage.text = result.error.message
                    context?.showToast(result.error)
                    viewModel.clearPictureLiveData()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_change_theme, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.app_bar_change_theme) {
            val context = activity as MainActivity
            val sp = context.getPreferences(Context.MODE_PRIVATE)
            val isRedTheme = sp.getBoolean(ARG_IS_RED_THEME, false)
            sp.edit().putBoolean(ARG_IS_RED_THEME, !isRedTheme).apply()
            context.recreate()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class PicassoCallback(val result: PictureLoadState.Success) : Callback {
        override fun onSuccess() {
            with(binding) {
                progressBarLayout.root.hide()
                errorLayout.root.hide()
                bottomSheet.root.show()
                bottomAppBar.show()
                bottomSheet.bottomSheetDescriptionHeader.text =
                    result.response.title
                bottomSheet.bottomSheetDescription.text =
                    result.response.explanation
            }
        }

        override fun onError(error: Exception) {
            with(binding) {
                progressBarLayout.root.hide()
                errorLayout.root.show()
                bottomSheet.root.hide()
                bottomAppBar.hide()
                errorLayout.errorMessage.text = error.message
            }
            context?.showToast(error)
        }
    }
}