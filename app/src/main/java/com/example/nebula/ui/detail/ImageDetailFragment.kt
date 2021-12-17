package com.example.nebula.ui.detail

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.nebula.R
import com.example.nebula.databinding.FragmentImageDetailBinding
import com.example.nebula.ui.list.ImageListViewModel

class ImageDetailFragment : Fragment() {

    private val imageListViewModel: ImageListViewModel by activityViewModels()

    private val args: ImageDetailFragmentArgs by navArgs()

    private var _binding: FragmentImageDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @BindingAdapter("imageUrl")
    fun loadImage(view: AppCompatImageView, url: String?) {
        Glide.with(requireContext())
            .load(url)
            .error(R.drawable.bg_placeholder)
            .into(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View {

        val transition =
            TransitionInflater.from(context).inflateTransition(R.transition.shared_image)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition

        _binding = FragmentImageDetailBinding.inflate(inflater, container, false)
        binding.apply {
            imageName = args.name
            imageUrl = args.url
            executePendingBindings()
        }

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
            binding.image = it
            binding.imageUrl = it?.hdUrl
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}