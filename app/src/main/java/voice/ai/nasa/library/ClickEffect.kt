package voice.ai.nasa.library

import android.view.View

object ClickEffect {

    fun apply(view: View, onClick: (() -> Unit)? = null) {
        view.animate()
            .scaleX(0.7f)
            .scaleY(0.7f)
            .setDuration(150)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .withEndAction {
                        onClick?.invoke()
                    }
                    .start()
            }
            .start()
    }
}
