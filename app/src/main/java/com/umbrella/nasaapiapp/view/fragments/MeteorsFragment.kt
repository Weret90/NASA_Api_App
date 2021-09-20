package com.umbrella.nasaapiapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.umbrella.nasaapiapp.R
import com.umbrella.nasaapiapp.databinding.FragmentMeteorsBinding
import com.umbrella.nasaapiapp.model.MeteorsLoadState
import com.umbrella.nasaapiapp.view.adapters.MeteorsNameAdapter
import com.umbrella.nasaapiapp.view.hide
import com.umbrella.nasaapiapp.view.show
import com.umbrella.nasaapiapp.viewmodel.MeteorsViewModel

const val ARG_METEOR_INFO = "ARG_METEOR_INFO"

class MeteorsFragment : Fragment() {

    private var _binding: FragmentMeteorsBinding? = null
    private val binding get() = _binding!!
    private val meteorsNameAdapter = MeteorsNameAdapter()
    private lateinit var viewModel: MeteorsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeteorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MeteorsViewModel::class.java)
        binding.meteorsRecyclerView.adapter = meteorsNameAdapter
        viewModel.getMeteorsLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }
        meteorsNameAdapter.setOnItemClickListener { meteor ->
            val bundle = Bundle()
            bundle.putSerializable(ARG_METEOR_INFO, meteor)
            findNavController().navigate(R.id.meteorsCloseApproachDatesFragment, bundle)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (meteorsNameAdapter.getData().isEmpty()) viewModel.makeApiCall()
        }
    }

    private fun renderData(meteorsLoadState: MeteorsLoadState) {
        when (meteorsLoadState) {
            is MeteorsLoadState.Loading -> {
                binding.loadingScreen.root.show()
                binding.errorScreen.root.hide()
            }
            is MeteorsLoadState.Success -> {
                binding.loadingScreen.root.hide()
                binding.errorScreen.root.hide()
                meteorsNameAdapter.setData(meteorsLoadState.response.meteors)
            }
            is MeteorsLoadState.Error -> {
                binding.loadingScreen.root.hide()
                binding.errorScreen.root.show()
                binding.errorScreen.errorMessage.text = meteorsLoadState.error.message
                binding.errorScreen.buttonReload.setOnClickListener {
                    viewModel.makeApiCall()
                }
            }
        }
    }
}