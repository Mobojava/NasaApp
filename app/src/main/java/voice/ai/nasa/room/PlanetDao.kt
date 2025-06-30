package voice.ai.nasa.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import voice.ai.nasa.model.PlanetModel

@Dao
interface PlanetDao {

    @Insert
    fun InsertPlanet (planetModel: PlanetModel)

    @Query("""
    DELETE FROM table_planet 
    WHERE date = :date AND 
          hdurl = :hdurl AND 
          url = :url
""")
    fun deletePlanet(date: String?, hdurl: String?, url: String?)


    @androidx.room.Query( " SELECT * FROM table_planet " )
    fun getAllPlanet() :List<PlanetModel>

    @Query("""
    SELECT EXISTS(
        SELECT 1 FROM table_planet 
        WHERE 
            date = :date AND
            hdurl = :hdurl AND 
            url = :url
    )
""")
    fun existsPlanet(
        date: String?,
        hdurl: String?,
        url: String?
    ): Boolean

}