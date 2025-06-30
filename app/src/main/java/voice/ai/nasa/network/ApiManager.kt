package voice.ai.nasa.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import voice.ai.nasa.model.ModelTranslate
import voice.ai.nasa.model.PlanetModel

const val BASE_URL = "https://api.nasa.gov/planetary/"
const val API_KEY = "kAE7kSJSzMWTfk3nvb4Qz7429BmAJizo6kLHVqeZ" // You can go to NASA's website and get your personal API key.

class ApiManager {
    private val apiService: ApiService

    init {

        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun getPlanet(callback: ApiCallback<List<PlanetModel>>) {

        apiService.getPlanet(API_KEY, "10").enqueue(object : Callback<List<PlanetModel>> {
            override fun onResponse(
                call: Call<List<PlanetModel>>,
                response: Response<List<PlanetModel>>
            ) {
                val body = response.body()!!
                callback.onSuccess(body)
            }

            override fun onFailure(call: Call<List<PlanetModel>>, t: Throwable) {
                callback.onError(t.message.toString())
            }

        })
    }

    interface ApiCallback<T> {
        fun onSuccess(data: T)
        fun onError(errorMessage: String)
    }
}