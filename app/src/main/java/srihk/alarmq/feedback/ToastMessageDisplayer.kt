package srihk.alarmq.feedback

import android.content.Context
import android.widget.Toast

class ToastMessageDisplayer(private val context: Context): MessageDisplayer {
    private fun show(message: String, toastLength: Int) {
        Toast.makeText(context.applicationContext, message, toastLength).show()
    }

    override fun showShort(message: String) {
        show(message, Toast.LENGTH_SHORT)
    }

    override fun showLong(message: String) {
        show(message, Toast.LENGTH_LONG)
    }
}