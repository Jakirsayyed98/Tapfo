package app.tapho.ui.games.models.GamezopCategory

import app.tapho.ui.games.models.getGames.Data


data class Data(
    val Games : List<Data>,
    val created_at: String,
    val id: String,
    val image: String,
    val name: String,
    val status: String,
    var isSelected: Boolean,
)