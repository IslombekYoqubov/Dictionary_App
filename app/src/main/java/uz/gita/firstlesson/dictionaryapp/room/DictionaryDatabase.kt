package uz.gita.firstlesson.dictionaryapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WordEntity::class], version = 1)
abstract class DictionaryDatabase :RoomDatabase(){
    abstract fun getDao(): DictionaryDao
    companion object{
        private var INSTANCE: DictionaryDatabase? = null

        fun init(context: Context){
            INSTANCE = Room.databaseBuilder(context = context, DictionaryDatabase::class.java,"dictionary.db")
                .createFromAsset("dictionary.db")
                .allowMainThreadQueries()
                .build()
        }

        fun getDatabase(): DictionaryDatabase {
            return INSTANCE!!
        }
    }
}