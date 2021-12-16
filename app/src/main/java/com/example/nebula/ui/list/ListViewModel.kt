package com.example.nebula.ui.list

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.example.nebula.data.model.ImageObject
import com.example.nebula.data.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : AndroidViewModel(application) {

    private val imageRepository: ImageRepository = ImageRepository()

    private val _images = MutableLiveData<List<ImageObject>>(listOf())
    val images: LiveData<List<ImageObject>> = _images

    val imagesWithAspectRatio: LiveData<List<ImageObject>?> = Transformations.map(_images) {
        it.filter { im -> im.hasAspectRatio }
    }
    init {
        getImages()
    }

    private fun getImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val im = imageRepository.fetchImages(getApplication() as Context)
            _images.postValue(im)
            calculateImageDimensions()
        }
    }

    private fun calculateImageDimensions() {
        _images.value?.let {
            for (image in it) {
                if(!image.hasAspectRatio) {
                    val futureBitmap = Glide.with(getApplication() as Context)
                        .asBitmap()
                        .load(image.url)
                        .submit()
                    val bitmap = futureBitmap.get()
                    val w = bitmap.width.toFloat()
                    val h = bitmap.height.toFloat()
                    image.aspectRatio = w/h
                }
            }
        }
    }
//
//    private fun getUserBookIds() {
//        accountRepo.getUserProfile()?.addSnapshotListener { value, e ->
//            if (e != null) {
//                //TODO notify error
//            }
//            val profile = value?.toObject(UserProfile::class.java)
//            profile?.let {
//                _ownedBookIds.postValue(profile.ownedBooks)
//            }
//        }
//    }
}