package uz.gita.firstlesson.dictionaryapp.screens.main_screen

import uz.gita.firstlesson.dictionaryapp.room.WordEntity

interface WordListContract {
    interface Model {
        fun loadWords() : List<WordEntity>
        fun getWordEntityById(wordId: Int) : WordEntity
        fun upgradeFavourite (wordId : Int, state : Int)
        fun loadFavouriteWords() : List<WordEntity>
        fun loadWordsByQuery(query : String) : List<WordEntity>
        fun loadFavouriteWordsByQuery(query : String) : List<WordEntity>
    }
    interface Presenter {
        fun clickItem(wordId: Int)
        fun clickWordFavourite(wordId: Int, state: Int)
        fun loadFavouriteWords()
        fun loadFavouriteWordsByQuery(query : String)
        fun loadWordsByQuery(query : String)
        fun loadWords()
    }
    interface View {
        fun showWords(list: List<WordEntity>)
        fun showEmptyScreen(boolean: Boolean)
        fun showAboutScreen(wordId: Int)
    }
}