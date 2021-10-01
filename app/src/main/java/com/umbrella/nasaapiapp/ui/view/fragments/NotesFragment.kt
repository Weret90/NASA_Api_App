package com.umbrella.nasaapiapp.ui.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umbrella.nasaapiapp.R
import com.umbrella.nasaapiapp.databinding.FragmentNotesBinding
import com.umbrella.nasaapiapp.databinding.FragmentPhotoFromMarsBinding
import com.umbrella.nasaapiapp.model.Note
import com.umbrella.nasaapiapp.ui.view.adapters.NotesAdapter

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private val adapter = NotesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.notesRV.adapter = adapter
        binding.buttonAddNote.setOnClickListener {
            adapter.addNote(Note("Заметка", "Описание заметки"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}