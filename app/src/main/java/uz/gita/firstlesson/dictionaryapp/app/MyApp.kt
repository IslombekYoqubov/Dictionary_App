package uz.gita.firstlesson.dictionaryapp.app

import android.app.Application
import uz.gita.firstlesson.dictionaryapp.room.DictionaryDatabase

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DictionaryDatabase.init(this)
    }
}