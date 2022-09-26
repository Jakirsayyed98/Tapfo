package app.instagst.ui.interfaces

interface LoaderListener {
    fun showLoader()
    fun dismissLoader()
    fun showMess(mess:String?)
}