package com.dkproject.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dkproject.domain.model.UserData
import com.dkproject.presentation.ui.screen.gameRecord.UserDataUiModel
import com.dkproject.presentation.ui.screen.gameRecord.UserSearchScreen
import com.dkproject.presentation.ui.screen.gameRecord.UserSearchViewModel


//네비게이션 경로
enum class GameRecordNavigationType {
    TeamBulid,
    UserSearch,
    GameRecord
}


@Composable
fun GameRecordNavigation(
    navController: NavHostController = rememberNavController(),
    onBack: () -> Unit = {},
    userClick: (UserDataUiModel) -> Unit = {}
) {

    NavHost(navController = navController, startDestination = GameRecordNavigationType.UserSearch.name) {
        //팀 빌드 스크린
        composable(route = GameRecordNavigationType.TeamBulid.name) {

        }
        //유저 검색 스크린
        composable(route = GameRecordNavigationType.UserSearch.name) {
            val searchViewModel: UserSearchViewModel = hiltViewModel()
            UserSearchScreen(viewModel = searchViewModel, onBack = onBack) { user->
                userClick(user)
            }


        }
        //게임 기록 스크린
        composable(route = GameRecordNavigationType.GameRecord.name) {

        }

    }
}