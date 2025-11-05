package uz.gita.firstlesson.dictionaryapp.screens.about_screen

import uz.gita.firstlesson.dictionaryapp.room.WordEntity

interface AboutContract {
    interface Model {
        fun getWordById(wordId: Int): WordEntity
        fun updateFavouriteState(wordId: Int, isFavourite: Int)
    }
    interface Presenter {
        fun loadWordData(wordId: Int)
        fun onClickBack()
        fun onClickFavourite()
    }
    interface View {
        fun showWordData(word: WordEntity)
        fun updateFavouriteIcon(isFavourite: Boolean)
        fun back()
    }
}

