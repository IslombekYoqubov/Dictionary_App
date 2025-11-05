package uz.gita.firstlesson.dictionaryapp.screens.main_screen

import androidx.lifecycle.ViewModel
import kotlin.math.abs

class WordListPresenter(private val view : WordListContract.View) : WordListContract.Presenter, ViewModel() {
    private val model : WordListContract.Model = WordListModel()
    override fun clickItem(wordId: Int) {
        view.showAboutScreen(wordId)
    }

    override fun clickWordFavourite(wordId: Int, state : Int) {
        model.upgradeFavourite(wordId, abs(state - 1))
    }

    override fun loadFavouriteWords() {
        view.showWords(model.loadFavouriteWords())
        view.showEmptyScreen(false)
    }

    override fun loadFavouriteWordsByQuery(query: String) {
        if(query.isBlank()) {
            view.showWords(model.loadWords())
        } else {
            val list = model.loadFavouriteWordsByQuery(query)
            view.showWords(list)
            view.showEmptyScreen(list.isEmpty())
        }
    }

    override fun loadWordsByQuery(query: String) {
        if(query.isBlank()) {
            view.showWords(model.loadWords())
        } else {
            val list = model.loadWordsByQuery(query)
            view.showWords(list)
            view.showEmptyScreen(list.isEmpty())
        }
    }

    override fun loadWords() {
        view.showWords(model.loadWords())
        view.showEmptyScreen(false)
    }
}