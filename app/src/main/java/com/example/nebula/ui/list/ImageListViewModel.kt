package com.example.nebula.ui.list

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.annotation.StringDef
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.example.nebula.R
import com.example.nebula.data.model.ImageObject
import com.example.nebula.data.repository.ImageRepository
import com.example.nebula.data.util.ImageDownloadUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

class ImageListViewModel(application: Application) : AndroidViewModel(application) {

    private val imageRepository: ImageRepository = ImageRepository()

    private val _images = MutableLiveData<List<ImageObject>>(listOf())
    val images: LiveData<List<ImageObject>> = _images

    val imagesWithAspectRatio: LiveData<List<ImageObject>> = Transformations.map(_images) {
        Log.d("TAG", "transforming")
        it.filter { im -> im.hasAspectRatio }
    }
    init {
        getImages()
    }

    private fun getImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val im = imageRepository.fetchImages(getApplication() as Context)
            _images.postValue(im)
            calculateImageDimensions(im)
        }
    }

    private fun calculateImageDimensions(list: List<ImageObject>) {
        for (image in list) {
            if (!image.hasAspectRatio) {
                val futureBitmap = Glide.with(getApplication() as Context)
                    .asBitmap()
                    .load(image.url)
                    .submit()
                val bitmap = futureBitmap.get()
                val w = bitmap.width.toFloat()
                val h = bitmap.height.toFloat()
                image.aspectRatio = w / h
                _images.postValue(list)
                Log.d("TAG", "calculateImageDimensions: ${image.title} is $w x $h")
            }
        }
    }

    fun imageAtIndex(i: Int): ImageObject? {
        return _images.value?.get(i)
    }

    companion object {
        const val RESULT_NONE = "NA"
        const val RESULT_SUCCESS = "S"
        const val RESULT_ERROR = "Err"
    }

    @StringDef(RESULT_NONE, RESULT_SUCCESS, RESULT_ERROR)
    annotation class DownloadResult

    data class DownloadResultObject(@DownloadResult val status: String, val file: File? = null) {
        companion object {
            fun none() = DownloadResultObject(RESULT_NONE)
            fun success(file: File) = DownloadResultObject(RESULT_SUCCESS,file)
            fun error() = DownloadResultObject(RESULT_ERROR)
        }
    }

    val downloadResult = MutableLiveData(DownloadResultObject.none())

    fun downloadImage(i: Int) {
        val image = imageAtIndex(i)?:return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val file = ImageDownloadUtil.download(getApplication(), image)
                downloadResult.postValue(DownloadResultObject.success(file))
            } catch (e: Exception) {
                downloadResult.postValue(DownloadResultObject.error())
            }
        }
    }
}