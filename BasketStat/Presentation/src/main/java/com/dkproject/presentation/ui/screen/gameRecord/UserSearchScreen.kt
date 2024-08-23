package com.dkproject.presentation.ui.screen.gameRecord

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.algolia.instantsearch.android.paging3.flow
import com.dkproject.domain.model.UserData
import com.dkproject.domain.model.UserRecord
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.components.CustomTextField
import com.dkproject.presentation.ui.components.SelectNumberDialog
import com.dkproject.presentation.ui.theme.BasketStatTheme
import com.dkproject.presentation.ui.theme.background
import com.dkproject.presentation.ui.theme.cardItembg
import com.dkproject.presentation.ui.theme.searchTextbg


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSearchScreen(
    modifier: Modifier = Modifier,
    recordViewModel: RecordViewModel,
    viewModel: UserSearchViewModel = hiltViewModel(),
    homeAway: Boolean,
    onBack: () -> Unit = {},
    userClick: (UserDataUiModel) -> Unit = {}
) {
    val recordState by recordViewModel.uiState.collectAsState()
    val hitsPaging = viewModel.hitsPaginator.flow.collectAsLazyPagingItems()
    var dialogVisible by remember { mutableStateOf(false) }
    val snackbarMessage by viewModel.snackbarMessages.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() } //스낵바 상태
    var selectedMember by remember { mutableStateOf(UserDataUiModel()) }
    LaunchedEffect(key1 = snackbarMessage) {
        snackbarMessage?.let {
            val result = snackbarHostState.showSnackbar(
                message = it,
                actionLabel = "닫기",
                duration = SnackbarDuration.Short
            )
            when (result) {
                SnackbarResult.Dismissed -> {}
                SnackbarResult.ActionPerformed -> {}
            }
            viewModel.updateSnackbarClear()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "선수 검색", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = background),
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            )
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(background)
        ) {
            if (dialogVisible) {
                SelectNumberDialog(onDismiss = { dialogVisible = false }) { userbackNumber ->
                    if (recordState.userRecords.find { it.backNumber == userbackNumber } == null) {
                        recordViewModel.addMember(
                            userUid = selectedMember.userUid,
                            userName = selectedMember.userNickName,
                            backNumber = userbackNumber,
                            homeaway = homeAway
                        )
                        dialogVisible = false
                        onBack()
                    } else {
                        dialogVisible = false
                        viewModel.updateSnackbarMessage("해당 등 번호는 이미 존재 합니다.")
                    }
                }
            }
            if (hitsPaging.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //검색 섹션
                CustomTextField(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    text = viewModel.searchBoxState.query,
                    placeholder = "선수를 검색해주세요.",
                    onTextChange = {
                        viewModel.searchBoxState.setText(it, submitQuery = true)
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = searchTextbg,
                        unfocusedContainerColor = searchTextbg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledContainerColor = searchTextbg,
                        errorContainerColor = searchTextbg
                    ),
                    leadingIcon = Icons.Default.Search,
                    isSingleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))

                LazyColumn() {
                    items(hitsPaging.itemSnapshotList.items, key = { it.userUid }) { user ->
                        UserSearchItem(
                            modifier = Modifier,
                            user = user,
                            onClick = {
                                if (recordState.game.usersUid.contains(it.userUid)) {
                                    viewModel.updateSnackbarMessage("해당 선수가 이미 존재합니다.")
                                } else {
                                    selectedMember = it
                                    dialogVisible = true
                                }
                            }
                        )
                    }
                }//endLayColumn
            }//endColumn
        } //endBox
    }//endScaffold
}//end fun


@Composable
fun UserSearchItem(
    modifier: Modifier = Modifier,
    user: UserDataUiModel,
    onClick: (UserDataUiModel) -> Unit = {}
) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth(0.9f)
            .height(80.dp),
        colors = CardDefaults.cardColors(containerColor = cardItembg),
        onClick = { onClick(user) },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    model = ImageRequest.Builder(context)
                        .data(user.userProfileImageUrl)
                        .crossfade(true)
                        .error(R.drawable.basic_image)
                        .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(text = user.userNickName, color = Color.White, fontSize = 16.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = user.userPosition, color = Color.White)
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
private fun UserSearchScreenPreview() {
    BasketStatTheme {
        val viewModel: UserSearchViewModel = viewModel()
        UserSearchScreen(
            modifier = Modifier.background(background),
            recordViewModel = RecordViewModel(),
            homeAway = false,
            viewModel = viewModel
        )
    }
}