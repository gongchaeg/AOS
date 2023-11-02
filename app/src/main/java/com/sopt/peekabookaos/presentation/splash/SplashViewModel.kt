package com.sopt.peekabookaos.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.peekabookaos.domain.entity.SplashState
import com.sopt.peekabookaos.domain.usecase.GetSplashStateUseCase
import com.sopt.peekabookaos.domain.usecase.GetVersionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getSplashStateUseCase: GetSplashStateUseCase,
    private val getVersionUseCase: GetVersionUseCase
) : ViewModel() {
    private val _versionName: MutableLiveData<String> = MutableLiveData()
    val versionName: LiveData<String> = _versionName
    private val _isVersionStatus = MutableLiveData(false)
    val isVersionStatus: LiveData<Boolean> = _isVersionStatus

    fun getSplashState(): SplashState = getSplashStateUseCase()

    fun getVersion() {
        viewModelScope.launch {
            getVersionUseCase()
                .onSuccess { response ->
                    _versionName.value = response.androidForceVersion
                    _isVersionStatus.value = true
                }
                .onFailure { throwable ->
                    _isVersionStatus.value = false
                    Timber.e("$throwable")
                }
        }
    }
}
