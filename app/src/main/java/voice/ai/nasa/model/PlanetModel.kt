package voice.ai.nasa.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "table_planet")
data class PlanetModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String?,
    val explanation: String?,
    val hdurl: String?,
    val mediaType: String?,
    val serviceVersion: String?,
    val title: String?,
    val url: String?
) : Parcelable
