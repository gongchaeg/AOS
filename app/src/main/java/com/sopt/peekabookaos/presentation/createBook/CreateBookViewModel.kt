package com.sopt.peekabookaos.presentation.createBook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.peekabookaos.domain.entity.Book
import com.sopt.peekabookaos.domain.usecase.PostCreateBookUseCase
import com.sopt.peekabookaos.util.extensions.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateBookViewModel @Inject constructor(
    private val postCreateBookUseCase: PostCreateBookUseCase
) : ViewModel() {
    private val _bookInfo = MutableStateFlow(Book())
    val bookInfo = _bookInfo.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val comment = MutableStateFlow("")

    val memo = MutableStateFlow("")

    fun postCreateBook() {
        viewModelScope.launch {
            postCreateBookUseCase(
                bookImage = _bookInfo.value.bookImage,
                bookTitle = _bookInfo.value.bookTitle,
                author = _bookInfo.value.author,
                description = comment.value,
                memo = memo.value
            ).onSuccess { response ->
                _bookInfo.value = _bookInfo.value.copy(id = response)
                _uiEvent.emit(UiEvent.SUCCESS)
            }.onFailure { throwable ->
                _uiEvent.emit(UiEvent.ERROR)
                Timber.e("$throwable")
            }
        }
    }

    fun initBookInfo(bookInfo: Book) {
        _bookInfo.value = bookInfo
    }
}