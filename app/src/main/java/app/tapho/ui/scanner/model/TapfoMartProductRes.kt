package app.tapho.ui.scanner.model

data class TapfoMartProductRes(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String
)