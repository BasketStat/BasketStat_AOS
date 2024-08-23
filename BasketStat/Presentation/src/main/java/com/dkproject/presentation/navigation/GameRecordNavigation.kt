package com.dkproject.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dkproject.domain.model.UserData
import com.dkproject.presentation.ui.screen.gameRecord.RecordViewModel
import com.dkproject.presentation.ui.screen.gameRecord.TeamBuildScreen
import com.dkproject.presentation.ui.screen.gameRecord.TeamSearchScreen
import com.dkproject.presentation.ui.screen.gameRecord.UserDataUiModel
import com.dkproject.presentation.ui.screen.gameRecord.UserSearchScreen
import com.dkproject.presentation.ui.screen.gameRecord.UserSearchViewModel


//네비게이션 경로
enum class GameRecordNavigationType {
    TeamBulid,
    TeamSearch,
    UserSearch,
    GameRecord
}

enum class HomeSelected {
    Home,
    AWAY,
}


@Composable
fun GameRecordNavigation(
    navController: NavHostController = rememberNavController(),
) {
    var recordViewModel: RecordViewModel = hiltViewModel()
    var homeTeam by remember { mutableStateOf(false) }
    var homeTeamMember by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = GameRecordNavigationType.TeamBulid.name
    ) {
        //팀 빌드 스크린
        composable(route = GameRecordNavigationType.TeamBulid.name) {
            TeamBuildScreen(recordViewModel = recordViewModel, searchTeam = { homeaway ->
                homeTeam = homeaway //true일 시 홈팀, false 일 시 어웨이 팀
                navController.navigate(GameRecordNavigationType.TeamSearch.name)
            }, searchMember = { homeMember->
                homeTeamMember = homeMember
                navController.navigate(GameRecordNavigationType.UserSearch.name)
            })
        }

        composable(route = GameRecordNavigationType.TeamSearch.name) {
            TeamSearchScreen(onBack = { navController.popBackStack() }) { teamData ->
                navController.popBackStack()
                if (homeTeam) {
                    recordViewModel.updateHomeTeam(teamData)
                } else {
                    recordViewModel.updateAwayTeam(teamData)
                }
            }
        }

        //유저 검색 스크린
        composable(route = GameRecordNavigationType.UserSearch.name) {
            UserSearchScreen(recordViewModel = recordViewModel, homeAway = homeTeamMember, onBack = { navController.popBackStack() }) { user ->

            }


        }
        //게임 기록 스크린
        composable(route = GameRecordNavigationType.GameRecord.name) {

        }

    }
}