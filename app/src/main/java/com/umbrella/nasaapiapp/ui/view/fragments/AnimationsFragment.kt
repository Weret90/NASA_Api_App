package com.umbrella.nasaapiapp.ui.view.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.squareup.picasso.Picasso
import com.umbrella.nasaapiapp.R
import com.umbrella.nasaapiapp.databinding.FragmentAnimationsStartBinding
import com.umbrella.nasaapiapp.model.Day
import com.umbrella.nasaapiapp.model.PictureLoadState
import com.umbrella.nasaapiapp.ui.viewmodel.PictureViewModel

class AnimationsFragment : Fragment() {

    private var show = false

    private var _binding: FragmentAnimationsStartBinding? = null
    private val binding get() = _binding!!
    private val pictureViewModel: PictureViewModel by lazy {
        ViewModelProvider(this).get(PictureViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimationsStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pictureViewModel.getPictureLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }
        pictureViewModel.makeApiCall(Day.TODAY)
        binding.backgroundImage.setOnClickListener { if (show) hideComponents() else showComponents() }
    }

    private fun renderData(result: PictureLoadState) {
        when (result) {
            is PictureLoadState.Success -> {
                binding.animationsLoadingLayout.root.visibility = View.GONE
                Picasso.get().load(result.response.url).into(binding.backgroundImage)
                binding.description.text = result.response.explanation
                binding.title.text = result.response.title
            }
            else -> {
                binding.animationsLoadingLayout.root.visibility = View.VISIBLE
            }
        }
    }

    private fun showComponents() {
        show = true

        val constraintSet = ConstraintSet()
        constraintSet.clone(context, R.layout.fragment_animations_end)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200

        TransitionManager.beginDelayedTransition(binding.constraintContainer, transition)
        constraintSet.applyTo(binding.constraintContainer)
    }

    private fun hideComponents() {
        show = false

        val constraintSet = ConstraintSet()
        constraintSet.clone(context, R.layout.fragment_animations_start)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200

        TransitionManager.beginDelayedTransition(binding.constraintContainer, transition)
        constraintSet.applyTo(binding.constraintContainer)
    }
}