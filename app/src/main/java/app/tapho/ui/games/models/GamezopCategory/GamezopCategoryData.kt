package app.tapho.ui.games.models.GamezopCategory

import app.tapho.ui.games.models.getGames.Games
import app.tapho.ui.model.PromotedApp

data class GamezopCategoryData(
    val gamesdata: List<Data>?,
    val data: List<Data>,
    val errorCode: String,
    val errorMsg: String,
    var games: ArrayList<app.tapho.ui.games.models.getGames.Data>?,

)