package uz.gita.firstlesson.dictionaryapp.screens.main_screen

import uz.gita.firstlesson.dictionaryapp.repository.AppRepository
import uz.gita.firstlesson.dictionaryapp.repository.impl.AppRepositoryImpl
import uz.gita.firstlesson.dictionaryapp.room.WordEntity

class WordListModel : WordListContract.Model {
    private val repository : AppRepository = AppRepositoryImpl.getInstance()
    override fun loadWords(): List<WordEntity> = repository.loadWords()

    override fun getWordEntityById(wordId: Int): WordEntity {
        return repository.getWordEntityById(wordId)
    }

    override fun upgradeFavourite(wordId: Int, state: Int) {
        repository.upgradeFavourite(wordId, state)
    }

    override fun loadFavouriteWords(): List<WordEntity> {
        return repository.loadFavouriteWords()
    }

    override fun loadWordsByQuery(query: String) : List<WordEntity>{
        return repository.loadWordsByQuery(query)
    }

    override fun loadFavouriteWordsByQuery(query: String): List<WordEntity> {
        return repository.loadFavouriteWordsByQuery(query)
    }
}