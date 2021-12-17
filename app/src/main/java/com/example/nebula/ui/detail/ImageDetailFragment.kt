package com.example.nebula.ui.detail

import android.Manifest
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.nebula.R
import com.example.nebula.databinding.FragmentImageDetailBinding
import com.example.nebula.ui.list.ImageListViewModel
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        binding.detailSave.setOnClickListener { view ->
            checkSavePermission {
                view.isEnabled = false
                binding.progressBar.visibility = View.VISIBLE

                imageListViewModel.downloadImage(args.position)
            }
        }

    }

    /**
     * Check write access permission
     *
     * @param onGrant Callback when permission is granted
     */
    private fun checkSavePermission(onGrant : () -> Unit) {

        val baseLis: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                onGrant()
            }
            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                context?.let {
                    AlertDialog.Builder(it)
                        .setTitle(R.string.permission_rat_storage_title)
                        .setMessage(R.string.permission_rat_storage_message_export)
                        .setNegativeButton(R.string.dialog_button_cancel) { dialog, _ ->
                            dialog.dismiss()
                            token.cancelPermissionRequest()
                        }
                        .setPositiveButton(R.string.dialog_button_ok){ dialog, _ ->
                            dialog.dismiss()
                            token.continuePermissionRequest()
                        }
                        .setOnDismissListener { token.cancelPermissionRequest() }
                        .show()
                }
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
            }
        }

        val snackBarLis: PermissionListener = SnackbarOnDeniedPermissionListener.Builder.with(
            activity?.findViewById(R.id.container) as ViewGroup,
            R.string.permission_rat_storage_denied_export)
            .withDuration(5000)
            .build()

        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(CompositePermissionListener(baseLis,snackBarLis)).check()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}