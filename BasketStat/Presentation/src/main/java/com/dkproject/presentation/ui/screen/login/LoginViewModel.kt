package com.dkproject.presentation.ui.screen.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkproject.domain.usecase.signup.CheckExistUserUseCase
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val checkExistUserUseCase: CheckExistUserUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    companion object {
        const val TAG = "LoginViewModel"
    }

    private val _state = MutableStateFlow(loginUiState())
    val state = _state.asStateFlow()
    fun kakaoLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            }
        }
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            if (error != null) {
                Log.e(TAG, "로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "로그인 성공 ${token.accessToken}")
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            }
        }
    }

    fun googleLogin() {
        viewModelScope.launch {
            Log.d(TAG, auth.currentUser?.uid.toString())
            checkExistUserUseCase(userUid = auth.currentUser?.uid.toString()).fold(
                onSuccess = { exist ->
                    if (exist) {
                        _state.update { it.copy(navigation = LoginNavigation.Home) }
                    } else {
                        _state.update { it.copy(navigation = LoginNavigation.SignUp) }
                    }
                },
                onFailure = {

                }
            )
        }
    }

    fun appleLogin() {

    }

    fun navigationReset() {
        _state.update { it.copy(navigation = LoginNavigation.Default) }
    }


}


data class loginUiState(
    val navigation: LoginNavigation = LoginNavigation.Default,
    val message: String = ""
)


enum class LoginNavigation {
    SignUp,
    Home,
    Default
}