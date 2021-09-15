package com.umbrella.nasaapiapp.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
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
import com.umbrella.nasaapiapp.model.AppState
import com.umbrella.nasaapiapp.model.Day
import com.umbrella.nasaapiapp.viewmodel.PictureViewModel

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
}