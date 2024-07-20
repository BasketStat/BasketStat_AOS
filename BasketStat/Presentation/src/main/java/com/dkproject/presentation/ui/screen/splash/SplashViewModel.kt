package com.dkproject.presentation.ui.screen.splash

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkproject.domain.usecase.signup.CheckExistUserUseCase
import com.dkproject.presentation.ui.screen.signUp.SignUpNavigation
import com.dkproject.presentation.util.NetworkManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val auth: FirebaseAuth,
    private val checkExistUserUseCase: CheckExistUserUseCase
): ViewModel(){
    private val _snackbarMessages = MutableStateFlow<String?>(null)
    val snackbarMessages = _snackbarMessages.asStateFlow()

    private val _navigateEvent = MutableStateFlow<SplashNavigation>(SplashNavigation.Default)
    val navigateEvent = _navigateEvent.asStateFlow()

    private val _loading = MutableStateFlow<Boolean>(true)
    val loading = _loading.asStateFlow()

    init {
        networkManager.registerNetworkCallback()
        observerNetworkConnection()
    }


    private fun observerNetworkConnection() {
        viewModelScope.launch {
            networkManager.isConnected.collectLatest {isConnected->
                Log.d("네트워크상태", isConnected.toString())
                isConnected?.let {
                    if (it) {
                        login()
                    } else {
                        _snackbarMessages.update { "네트워크 연결 상태를 확인해주세요" }
                    }
                }
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            _loading.update { true }
            if (auth.currentUser != null) {
                checkExistUserUseCase(auth.currentUser?.uid.toString()).onSuccess {exist->
                    if (exist) {
                        _navigateEvent.update { SplashNavigation.Home }
                    } else {
                        _navigateEvent.update { SplashNavigation.Login }
                    }
                }.onFailure {
                    _snackbarMessages.update { "자동 로그인에 실패하였습니다." }
                    _navigateEvent.update { SplashNavigation.Login }
                }
            } else {
                _navigateEvent.update { SplashNavigation.Login }
            }
            _loading.update { false }
        }
    }

    fun clearSnackbarMessage() {
        _snackbarMessages.update { null }
    }

    override fun onCleared() {
        super.onCleared()
        networkManager.unRegisterNetworkCallback()
    }

}

enum class SplashNavigation {
    Default,
    Login,
    Home
}