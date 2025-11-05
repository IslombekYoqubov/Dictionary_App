package uz.gita.firstlesson.dictionaryapp.screens.main_screen

import uz.gita.firstlesson.dictionaryapp.repository.view_models.WordListViewModel
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import uz.gita.firstlesson.dictionaryapp.R
import uz.gita.firstlesson.dictionaryapp.adapters.DictionaryAdapter
import uz.gita.firstlesson.dictionaryapp.databinding.ActivityMainBinding
import uz.gita.firstlesson.dictionaryapp.room.WordEntity
import uz.gita.firstlesson.dictionaryapp.screens.about_screen.AboutScreen
import java.util.Locale

class WordListScreen : Fragment(), WordListContract.View {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val adapter = DictionaryAdapter()
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var speechRecognizerLauncher: ActivityResultLauncher<Intent>
    private val viewModel: WordListViewModel by activityViewModels()
    private val presenter: WordListContract.Presenter = WordListPresenter(this)
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private val delay = 500L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSpeechRecognizer()
        initTextToSpeech()

        binding.bottomNavView.selectedItemId =
            if (viewModel.position == 0) R.id.menuWords else R.id.menuBookmarks

        loadData()
        loadActions()
        if (viewModel.query.isNotEmpty()) {
            if (viewModel.position == 1) presenter.loadFavouriteWordsByQuery(viewModel.query)
            else presenter.loadWordsByQuery(viewModel.query)
        } else {
            if (viewModel.position == 1) presenter.loadFavouriteWords()
            else presenter.loadWords()
        }
    }

    private fun initSpeechRecognizer() {
        speechRecognizerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val resultList =
                    result.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val spokenText = resultList?.get(0) ?: return@registerForActivityResult
                binding.inputQuery.setQuery(spokenText, false) // submit emas, faqat text
                if (viewModel.position == 1) {
                    presenter.loadFavouriteWordsByQuery(spokenText)
                } else {
                    presenter.loadWordsByQuery(spokenText)
                }
            }
        }
    }

    private fun initTextToSpeech() {
        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.US
            }
        }
    }

    private fun loadData() {
        binding.rvWords.adapter = adapter
    }

    private fun loadActions() {
        adapter.setImgCLickListener { wordId, state, pos ->
            presenter.clickWordFavourite(wordId, state)
            adapter.updateFavourite(pos)
            if (viewModel.position == 1) adapter.removeItem(pos)
            else adapter.notifyItemChanged(pos)
        }
        binding.inputQuery.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRunnable?.let { handler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    val query = newText?.trim().orEmpty()
                    viewModel.query = query
                    adapter.setQuery(query)

                    if (query.isEmpty()) {
                        if (viewModel.position == 1) presenter.loadFavouriteWords()
                        else presenter.loadWords()
                    } else {
                        if (viewModel.position == 1) presenter.loadFavouriteWordsByQuery(query)
                        else presenter.loadWordsByQuery(query)
                    }
                }

                handler.postDelayed(searchRunnable!!, delay)
                return true
            }
        })
        adapter.setSpeakerClickListener { text ->
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
        adapter.setOnClickListener {
            presenter.clickItem(it.id)
        }
        binding.bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuWords -> {
                    viewModel.position = 0
                    binding.inputQuery.setQuery("", false)
                    binding.inputQuery.clearFocus()
                    presenter.loadWords()
                    true
                }

                R.id.menuBookmarks -> {
                    viewModel.position = 1
                    binding.inputQuery.setQuery("", false)
                    binding.inputQuery.clearFocus()
                    presenter.loadFavouriteWords()
                    true
                }

                else -> false
            }
        }

        // mic button
        binding.btnMic.setOnClickListener {
            if (checkAudioPermission()) {
                startSpeechToText()
            } else {
                requestAudioPermission()
            }
        }
    }

    override fun showWords(list: List<WordEntity>) {
        adapter.submitList(list)
    }

    override fun showEmptyScreen(boolean: Boolean) {
        binding.emptyScreen.isVisible = boolean
    }

    override fun showAboutScreen(wordId: Int) {
        val fr = AboutScreen()
        val args = Bundle()
        args.putInt("word_id", wordId)
        fr.arguments = args
        parentFragmentManager.beginTransaction()
            .replace(R.id.myContainer, fr)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        _binding = null
    }

    private fun checkAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startSpeechToText()
        } else {
            Toast.makeText(requireContext(), "Audio ruxsat kerak", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestAudioPermission() {
        requestPermissions(arrayOf(android.Manifest.permission.RECORD_AUDIO), 1001)
    }

    private fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Gapiring...")
        }

        try {
            speechRecognizerLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                "Sizning qurilmangizda bu funksiyani qo‘llab-quvvatlamaydi",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}