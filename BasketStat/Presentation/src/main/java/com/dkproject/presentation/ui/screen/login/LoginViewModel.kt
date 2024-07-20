package com.dkproject.presentation.ui.screen.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkproject.domain.usecase.signup.CheckExistUserUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.oAuthCredential
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
import kotlinx.coroutines.tasks.await
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

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading = _loading.asStateFlow()

    fun kakaoLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                    } else if (user != null) {
                        viewModelScope.launch {
                            _loading.update { true }
                            val provider = "oidc.kakao"
                            val credential = oAuthCredential(provider) {
                                idToken = token.idToken
                                accessToken = token.accessToken
                            }
                            auth.signInWithCredential(credential).addOnFailureListener {
                                _state.update { it.copy(message = "카카오톡 로그인에 실패하였습니다.") }
                            }.await()
                            checkExistUserUseCase(userUid = auth.currentUser?.uid.toString()).fold(
                                onSuccess = {exist->
                                    _loading.update { false }
                                    if (exist) {
                                        _state.update { it.copy(navigation = LoginNavigation.Home) }
                                    } else {
                                        _state.update { it.copy(navigation = LoginNavigation.SignUp) }
                                    }
                                },
                                onFailure = {
                                    _loading.update { false }
                                    _state.update { it.copy(message = "카카오톡 로그인에 실패하였습니다.") }
                                }
                            )
                        }
                    }

                }
            }
        }

// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    fun googleLogin() {
        _loading.update { true }
        viewModelScope.launch {
            Log.d(TAG, auth.currentUser?.uid.toString())
            checkExistUserUseCase(userUid = auth.currentUser?.uid.toString()).fold(
                onSuccess = { exist ->
                    _loading.update { false }
                    if (exist) {
                        _state.update { it.copy(navigation = LoginNavigation.Home) }
                    } else {
                        _state.update { it.copy(navigation = LoginNavigation.SignUp) }
                    }
                },
                onFailure = {
                    _loading.update { false }
                    _state.update { it.copy(message = "구글 로그인 실패") }
                }
            )
        }
    }

    fun appleLogin() {

    }

    fun clearSnackbarMessage() {
        _state.update { it.copy(message = null) }
    }

    fun navigationReset() {
        _state.update { it.copy(navigation = LoginNavigation.Default) }
    }


}


data class loginUiState(
    val navigation: LoginNavigation = LoginNavigation.Default,
    val message: String? = null
)


enum class LoginNavigation {
    SignUp,
    Home,
    Default
}