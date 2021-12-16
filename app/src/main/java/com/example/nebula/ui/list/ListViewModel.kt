package com.example.nebula.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ListViewModel(application: Application) : AndroidViewModel(application) {

//    private val bookRepo: BookRepository = BookRepository()

//    private val _allBooks = MutableLiveData<List<Book>>(listOf())
//    val allBooks: LiveData<List<Book>> = _allBooks
//
//    val homeFilter = MutableLiveData<String>("")
//
//    private val _filteredBooks: MediatorLiveData<List<Book>> by lazy {
//        val med = MediatorLiveData<List<Book>>()
//        med.addSource(_allBooks) { filterBookList() }
//        med.addSource(homeFilter) { filterBookList() }
//        med
//    }
//    val filteredBooks: LiveData<List<Book>> = _filteredBooks
//
//    private val _ownedBookIds = MutableLiveData<List<String>>(listOf())
//
//    private val _ownedBooks: MediatorLiveData<List<Book>> by lazy {
//        val med = MediatorLiveData<List<Book>>()
//        med.addSource(_allBooks) { findOwnedBooks() }
//        med.addSource(_ownedBookIds) { findOwnedBooks() }
//        getUserBookIds()
//        med
//    }
//    val ownedBooks: LiveData<List<Book>> = _ownedBooks
//
//    init {
//        setGreeting()
//        setInfo()
//        getAllBooks()
//    }
//
//    companion object {
//        const val MORNING: String = "M"
//        const val AFTERNOON: String = "A"
//        const val EVENING: String = "E"
//
//        private val MORNING_END = LocalTime.of(11, 55)
//        private val NOON_END = LocalTime.of(15, 0)
//        private val NIGHT_END = LocalTime.of(3, 55)
//    }
//
//    private fun filterBookList() {
//        val bookList = _allBooks.value?.toMutableList() ?: mutableListOf()
//        val filter = homeFilter.value ?: ""
//
//        _filteredBooks.value = when {
//            filter.isEmpty() -> bookList
//            else -> bookList.filter {
//                return@filter max(FuzzySearch.partialRatio(filter, it.title),FuzzySearch.partialRatio(filter, it.author)) > 65
//            }.sortedByDescending {
//                return@sortedByDescending when {
//                    it.title.contains(filter,true) || it.author.contains(filter,true) -> 100
//                    else -> max(FuzzySearch.partialRatio(filter, it.title),FuzzySearch.partialRatio(filter, it.author))
//                }
//            }
//        }
//    }
//
//    private fun findOwnedBooks() {
//        val bookList = _allBooks.value?.toMutableList() ?: mutableListOf()
//        val ids = _ownedBookIds.value ?: listOf()
//
//        _ownedBooks.value = bookList.filter {
//            return@filter ids.contains(it.id)
//        }
//    }
//
//    private fun setGreeting() {
//        val time = LocalTime.now()
//        _greeting.value = when {
//            time.isBefore(NIGHT_END) -> EVENING
//            time.isBefore(MORNING_END) -> MORNING
//            time.isBefore(NOON_END) -> AFTERNOON
//            else -> EVENING
//        }
//    }
//
//    private fun setInfo() {
//        val user = AccountRepository().getUser()
//        user?.apply {
//            _userName.value = displayName
//        }
//    }
//
//    private fun getAllBooks() {
//        bookRepo.getBookListRef().addSnapshotListener { value, e ->
//            if (e != null) {
//                //TODO notify error
//            }
//            val bookList: MutableList<Book> = mutableListOf()
//            for (doc in value!!) {
//                val book = doc.toObject(Book::class.java)
//                bookList.add(book)
//            }
//            _allBooks.postValue(bookList)
//        }
//    }
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