package com.example.nebula.ui.list

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.example.nebula.data.model.ImageObject
import com.example.nebula.data.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    fun imageAtIndex(i: Int): LiveData< ImageObject? > {
        return Transformations.map(_images) {
            it[i]
        }
    }
}