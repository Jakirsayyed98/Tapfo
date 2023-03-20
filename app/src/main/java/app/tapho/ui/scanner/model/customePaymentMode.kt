package app.tapho.ui.scanner.model

data class customePaymentMode(
    val id:String,
    val name:String,
    val discription:String,
    val image:String,
    val status:String,
    var checked:Boolean,
)
