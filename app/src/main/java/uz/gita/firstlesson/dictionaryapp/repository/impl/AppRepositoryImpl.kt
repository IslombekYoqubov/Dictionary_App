package uz.gita.firstlesson.dictionaryapp.repository.impl

import uz.gita.firstlesson.dictionaryapp.repository.AppRepository
import uz.gita.firstlesson.dictionaryapp.room.DictionaryDatabase
import uz.gita.firstlesson.dictionaryapp.room.WordEntity

class AppRepositoryImpl private constructor() : AppRepository {
     private val daoImpl = DictionaryDatabase.getDatabase().getDao()
    companion object {
        private var INSTANCE : AppRepositoryImpl? = null

        fun getInstance() : AppRepositoryImpl{
            if(INSTANCE == null) INSTANCE = AppRepositoryImpl()
            return INSTANCE!!
        }
    }
    override fun loadWords(): List<WordEntity> = daoImpl.getAllWords()

    override fun upgradeFavourite(wordId: Int, state: Int) {
        daoImpl.updateFavourite(wordId, state)
    }

    override fun loadFavouriteWords(): List<WordEntity> {
        return daoImpl.getAllFavourites()
    }

    override fun getWordEntityById(wordId: Int): WordEntity {
        return daoImpl.getWordEntityById(wordId)
    }

    override fun loadWordsByQuery(query: String): List<WordEntity> {
        return daoImpl.getByQueryEng(query)
    }

    override fun loadFavouriteWordsByQuery(query: String): List<WordEntity> {
        return daoImpl.getFavouritesByQueryEng(query)
    }

}