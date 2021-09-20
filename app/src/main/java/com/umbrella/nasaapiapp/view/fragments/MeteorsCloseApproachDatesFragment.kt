package com.umbrella.nasaapiapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umbrella.nasaapiapp.databinding.FragmentMeteorsCloseApproachDatesBinding
import com.umbrella.nasaapiapp.model.Meteor
import com.umbrella.nasaapiapp.view.adapters.MeteorsCloseApproachDatesAdapter

class MeteorsCloseApproachDatesFragment : Fragment() {

    private var _binding: FragmentMeteorsCloseApproachDatesBinding? = null
    private val binding get() = _binding!!
    private val adapter = MeteorsCloseApproachDatesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeteorsCloseApproachDatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            binding.meteorsCloseApproachDatesRecyclerView.adapter = adapter
            val meteor = it.getSerializable(ARG_METEOR_INFO) as Meteor
            adapter.setData(meteor.closeApproachData)
        }
    }
}