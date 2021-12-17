package com.example.nebula.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.nebula.databinding.FragmentImageDetailBinding
import com.example.nebula.ui.list.ImageListViewModel

class ImageDetailFragment : Fragment() {

    private val imageListViewModel: ImageListViewModel by activityViewModels()

    private val args: ImageDetailFragmentArgs by navArgs()

    private var _binding: FragmentImageDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        _binding = FragmentImageDetailBinding.inflate(inflater, container, false)

        init()
        observe()

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun init() {
    }

    private fun observe() {
        imageListViewModel.imageAtIndex(args.position).observe(viewLifecycleOwner) {
            _binding?.image = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}