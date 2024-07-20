package com.dkproject.presentation.ui.screen.signUp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkproject.domain.model.UserData
import com.dkproject.domain.usecase.Image.UploadImageUseCase
import com.dkproject.domain.usecase.signup.SignUpUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val uploadImageUseCase: UploadImageUseCase,
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()


    private val _snackbarMessages = MutableStateFlow<String?>(null)
    val snackbarMessages = _snackbarMessages.asStateFlow()

    private val _navigateEvent = MutableStateFlow<SignUpNavigation>(SignUpNavigation.Default)
    val navigateEvent = _navigateEvent.asStateFlow()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading = _loading.asStateFlow()

    init {
        updateUserUid()
    }

    //유저 uid 업데이트 초기화에서 불림
    private fun updateUserUid() {
        _uiState.update { it.copy(userUid = auth.currentUser?.uid ?: "") }
    }
    //유저 닉네임 상태 업데이트
    fun updateUserNickName(nickName: String) {
        _uiState.update { it.copy(userNickName = nickName) }
    }
    //유저 프로필 사진 정보 상태 업데이트
    fun updateUserProfileImageUrl(profileImageUrl: String) {
        _uiState.update { it.copy(userProfileImageUrl = profileImageUrl) }
    }
    //유저 키 상태 업데이트
    fun updateUserHeight(height: Double) {

        _uiState.update { it.copy(userHeight = height) }
    }
    //유저 몸무게 상태 업데이트
    fun updateUserWeight(weight: Double) {
        _uiState.update { it.copy(userWeight = weight) }
    }
    //유저 포지션 상태 업데이트
    fun updateUserPosition(position: String) {
        _uiState.update { it.copy(userPosition = position) }
    }
    //스낵바 메시지 클리어
    fun clearSnackbarMessage() {
        _snackbarMessages.update { null }
    }
    //회원가입 버튼 누를 시 체크, 입력 미 완료시 스낵바 상태 변경, 완료 시 회원가입 함수 호출
    fun validateInput() {
        val value = uiState.value
        if (value.userProfileImageUrl.isEmpty()) {
            _snackbarMessages.update { "프로필 이미지를 입력해주세요." }
        } else if (value.userNickName.isEmpty()) {
            _snackbarMessages.update { "닉네임을 입력해주세요." }
        } else if (value.userHeight == 0.0) {
            _snackbarMessages.update { "키를 입력해주세요." }
        } else if (value.userWeight == 0.0) {
            _snackbarMessages.update { "몸무게를 입력해주세요." }
        } else if (value.userPosition.isEmpty()) {
            _snackbarMessages.update { "포지션을 입력해주세요." }
        } else {
            //다 입력했으므로 회원가입 진행
            signUp()
        }
    }

    //회원가입 실행 함수
    private fun signUp() {
        _loading.update { true } //로딩바 표시
        viewModelScope.launch {
            //파이어 스토리지에 이미지 업로드 후 주소 받아오기, 실패 시 스낵바 출력 후 함수 종료
            val profileString = uploadImageUseCase(userUid = uiState.value.userUid, imageUri = uiState.value.userProfileImageUrl).getOrElse {
                _loading.update { false }
                _snackbarMessages.update { "이미지 업로드에 실패했습니다." }
                return@launch
            }
            val value = uiState.value
            //회원가입 모델
            val userData = UserData(
                userUid = value.userUid,
                userNickName = value.userNickName,
                userProfileImageUrl = profileString,
                userHeight = value.userHeight,
                userWeight = value.userWeight,
                userPosition = value.userPosition
            )
            //회원가입 유즈 케이스
            signUpUseCase(userData = userData).onSuccess {
                _loading.update { false }
                _navigateEvent.update { SignUpNavigation.Home }
            }.onFailure {
                //회원 가입 실패 시 스낵바 출력 후 로딩바 false
                _snackbarMessages.update { "회원 가입에 실패했습니다." }
                _loading.update { false }
            }
        }
    }



}

data class SignUpUiState(
    val userUid: String = "",
    val userNickName: String = "",
    val userProfileImageUrl: String = "",
    val userHeight: Double = 0.0,
    val userWeight: Double = 0.0,
    val userPosition: String = ""
)

enum class SignUpNavigation {
    Default,
    Home
}