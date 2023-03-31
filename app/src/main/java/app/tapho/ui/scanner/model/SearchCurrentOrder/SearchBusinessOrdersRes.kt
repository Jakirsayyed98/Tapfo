package app.tapho.ui.scanner.model.SearchCurrentOrder

import app.tapho.network.BaseRes

data class SearchBusinessOrdersRes(
    val `data`: List<Data>,
):BaseRes()