package uz.gita.firstlesson.dictionaryapp.screens.about_screen
import uz.gita.firstlesson.dictionaryapp.room.WordEntity

class AboutPresenter(
    private val view: AboutContract.View
) : AboutContract.Presenter {

    private val model: AboutContract.Model = AboutModel()
    private lateinit var word: WordEntity

    override fun loadWordData(wordId: Int) {
        word = model.getWordById(wordId)
        view.showWordData(word)
    }

    override fun onClickBack() {
        view.back()
    }

    override fun onClickFavourite() {
        val newState = if (word.is_favourite == 1) 0 else 1
        word.is_favourite = newState
        model.updateFavouriteState(word.id, newState)
        view.updateFavouriteIcon(newState == 1)
    }
}
