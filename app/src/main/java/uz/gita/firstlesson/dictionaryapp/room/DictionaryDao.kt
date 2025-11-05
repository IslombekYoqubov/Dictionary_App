package uz.gita.firstlesson.dictionaryapp.room

import androidx.room.Dao
import androidx.room.Query
import uz.gita.firstlesson.dictionaryapp.room.WordEntity

@Dao
interface DictionaryDao {

    @Query("SELECT * FROM dictionary")
    fun getAllWords():List<WordEntity>

    @Query("SELECT * FROM dictionary WHERE english LIKE '%' || :query || '%'")
    fun getByQueryEng(query:String):List<WordEntity>

    @Query("SELECT * FROM dictionary WHERE uzbek LIKE '%' || :query || '%'")
    fun getByQueryUz(query: String) : List<WordEntity>

    @Query("UPDATE dictionary SET is_favourite = :isFavourite WHERE id = :wordId")
    fun updateFavourite(wordId : Int, isFavourite : Int)

    @Query("SELECT * from dictionary where id = :wordId")
    fun getWordEntityById(wordId: Int) : WordEntity

    @Query("SELECT * FROM dictionary WHERE is_favourite = 1")
    fun getAllFavourites() : List<WordEntity>

    @Query("SELECT * FROM dictionary WHERE is_favourite = 1 AND english LIKE '%' || :query || '%'")
    fun getFavouritesByQueryEng(query: String): List<WordEntity>
}