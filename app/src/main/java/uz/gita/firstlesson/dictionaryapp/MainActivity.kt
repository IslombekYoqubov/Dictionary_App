package uz.gita.firstlesson.dictionaryapp

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import uz.gita.firstlesson.dictionaryapp.adapters.DictionaryAdapter
import uz.gita.firstlesson.dictionaryapp.databinding.ActivityFirstBinding
import uz.gita.firstlesson.dictionaryapp.screens.main_screen.WordListScreen

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstBinding
    private lateinit var textToSpeech: TextToSpeech
    private val adapter: DictionaryAdapter = DictionaryAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.myContainer, WordListScreen())
                .commit()
        }
//
//        binding.rvWords.adapter = adapter
//        dao = DictionaryDatabase.getDatabase(this).getDao()
//        val allWords = dao.getAllWords()
//        Log.d("TTT", "So'zlar soni: ${allWords.size}")  // << BU YERDA!
//        adapter.submitList(dao.getAllWords())
//
//        binding.inputQuery.addTextChangedListener{
//            adapter.submitList(dao.getByQueryEng(binding.inputQuery.text.toString()))
//        }
////        binding.inputQuery.setOnEditorActionListener { v, actionId, event ->
////            if (actionId==0){
////                adapter.submitList(dao.getByQuery(binding.inputQuery.text.toString()))
////            }
////
////            return@setOnEditorActionListener false
////        }

    }
}