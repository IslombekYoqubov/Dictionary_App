package uz.gita.firstlesson.dictionaryapp.screens.about_screen
import uz.gita.firstlesson.dictionaryapp.repository.impl.AppRepositoryImpl
import uz.gita.firstlesson.dictionaryapp.room.WordEntity

class AboutModel : AboutContract.Model {

    private val repository = AppRepositoryImpl.getInstance()
    override fun getWordById(wordId: Int): WordEntity {
        return repository.getWordEntityById(wordId)
    }

    override fun updateFavouriteState(wordId: Int, isFavourite: Int) {
        repository.upgradeFavourite(wordId, isFavourite)
    }
}
