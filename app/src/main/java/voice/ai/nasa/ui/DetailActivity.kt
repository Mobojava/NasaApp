package voice.ai.nasa.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.varunest.sparkbutton.SparkEventListener
import voice.ai.nasa.databinding.ActivityDetailBinding
import voice.ai.nasa.model.ModelTranslate
import voice.ai.nasa.model.PlanetModel
import voice.ai.nasa.network.ApiManagerTranslate
import voice.ai.nasa.room.MyDatabase
import voice.ai.nasa.room.PlanetDao
import kotlin.properties.Delegates

class DetailActivity() : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding
    var isTranslate = false
    lateinit var planet: PlanetModel
    lateinit var position: String
    val apiManager = ApiManagerTranslate()
    lateinit var title: String
    lateinit var explanation: String
    var title_Translate: String by Delegates.observable("not") { _, old, new ->
        if (isTranslate) {
            animateText(binding.title, new.trim())
        }
    }
    var description_Translate: String by Delegates.observable("not") { _, old, new ->
        if (isTranslate) {
            animateText(binding.description, new.trim())
        }
    }
    private var currentPart = 0
    private lateinit var explanationParts: List<String>
    lateinit var planetDao: PlanetDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        planet = intent.getParcelableExtra<PlanetModel>("planet_data")!!

        title = planet.title.toString()
        explanation = planet.explanation.toString()
        planetDao = MyDatabase.getDatabase(this@DetailActivity).planeDao


        setData()
        animation()
        funTranslate()
        btnSaveDatabase()
        existInDatabase()
        binding.btnTrnslate.setOnClickListener {
            isTranslate = true
            if (title_Translate == "not" || description_Translate == "not") {
                binding.title.text = "لطفا کمی صبر کنید درحال ترجمه هستم"
                binding.description.text = "ترجمه . . ."
            } else {
                binding.title.text = title_Translate
                binding.description.text = description_Translate
            }
        }


    }


    private fun existInDatabase() {
        val exists = planetDao.existsPlanet(
            planet.date,
            planet.hdurl,
            planet.url
        )

        if (exists) {
            binding.sparkButton.isChecked = true
        } else {
            binding.sparkButton.isChecked = false
        }

    }

    private fun btnSaveDatabase() {
        binding.sparkButton.setEventListener(object : SparkEventListener {
            override fun onEvent(button: ImageView?, buttonState: Boolean) {
                if (buttonState) {
                    savedDatabase(true)
                } else {
                    savedDatabase(false)
                }
            }

            private fun savedDatabase(b: Boolean) {
                if (b) {
                    planetDao.InsertPlanet(
                        PlanetModel(
                            date = planet.date,
                            explanation = planet.explanation,
                            hdurl = planet.hdurl,
                            mediaType = planet.mediaType,
                            serviceVersion = planet.serviceVersion,
                            title = planet.title,
                            url = planet.url
                        )

                    )

                } else {
                    planetDao.deletePlanet(
                        date = planet.date,
                        hdurl = planet.hdurl,
                        url = planet.url
                    )
                }
            }

            override fun onEventAnimationEnd(button: ImageView?, buttonState: Boolean) {
            }

            override fun onEventAnimationStart(button: ImageView?, buttonState: Boolean) {
            }
        })
    }

    private var currentStep = 0

    private fun funTranslate() {
        when (currentStep) {
            0 -> {
                apiManager.getTranslate(
                    title,
                    object : ApiManagerTranslate.ApiCallbackTranslate<ModelTranslate> {
                        override fun onSuccess(data: ModelTranslate) {
                            title_Translate = data.responseData?.translatedText ?: "ترجمه نشد"
                            currentStep++
                            funTranslate()
                        }

                        override fun onError(errorMessage: String) {
                            title_Translate = "خطا در ترجمه عنوان"
                            currentStep++
                            funTranslate()
                        }
                    })
            }

            1 -> {
                startTranslation(explanation)
            }
        }
    }

    private fun startTranslation(explanation: String) {
        explanationParts = explanation.chunked(500)
        description_Translate = ""
        currentPart = 0
        translateNextPart()
    }

    private fun translateNextPart() {
        if (currentPart >= explanationParts.size) {
            return
        }

        val partText = explanationParts[currentPart]

        apiManager.getTranslate(
            partText,
            object : ApiManagerTranslate.ApiCallbackTranslate<ModelTranslate> {
                override fun onSuccess(data: ModelTranslate) {
                    appendTranslation(data.responseData?.translatedText ?: "")
                }

                override fun onError(errorMessage: String) {
                    appendTranslation("[ترجمه نشد]")
                }
            })
    }

    private fun appendTranslation(translated: String) {
        description_Translate += translated + " "
        currentPart++
        translateNextPart()
    }

    private fun animation() {

        binding.card.visibility = View.INVISIBLE
        binding.card2.visibility = View.INVISIBLE
        binding.sparkButton.visibility = View.INVISIBLE
        binding.neumorphCardView.visibility = View.INVISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            animateFromRight(binding.card)
        }, 1000)

        Handler(Looper.getMainLooper()).postDelayed({
            animateFromRight(binding.neumorphCardView)
            animateFromRight(binding.sparkButton)
        }, 1200)

        Handler(Looper.getMainLooper()).postDelayed({
            animateFromRight(binding.card2)
        }, 1400)

    }

    private fun animateFromRight(view: View) {
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f
        )
        animation.duration = 500
        animation.fillAfter = true
        view.visibility = View.VISIBLE
        view.startAnimation(animation)
    }

    private fun setData() {
        Glide.with(this)
            .load(planet.url)
            .into(binding.neumorphImageView)

        binding.title.text = planet.title
        binding.description.text = planet.explanation
    }

    override fun onPause() {
        super.onPause()
        binding.card.animate().cancel()
        binding.card2.animate().cancel()
        binding.neumorphCardView.animate().cancel()
    }

    private fun animateText(textView: TextView, fullText: String) {
        textView.text = ""
        var index = 0
        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                if (index <= fullText.length) {
                    textView.text = fullText.substring(0, index)
                    index++
                    handler.postDelayed(this, 10)
                }
            }
        }

        handler.post(runnable)
    }

}