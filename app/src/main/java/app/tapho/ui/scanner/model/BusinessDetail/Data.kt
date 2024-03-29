package app.tapho.ui.scanner.model.BusinessDetail

data class Data(
    val address: String,
    val always_open: String,
    val area: String,
    val average_rating: Int,
    val business_addon_id: String,
    val business_category_id: Any,
    val business_category_name: String,
    val business_name: String,
    val business_subcategory_id: Any,
    val business_subcategory_name: String,
    val business_tag: List<BusinessTag>,
    val business_type_id: Any,
    val business_type_name: String,
    val city: String,
    val code: String,
    val contacts: String,
    val created_at: String,
    val description: String,
    val email: String,
    val established_year: String,
    val floor: String,
    val fri_closing: Any,
    val fri_opening: Any,
    val gst: String,
    val id: String,
    val image: Any,
    val landmark: String,
    val latitude: String,
    val logo: Any,
    val longitude: String,
    val min_order_value: Any,
    val mon_closing: Any,
    val mon_opening: Any,
    val pancard: String,
    val pincode: String,
    val qr_code: String,
    val radius: Any,
    val rating_list: List<Any>,
    val sat_closing: Any,
    val sat_opening: Any,
    val status: String,
    val sun_closing: String,
    val sun_opening: String,
    val temparary_close: String,
    val thu_closing: Any,
    val thu_opening: Any,
    val tue_closing: Any,
    val tue_opening: Any,
    val unique_business_id: String,
    val user_id: String,
    val wed_closing: Any,
    val wed_opening: Any,
    val whatsapp_business: String
)