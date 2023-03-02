package app.tapho.ui.model

import app.tapho.ui.model.MiniApp

data class MinisRecentsRes(
    val `data`: ArrayList<MiniApp>,
    val errorCode: String,
    val errorMsg: String
)