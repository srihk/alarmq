package srihk.alarmq

interface MessageDisplayer {
    fun showShort(message: String)
    fun showLong(message: String)
}