package app.tapho.ui.login.model

import app.tapho.network.BaseRes

class LoginRes(
    var data: List<LoginData>?,
    var otp: String?
) : BaseRes() {

}