package com.sopt.peekabookaos.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.peekabookaos.data.entity.Book
import com.sopt.peekabookaos.data.entity.BookComment
import com.sopt.peekabookaos.data.repository.DetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailRepository: DetailRepository
) : ViewModel() {
    private val _bookData = MutableLiveData<Book>()
    val bookData: LiveData<Book> = _bookData

    private val _bookComment = MutableLiveData<BookComment>()
    val bookComment: LiveData<BookComment> = _bookComment

    private val _isMyDetailView = MutableLiveData<Boolean>()
    val isMyDetailView: LiveData<Boolean> = _isMyDetailView

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> = _isDeleted

    private val _bookId = MutableLiveData<Int>()
    val bookId: LiveData<Int> = _bookId

    fun initIsMyDetailView(detail: Boolean) {
        _isMyDetailView.value = detail
    }

    fun initBookId(id: Int) {
        _bookId.value = id
    }

    fun getDetail() {
        viewModelScope.launch {
            Timber.tag("kang").e("서버 연결 bookId")
            detailRepository.getDetail(bookId.value!!)
                .onSuccess { response ->
                    _bookComment.value = BookComment(
                        response.description,
                        response.memo
                    )
                    _bookData.value = response.book
                }.onFailure { throwable ->
                    Timber.e("$throwable")
                }
        }
    }

    fun deleteDetail() {
        viewModelScope.launch {
            detailRepository.deleteDetail(bookId.value!!)
                .onSuccess {
                    _isDeleted.value = true
                }.onFailure { throwable ->
                    _isDeleted.value = false
                    Timber.e("$throwable")
                }
        }
    }

    fun updateBookData() {
        _bookData.value = _bookData.value!!.copy(id = _bookId.value!!)
    }
}
