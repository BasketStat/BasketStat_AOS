package com.dkproject.domain.model

import java.util.Date
import java.util.UUID


data class Game(
    var gameId: String = UUID.randomUUID().toString(),
    var gameName: String = "",
    var homeTeamUid: String = "",
    var homeScore: Int = 0,
    var awayTeamUid: String = "",
    var awayScore: Int = 0,
    var date: Date = Date(),
    var usersUid: List<String> = emptyList(),
    var usersRecord: List<String> = emptyList()
)




data class SelectedPlayer(
    var playerUid: String,
    var isHomeTeam: Boolean
)


data class UserRecord(
    var userRecordId: String,
    var date:Date,
    var backNumber: Int,            //게임 시 유저 등넘버
    var gameId: String ,            //게임 id
    var userUid: String ,           //Uid
    var isHomeTeam: Boolean,        // true이면 홈팀, false이면 어웨이팀
    var userName: String,           //유저 이름
    var twoPointFail: Int = 0,      //2점 실패
    var twoPointSuccess: Int = 0,   //2점 성공
    var threePointFail: Int = 0,    //3점 실패,
    var threePointSuccess: Int = 0, //3점 성공
    var freeThrowFail: Int = 0,     //자유투 실패
    var freeThrowSuccess: Int = 0,  //자유투 성공
    var assist: Int = 0,            //어시스트
    var rebound: Int = 0,           //리바운드
    var block: Int = 0,             //블락
    var steal: Int = 0,             //스틸
    var foul: Int = 0,              //파울
    var turnOver: Int = 0,          //턴오버
)








