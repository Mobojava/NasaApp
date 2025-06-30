package voice.ai.nasa.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import voice.ai.nasa.model.PlanetModel

@Database(version = 1, exportSchema = false, entities = [PlanetModel::class])
abstract class MyDatabase : RoomDatabase() {

    abstract val planeDao: PlanetDao

    companion object {

        private val dataBase: MyDatabase? = null
        fun getDatabase(context: Context): MyDatabase {
            var instance = dataBase
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "myDatabase.dp"
                ).allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }

}