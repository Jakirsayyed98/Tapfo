package app.tapho.ui.Faqs.Model

data class FaqQuestion(
    val answer: String,
    val created_at: String,
    val faq_id: String,
    val id: String,
    val question: String,
    val status: String
)