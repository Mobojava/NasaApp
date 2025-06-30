package voice.ai.nasa.ui

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.google.android.material.snackbar.Snackbar
import voice.ai.nasa.R
import voice.ai.nasa.adapter.PlanetAdapter
import voice.ai.nasa.databinding.ActivityMainBinding
import voice.ai.nasa.library.ClickEffect
import voice.ai.nasa.library.CustomSnakbar
import voice.ai.nasa.model.PlanetModel
import voice.ai.nasa.network.ApiManager

//  emplemation Room
// 1=> Enttiy
// 2=> Dao
// => Database
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val apiManager = ApiManager()
    private var toggle = false
    var isLoading = false
    var them = true
    private lateinit var adapter: PlanetAdapter
    val planetList = mutableListOf<PlanetModel>()
    var firstRun = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn4.setOnClickListener {
            ClickEffect.apply(it)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, Favorites::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }, 800)
        }

        binding.btn7.setOnClickListener {
            ClickEffect.apply(it)
            Handler(Looper.getMainLooper()).postDelayed({ finish() }, 800)
        }

        binding.icMenu.setOnClickListener {
            ClickEffect.apply(it)
            Handler(Looper.getMainLooper()).postDelayed({
                fun_CloseOpenMenu()
            }, 800)
        }
        click()
        binding.changeThem.setOnClickListener {
            ClickEffect.apply(it)
            Handler(Looper.getMainLooper()).postDelayed({
                fun_ChangeThem()
            }, 800)
        }


        adapter = PlanetAdapter(planetList, object : PlanetAdapter.EventClick<PlanetModel> {
            override fun Click(data: PlanetModel) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("planet_data", data)
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }, 200)
            }
        })

        binding.recyclerMain.adapter = adapter
        binding.recyclerMain.layoutManager = GridLayoutManager(this, 2)

        loadMoreItems()

        binding.recyclerMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                val threshold = 5

                if (!isLoading && totalItemCount <= lastVisibleItemPosition + threshold) {
                    loadMoreItems()
                }
            }
        })
    }

    private fun loadMoreItems() {
        isLoading = true

        apiManager.getPlanet(object : ApiManager.ApiCallback<List<PlanetModel>> {
            override fun onSuccess(data: List<PlanetModel>) {
                val oldSize = planetList.size
                planetList.addAll(data)
                adapter.notifyItemRangeInserted(oldSize, data.size)
                isLoading = false

                Handler(Looper.getMainLooper()).postDelayed({
                    binding.animationView.visibility = View.INVISIBLE
                    binding.recyclerMain.visibility = View.VISIBLE
                }, 2000)
                if (firstRun >= 2){
                    CustomSnakbar(this@MainActivity,binding.root, "Get More Data ...").showSnakbar()

                }
                firstRun ++

            }

            override fun onError(errorMessage: String) {
                if (firstRun >= 3){
                    CustomSnakbar(this@MainActivity, binding.root, "Error server").showSnakbar()
                }
                binding.animationView.setAnimation(R.raw.notfound)
                isLoading = false
            }
        })
    }

    private fun fun_ChangeThem() {
        if (them) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            them = false
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            them = true
        }
    }

    private fun click() {
        val buttons = listOf(
            binding.btn1,
            binding.btn2,
            binding.btn3,
            binding.btn5,
            binding.btn6
        )
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                ClickEffect.apply(button)
            }
        }
    }

    private fun fun_CloseOpenMenu() {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val translationX = screenWidth * 0.8f

        binding.constraintLayout2.animate()
            .translationX(if (!toggle) translationX else 0f)
            .setDuration(300)
            .start()

        toggle = !toggle
    }


}