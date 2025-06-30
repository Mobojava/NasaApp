package voice.ai.nasa.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import voice.ai.nasa.model.ModelTranslate
import voice.ai.nasa.model.PlanetModel

const val BASE_URL_TRANSLATE = "https://api.mymemory.translated.net/"

class ApiManagerTranslate {
    private val apiService: ApiService

    init {

        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL_TRANSLATE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun getTranslate(text: String, callback: ApiCallbackTranslate<ModelTranslate>) {
        apiService.getTranslate(text, "en|fa").enqueue(object : Callback<ModelTranslate> {
            override fun onResponse(
                call: Call<ModelTranslate>,
                response: Response<ModelTranslate>
            ) {
                val data = response.body()
                data?.let { callback.onSuccess(it) }
            }

            override fun onFailure(call: Call<ModelTranslate>, t: Throwable) {
                callback.onError(t.message.toString())
            }

        })
    }

    interface ApiCallbackTranslate<T> {
        fun onSuccess(data: T)
        fun onError(errorMessage: String)
    }


}