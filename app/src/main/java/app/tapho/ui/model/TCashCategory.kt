package app.tapho.ui.model

class TCashCategory(
    var all_user: String?,
    var category: String?,
    var id: String?,
    var merchant_payout_id: String?,
    var new_user: String?,
    var old_user: String?,
    var payout_type: String?
){
    fun getAllUser():String?{
        return if(all_user.isNullOrEmpty() || all_user=="0")
            null
        else
            all_user
    }

    fun getNewUser():String?{
        return if(new_user.isNullOrEmpty() || new_user=="0")
            null
        else
            new_user
    }

    fun getOldUser():String?{
        return if(old_user.isNullOrEmpty() || old_user=="0")
            null
        else
            old_user
    }
}