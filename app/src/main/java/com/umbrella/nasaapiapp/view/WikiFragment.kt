package com.umbrella.nasaapiapp.view

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.umbrella.nasaapiapp.R
import com.umbrella.nasaapiapp.databinding.FragmentPictureBinding
import com.umbrella.nasaapiapp.databinding.FragmentWikiBinding

private const val WIKI_URL = "https://ru.m.wikipedia.org/wiki/"

class WikiFragment : Fragment() {

    private var _binding: FragmentWikiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWikiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            binding.wikiPage.webViewClient = object : WebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.progressBarLayout.root.visibility = View.VISIBLE
                }
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.progressBarLayout.root.visibility = View.GONE
                }
            }
            binding.wikiPage.loadUrl(WIKI_URL + it.getString(ARG_WIKI_REQUEST))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}