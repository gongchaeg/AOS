package com.sopt.peekabookaos.presentation.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.peekabookaos.domain.entity.Recommend
import com.sopt.peekabookaos.domain.usecase.GetRecommendUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel @Inject constructor(
    private val getRecommendUseCase: GetRecommendUseCase
) : ViewModel() {
    private val _recommendedBook = MutableLiveData<List<Recommend>>()
    val recommendedBook: LiveData<List<Recommend>> = _recommendedBook

    private val _recommendingBook = MutableLiveData<List<Recommend>>()
    val recommendingBook: LiveData<List<Recommend>> = _recommendingBook

    fun getRecommend() {
        viewModelScope.launch {
            getRecommendUseCase()
                .onSuccess { response ->
                    _recommendingBook.value = response.recommendingBook
                    _recommendedBook.value = response.recommendedBook
                }.onFailure { throwable ->
                    Timber.e("$throwable")
                }
        }
    }
}
