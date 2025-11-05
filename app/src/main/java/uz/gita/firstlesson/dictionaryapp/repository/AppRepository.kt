package uz.gita.firstlesson.dictionaryapp.repository

import androidx.room.Query
import uz.gita.firstlesson.dictionaryapp.room.WordEntity

interface AppRepository {
    fun loadWords() : List<WordEntity>
    fun upgradeFavourite(wordId : Int, state : Int)
    fun loadFavouriteWords() : List<WordEntity>
    fun getWordEntityById(wordId: Int) : WordEntity
    fun loadWordsByQuery(query: String) : List<WordEntity>
    fun loadFavouriteWordsByQuery(query : String) : List<WordEntity>
}