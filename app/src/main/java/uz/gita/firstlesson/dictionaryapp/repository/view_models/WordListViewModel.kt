package uz.gita.firstlesson.dictionaryapp.repository.view_models

import androidx.lifecycle.ViewModel

class WordListViewModel : ViewModel() {
    var state = 1
    var query = ""
    var position = 0
}
