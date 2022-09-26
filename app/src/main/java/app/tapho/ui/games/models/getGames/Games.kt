package app.tapho.ui.games.models.getGames

import app.tapho.ui.games.models.getGames.Data

open class Games(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String
)