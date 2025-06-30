package voice.ai.nasa.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import voice.ai.nasa.model.ModelTranslate
import voice.ai.nasa.model.PlanetModel

interface ApiService {


    @GET("apod")
    fun getPlanet(
        @Query("api_key") api_key: String,
        @Query("count") count: String
    ) :Call<List<PlanetModel>>


    @GET("get")
    fun getTranslate(
        @Query("q") query: String,
        @Query("langpair") langPair: String
    ):Call<ModelTranslate>

}