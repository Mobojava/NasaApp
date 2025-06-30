package voice.ai.nasa.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import voice.ai.nasa.R
import voice.ai.nasa.adapter.adapterFavorite
import voice.ai.nasa.databinding.FavoritesBinding
import voice.ai.nasa.library.ClickEffect
import voice.ai.nasa.model.PlanetModel
import voice.ai.nasa.room.MyDatabase
import voice.ai.nasa.room.PlanetDao

class Favorites : AppCompatActivity() {
    lateinit var binding: FavoritesBinding
    lateinit var planets: List<PlanetModel>
    lateinit var adapterFavorite: adapterFavorite
    lateinit var planetDao: PlanetDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val planetDao = MyDatabase.getDatabase(this).planeDao

        planets = planetDao.getAllPlanet()

        if(planets.isNotEmpty()){
            Handler(Looper.getMainLooper()).postDelayed({
                binding.animationView.visibility = View.GONE
                binding.recyclerFavorite.visibility = View.VISIBLE
            },1000)
        }else{
            binding.animationView.setAnimation( R.raw.notfound )
        }

        adapterFavorite = adapterFavorite(
            planets,
            object : adapterFavorite.eventClick<PlanetModel> {
                override fun Click(data: PlanetModel, position: String) {
                    val intent = Intent(this@Favorites, DetailActivity::class.java)
                    intent.putExtra("planet_data", data)
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }, 200)
                }

            })
        binding.recyclerFavorite.adapter = adapterFavorite
        binding.recyclerFavorite.layoutManager = GridLayoutManager(this, 2)


    }

}