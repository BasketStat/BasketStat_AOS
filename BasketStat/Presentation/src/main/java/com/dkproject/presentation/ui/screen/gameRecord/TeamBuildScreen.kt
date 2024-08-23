package com.dkproject.presentation.ui.screen.gameRecord

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dkproject.domain.model.UserRecord
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.theme.background


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamBuildScreen(
    recordViewModel: RecordViewModel,
    searchTeam: (Boolean) -> Unit = {},
    searchMember: (Boolean) -> Unit = {}
) {
    val uiState by recordViewModel.uiState.collectAsState()
    val snackbarMessage by recordViewModel.snackbarMessages.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() } //스낵바 상태

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
            recordViewModel.updateSnackbarClear()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Team Build",color = Color.White)}, navigationIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                   Icon(imageVector = Icons.Default.Clear, contentDescription = "exit",tint = Color.White)
                }
            },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = background))
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(0.5f))

            // Select Team Buttons
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SelectTeamButton(
                    modifier = Modifier.clickable {
                        searchTeam(true)
                    },
                    teamProfileUrl = uiState.homeTeam.teamProfileUrl,
                    teamName = uiState.homeTeam.teamName
                )
                Spacer(modifier = Modifier.padding(horizontal = 24.dp))

                SelectTeamButton(
                    modifier = Modifier.clickable {
                        searchTeam(false)
                    },
                    teamProfileUrl = uiState.awayTeam.teamProfileUrl,
                    teamName = uiState.awayTeam.teamName
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(modifier = Modifier.fillMaxWidth(0.8f))

            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TeamMemberSection(
                    modifier = Modifier.weight(1f),
                    memberList = uiState.userRecords.filter { it.isHomeTeam }) {
                    if (uiState.homeTeam.teamUid.isNotEmpty()) {
                        searchMember(true)
                    } else {
                        recordViewModel.updateSnackbarMessage("팀을 먼저 선택해 주세요.")
                    }
                }
                VerticalDivider(modifier = Modifier.height((36 + uiState.userRecords.size * 36).dp))
                TeamMemberSection(
                    modifier = Modifier.weight(1f),
                    memberList = uiState.userRecords.filter { !it.isHomeTeam }) {
                    if (uiState.awayTeam.teamUid.isNotEmpty()) {
                        searchMember(false)
                    } else {
                        recordViewModel.updateSnackbarMessage("팀을 먼저 선택해 주세요.")
                    }
                }
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(0.8f))

            Spacer(modifier = Modifier.weight(6f))

        }
    }
}

@Composable
fun SelectTeamButton(
    modifier: Modifier = Modifier,
    teamProfileUrl: String = "",
    teamName: String = ""
) {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box() {
            AsyncImage(
                modifier = modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = Color.Gray),
                model = ImageRequest.Builder(context)
                    .data(teamProfileUrl)
                    .crossfade(true)
                    .error(R.drawable.baseline_add_24)
                    .placeholder(R.drawable.baseline_add_24)
                    .build(),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(teamName.ifEmpty { "Select Team" }, color = Color.White)
    }
}


@Composable
fun TeamMemberSection(
    modifier: Modifier = Modifier,
    memberList: List<UserRecord> = emptyList(),
    addMember: () -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        items(memberList) { user ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = user.backNumber.toString(), modifier = Modifier.weight(1f), color = Color.White)
                Text(text = user.userName, color = Color.White)
            }
        }

        item {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = addMember,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            ) {
                Text("+", color = Color.White)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun TeamBuildScreenPreview() {
    TeamBuildScreen(recordViewModel = RecordViewModel())
}