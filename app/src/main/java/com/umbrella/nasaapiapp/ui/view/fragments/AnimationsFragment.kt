package com.umbrella.nasaapiapp.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.umbrella.nasaapiapp.R
import com.umbrella.nasaapiapp.databinding.FragmentAnimationsStartBinding

class AnimationsFragment : Fragment() {

    private var show = false

    private var _binding: FragmentAnimationsStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimationsStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backgroundImage.setOnClickListener { if (show) hideComponents() else showComponents() }
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