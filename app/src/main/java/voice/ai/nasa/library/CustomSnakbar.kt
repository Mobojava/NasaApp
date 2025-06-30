package voice.ai.nasa.library

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import voice.ai.nasa.R
class CustomSnakbar(val context: Context, val rootView: View, val text: String) {

    @SuppressLint("RestrictedApi")
    fun showSnakbar() {
        val snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG)

        val customView = LayoutInflater.from(context).inflate(R.layout.custom_snackbar, null)
        customView.findViewById<TextView>(R.id.txt_snakbar).text = text

        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.removeAllViews()
        snackbarLayout.addView(customView)

        snackbar.show()
    }
}
