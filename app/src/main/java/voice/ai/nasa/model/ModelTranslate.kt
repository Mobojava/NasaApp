package voice.ai.nasa.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModelTranslate(
    @SerializedName("responseData")
    val responseData: ResponseData?
) : Parcelable {
    @Parcelize
    data class ResponseData(
        @SerializedName("match")
        val match: Float?,
        @SerializedName("translatedText")
        val translatedText: String?
    ) : Parcelable
}
