package com.example.nebula.ui.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View {

        val transition =
            TransitionInflater.from(context).inflateTransition(R.transition.shared_image)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition

        _binding = FragmentImageDetailBinding.inflate(inflater, container, false)
        binding.apply {
            imageName = args.name
            executePendingBindings()
        }

        postponeEnterTransition()

        init()

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun init() {
        val image = imageListViewModel.imageAtIndex(args.position)
        binding.image = image
        if(image==null) {
            startPostponedEnterTransition()
            return
        }
        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                startPostponedEnterTransition()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                startPostponedEnterTransition()
                return false
            }

        }

        val thumbRequest = Glide.with(requireContext())
            .load(image.url)
            .listener(listener)

        Glide.with(requireContext())
            .load(image.hdUrl)
            .error(R.drawable.bg_placeholder)
            .thumbnail(thumbRequest)
            .into(binding.detailImage)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}