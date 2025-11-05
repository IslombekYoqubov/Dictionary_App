package uz.gita.firstlesson.dictionaryapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "dictionary")
data class WordEntity (
    @PrimaryKey
    val id: Int,
    val english: String?,
    val type: String?,
    val transcript: String?,
    val uzbek: String?,
    val countable: String?,
    var is_favourite: Int?
) :Serializable