package app.tapho.Connection

interface ConnectivityListener {
    fun onNetworkConnectionChanged(isConnected: Boolean)
}