package com.dkproject.presentation.ui.screen.gameRecord

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.algolia.instantsearch.android.paging3.flow
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.components.CustomTextField
import com.dkproject.presentation.ui.theme.background
import com.dkproject.presentation.ui.theme.cardItembg
import com.dkproject.presentation.ui.theme.searchTextbg


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: TeamSearchViewModel = hiltViewModel(),
    onBack: () -> Unit,
    teamSelected: (TeamDataUiModel) -> Unit,
) {
    val hitsPaging = viewModel.hitsPaginator.flow.collectAsLazyPagingItems()
    hitsPaging.loadState
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "팀 검색", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = background),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            )
        }) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(background)) {
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
                    placeholder = "팀을 검색해주세요.",
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
                        unfocusedTextColor = Color.White
                    ),
                    leadingIcon = Icons.Default.Search,
                    isSingleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(hitsPaging.itemSnapshotList.items, key = { it.teamUid }) { team ->
                        TeamSearchItem(
                            modifier = Modifier,
                            team = team,
                            onTeamSelected = { teamSelected(it) }
                        )
                    }
                }
            }
        }

    }
}


@Composable
fun TeamSearchItem(
    modifier: Modifier = Modifier,
    team: TeamDataUiModel,
    onTeamSelected: (TeamDataUiModel) -> Unit = {},
) {
    val context = LocalContext.current
    ElevatedCard(
        onClick = { onTeamSelected(team) },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .padding(vertical = 6.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(cardItembg)
                .fillMaxSize()
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .clip(RoundedCornerShape(8.dp)),
                model = ImageRequest.Builder(context)
                    .data(team.teamProfileUrl)
                    .crossfade(true)
                    .error(R.drawable.basic_image)
                    .build(),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                text = team.teamName, color = Color.White, fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

}

@Composable
@Preview(showBackground = true)
private fun TeamSearchScreenPreview() {
    TeamSearchScreen(onBack = {}) {}
}