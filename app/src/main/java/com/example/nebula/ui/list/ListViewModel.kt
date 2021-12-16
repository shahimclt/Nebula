package com.example.nebula.ui.list

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nebula.data.model.ImageObject
import com.example.nebula.data.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : AndroidViewModel(application) {

    private val imageRepository: ImageRepository = ImageRepository()

    private val _images = MutableLiveData<List<ImageObject>>(listOf())
    val images: LiveData<List<ImageObject>> = _images

    init {
        getImages()
    }

    private fun getImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val im = imageRepository.fetchImages(getApplication() as Context)
            _images.postValue(im)
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