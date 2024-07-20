package com.dkproject.presentation.ui.screen.signUp

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.dkproject.presentation.Activity.HomeActivity
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.components.CustomTextField
import com.dkproject.presentation.ui.theme.background
import com.dkproject.presentation.ui.theme.buttonColor
import com.dkproject.presentation.ui.theme.textColor
import com.dkproject.presentation.ui.theme.textFieldBackground
import com.dkproject.presentation.ui.theme.wheelCOlor
import com.dkproject.presentation.util.PositionData
import com.dkproject.presentation.util.rememberImeState


@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.uiState.collectAsState().value //회원가입 정보 상태
    val snackbarMessage by viewModel.snackbarMessages.collectAsState() //스낵바 이벤트 상태
    val loading by viewModel.loading.collectAsState() //로딩상태
    val navigation by viewModel.navigateEvent.collectAsState()
    val imeState = rememberImeState() //키보드 상태
    val scrollState = rememberScrollState() //스크롤 상태
    var visiblePositionPicker by remember { mutableStateOf(false) } //포지션 선택 휠
    val snackbarHostState = remember { SnackbarHostState() } //스낵바 상태
    val visualMediaPickerLauncher = // 이미지 피커 런쳐
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.updateUserProfileImageUrl(profileImageUrl = uri.toString())
            }
        }
    //키보드 올라오면 버튼도 위로 올라오게
    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }
    //이벤트에 따른 스낵바 발생
    LaunchedEffect(key1 = snackbarMessage) {
        snackbarMessage?.let {message->
            val result = snackbarHostState.showSnackbar(message,actionLabel = "닫기", duration = SnackbarDuration.Short)
            when (result) {
                SnackbarResult.Dismissed -> {}
                SnackbarResult.ActionPerformed -> {}
            }
            viewModel.clearSnackbarMessage()
        }
    }
    //회원가입 성공 시 홈으로 이동
    LaunchedEffect(key1 = navigation) {
        when (navigation) {
            SignUpNavigation.Default -> {}
            SignUpNavigation.Home -> {
                context.startActivity(Intent(context,HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = background)
        ) {
            if(loading) {
                CircularProgressIndicator(modifier=Modifier.align(Alignment.Center))
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                //유저 프로필 정보 섹션
                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(174.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            visualMediaPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    painter = rememberAsyncImagePainter(
                        model = if (state.userProfileImageUrl == "") R.drawable.default_image else state.userProfileImageUrl
                    ),
                    contentDescription = "프로필 사진",
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(24.dp))
                //유저 닉네임 입력 섹션
                UserNickNameSection(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .align(Alignment.CenterHorizontally),
                    text = state.userNickName
                ) { nickName ->
                    viewModel.updateUserNickName(nickName = nickName)
                }

                Spacer(modifier = Modifier.height(48.dp))
                //유저 키 섹션
                UserHeightSection() { height ->
                    if (height != null) {
                        viewModel.updateUserHeight(height = height.toDouble())
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                //유저 몸무게 섹션
                UserWeightSection() { weight ->
                    if (weight != null) {
                        viewModel.updateUserWeight(weight = weight.toDouble())
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                //유저 포지션 섹션
                UserPositionSection(state.userPosition) {
                    visiblePositionPicker = !visiblePositionPicker
                }
                //포지션 눌리면 휠 보이게하기
                AnimatedVisibility(visible = visiblePositionPicker) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(color = wheelCOlor)
                    ) {
                        PositionData.positions.forEach { data ->
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .clickable {
                                        viewModel.updateUserPosition(data.first)
                                        visiblePositionPicker = false
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    text = data.first,
                                    color = if (state.userPosition == data.first) Color.White else textColor
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = data.second,
                                    color = if (state.userPosition == data.first) Color.White else textColor
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(3f))


                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .padding(bottom = 35.dp),
                    onClick = {
                        viewModel.validateInput()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor
                    ),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                ) {
                    Text(text = "확인", color = Color.White)
                }

            }
        }
    }
}


@Composable
fun UserNickNameSection(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit
) {
    CustomTextField(
        modifier = modifier.padding(start = 12.dp),
        text = text,
        placeholder = "닉네임을 입력해주세요",
        onTextChange = onTextChange,
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        isSingleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
        placeholderAlignment = TextAlign.End,
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = 24.sp),
    )
}

@Composable
fun UserHeightSection(
    onTextChange: (Double?) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var height by remember { mutableStateOf<Double?>(null) }

    CustomTextField(
        modifier = Modifier.fillMaxWidth(0.8f),
        text = text,
        placeholder = "키를 입력해주세요",
        onTextChange = { newText ->
            if (newText.isEmpty() || newText.matches(Regex("^[0-9]*\\.?[0-9]*\$"))) {
                height = newText.toDoubleOrNull()
                text = newText
                onTextChange(height)
                isError = newText.isNotEmpty() && height == null
            } else {
                isError = true
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedContainerColor = textFieldBackground,
            focusedContainerColor = textFieldBackground,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RectangleShape,
        isSingleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        ),
        isError = isError
    )
}


@Composable
fun UserWeightSection(
    onTextChange: (Double?) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var weight by remember { mutableStateOf<Double?>(null) }

    CustomTextField(
        modifier = Modifier.fillMaxWidth(0.8f),
        text = text,
        placeholder = "몸무게를 입력해주세요",
        onTextChange = { newText ->
            if (newText.isEmpty() || newText.matches(Regex("^[0-9]*\\.?[0-9]*\$"))) {
                weight = newText.toDoubleOrNull()
                text = newText
                onTextChange(weight)
                isError = newText.isNotEmpty() && weight == null
            } else {
                isError = true
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedContainerColor = textFieldBackground,
            focusedContainerColor = textFieldBackground,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RectangleShape,
        isSingleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        )
    )
}


@Composable
fun UserPositionSection(
    text: String,
    onClick: () -> Unit,
) {
    CustomTextField(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .clickable { onClick() },
        text = text,
        placeholder = "포지션을 선택해주세요",
        onTextChange = {},
        trailingIcon = ImageVector.vectorResource(id = R.drawable.dropdown),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedContainerColor = textFieldBackground,
            focusedContainerColor = textFieldBackground,
            disabledContainerColor = textFieldBackground,
            disabledTextColor = Color.White
        ),
        shape = RectangleShape,
        isSingleLine = true,
        enable = false
    )
}


