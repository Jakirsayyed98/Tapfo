package app.tapho.ui.Faqs.Model

data class Data(
    val created_at: String,
    val faq_question: List<FaqQuestion>,
    val id: String,
    val name: String,
    val status: String
)