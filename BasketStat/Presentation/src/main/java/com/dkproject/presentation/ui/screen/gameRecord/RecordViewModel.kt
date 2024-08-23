package com.dkproject.presentation.ui.screen.gameRecord

import androidx.lifecycle.ViewModel
import com.dkproject.domain.model.Game
import com.dkproject.domain.model.UserRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

class RecordViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()

    private val _snackbarMessages = MutableStateFlow<String?>(null)
    val snackbarMessages = _snackbarMessages.asStateFlow()

    fun updateHomeTeam(team: TeamDataUiModel) {
        val game = uiState.value.game.copy(homeTeamUid = team.teamUid)
        _uiState.update { it.copy(game = game, homeTeam = team) }
    }

    fun updateAwayTeam(team: TeamDataUiModel) {
        val game = uiState.value.game.copy(awayTeamUid = team.teamUid)
        _uiState.update { it.copy(awayTeam = team) }
    }

    fun addMember(userUid: String, userName: String, backNumber: Int, homeaway: Boolean) {
        val userRecordId = UUID.randomUUID().toString()
        val userRecord = UserRecord(
            userRecordId = userRecordId,
            date = uiState.value.game.date,
            backNumber = backNumber,
            gameId = uiState.value.game.gameId,
            userUid = userUid,
            isHomeTeam = homeaway,
            userName = userName
        )
        val newUserRecord = uiState.value.game.usersRecord.toMutableList()
        newUserRecord.add(userRecordId)
        val newUserUidList = uiState.value.game.usersUid.toMutableList()
        newUserUidList.add(userUid)

        val userRecords = uiState.value.userRecords.toMutableList()
        userRecords.add(userRecord)
        _uiState.update {
            it.copy(
                game = uiState.value.game.copy(
                    usersUid = newUserUidList,
                    usersRecord = newUserRecord
                ), userRecords = userRecords
            )
        }

    }

    fun updateSnackbarMessage(message: String) {
        _snackbarMessages.update { message }
    }

    fun updateSnackbarClear() {
        _snackbarMessages.update { null }
    }


}


data class RecordUiState(
    var game: Game = Game(),
    var userRecords: List<UserRecord> = emptyList(),
    var homeTeam: TeamDataUiModel = TeamDataUiModel(),
    var awayTeam: TeamDataUiModel = TeamDataUiModel(),
)