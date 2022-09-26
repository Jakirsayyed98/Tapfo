package app.tapho.ui.localbizzUI.Model.getBusinessListForBusinessPerson

data class Rating(
    val business_id: String,
    val business_user_id: String,
    val created_at: String,
    val id: String,
    val rating: String,
    val review: String,
    val user_id: String,
    val user_profile_detail: List<UserProfileDetail>
)