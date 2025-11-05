package uz.gita.firstlesson.dictionaryapp.screens.about_screen

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uz.gita.firstlesson.dictionaryapp.R
import uz.gita.firstlesson.dictionaryapp.databinding.AboutScreenBinding
import uz.gita.firstlesson.dictionaryapp.room.WordEntity
import java.util.*
class AboutScreen : Fragment(), AboutContract.View {
    private var _binding: AboutScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: AboutContract.Presenter
    private lateinit var textToSpeech: TextToSpeech
    private var currentWord: WordEntity? = null

    private val wordId: Int by lazy {
        requireArguments().getInt("word_id")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AboutScreenBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter = AboutPresenter(this)
        presenter.loadWordData(wordId)
        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.US
            }
        }

        binding.buttonBack.setOnClickListener {
            presenter.onClickBack()
        }

        binding.buttonFavourite.setOnClickListener {
            presenter.onClickFavourite()
        }

        binding.buttonSpeaker.setOnClickListener {
            currentWord?.let {
                val textToSpeak = it.english
                textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    override fun showWordData(word: WordEntity) {
        currentWord = word
        binding.textEnglish.text = "English : ${word.english}"
        binding.textType.text = "Type : ${word.type}"
        binding.textTranscript.text = "Transcript : ${word.transcript}"
        binding.textUzbek.text = "Uzbek : ${word.uzbek}"
        binding.textCountable.text = "Countable : ${word.countable}"
        updateFavouriteIcon(word.is_favourite == 1)
    }

    override fun updateFavouriteIcon(isFavourite: Boolean) {
        val icon = if (isFavourite) {
            R.drawable.ic_selected_bookmark
        } else {
            R.drawable.ic_unselected_bookmark
        }
        binding.buttonFavourite.setImageResource(icon)
    }

    override fun back() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        _binding = null
    }
}
