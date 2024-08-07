package com.dkproject.presentation.ui.screen.gameRecord

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.algolia.instantsearch.android.paging3.flow
import com.dkproject.domain.model.UserData
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.components.CustomTextField
import com.dkproject.presentation.ui.theme.BasketStatTheme
import com.dkproject.presentation.ui.theme.background
import com.dkproject.presentation.ui.theme.searchTextbg


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: UserSearchViewModel,
    onBack: () -> Unit = {},
    userClick: (UserDataUiModel) -> Unit = {}
) {
    val hitsPaging = viewModel.hitsPaginator.flow.collectAsLazyPagingItems()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "선수 검색", color = Color.White) },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = background),
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                }
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextField(
                modifier = Modifier.fillMaxWidth(0.9f),
                text = viewModel.searchBoxState.query,
                placeholder = "선수를 검색해주세요.",
                onTextChange = {
                     viewModel.searchBoxState.setText(it, submitQuery = true)
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(focusedContainerColor = searchTextbg,unfocusedContainerColor = searchTextbg,
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,unfocusedTextColor = Color.White),
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
                    UserSearchItem(modifier = Modifier.clickable { userClick(user) },user = user)
                }
            }
        }
    }
}


@Composable
fun UserSearchItem(
    modifier: Modifier = Modifier,
    user: UserDataUiModel
) {
    val context = LocalContext.current
    Row(modifier = modifier
        .padding(vertical = 6.dp, horizontal = 16.dp)
        .fillMaxWidth(),verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            model = ImageRequest.Builder(context)
                .data(user.userProfileImageUrl)
                .crossfade(true)
                .error(R.drawable.basic_image)
                .build(),
            contentDescription = "",
            contentScale = ContentScale.Crop)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = user.userNickName, color = Color.White, fontSize = 16.sp)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = user.userPosition, color = Color.White)
    }

}


@Composable
@Preview(showBackground = true)
private fun UserSearchScreenPreview() {
    BasketStatTheme {
        val viewModel: UserSearchViewModel = viewModel()
        UserSearchScreen(modifier = Modifier.background(background),
            viewModel = viewModel)
    }
}