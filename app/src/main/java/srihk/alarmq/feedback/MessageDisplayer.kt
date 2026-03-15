package srihk.alarmq.feedback

interface MessageDisplayer {
    fun showShort(message: String)
    fun showLong(message: String)
}