package uz.gita.firstlesson.dictionaryapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import uz.gita.firstlesson.dictionaryapp.R
import uz.gita.firstlesson.dictionaryapp.room.WordEntity
import kotlin.math.abs

class DictionaryAdapter:RecyclerView.Adapter<DictionaryAdapter.WordViewHolder>() {
    private var list = mutableListOf<WordEntity>()
    private var onItemClickListener:((WordEntity)->Unit)? = null
    private lateinit var imgClickListener : ((Int, Int, Int) -> Unit)
    private var onSpeakerClickListener: ((String) -> Unit)? = null
    private var query :String? = null

    fun setOnClickListener(listener:(WordEntity)->Unit){
        onItemClickListener = listener
    }
    fun submitList(tList:List<WordEntity>){
        list.clear()
        list.addAll(tList)
        notifyDataSetChanged()
    }
    inner class WordViewHolder(view:View):RecyclerView.ViewHolder(view){
        private var word:AppCompatTextView
        private var definition:AppCompatTextView
        private var imageView : AppCompatImageButton
        private val speaker: ImageView = view.findViewById(R.id.speaker)
        init {
            view.setOnClickListener {
                onItemClickListener?.invoke(list[adapterPosition])
            }
            word = view.findViewById(R.id.tvWord)
            definition = view.findViewById(R.id.tvTranslate)
            imageView = view.findViewById(R.id.ivBookmark)

            imageView.setOnClickListener {
                if(::imgClickListener.isInitialized){
                    val item = list[adapterPosition]
                    imgClickListener.invoke(item.id, item.is_favourite?:0, adapterPosition)
                }
            }

            speaker.setOnClickListener {
                val text = list[adapterPosition].english
                if (text != null) {
                    onSpeakerClickListener?.invoke(text)
                }
            }
        }
        fun bind(position:Int){
            val item = list[position]
            word.text = highlightText(item.english ?: "", query?:"", android.graphics.Color.RED)
            definition.text = list[position].uzbek
            if(list[position].is_favourite == 0){
                imageView.setImageResource(R.drawable.ic_unselected_bookmark)
            } else imageView.setImageResource(R.drawable.ic_selected_bookmark)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word,parent,false)
        return WordViewHolder((view))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) = holder.bind(position)

    fun setImgCLickListener(block : ((Int, Int, Int) -> Unit)){
        imgClickListener = block
    }

    fun updateFavourite(position: Int) {
        val word = list[position]
        val updated = word.copy(is_favourite = if (word.is_favourite == 1) 0 else 1)
        list = list.toMutableList().also { it[position] = updated }
    }

    fun removeItem(position: Int){
        list.removeAt(position)
        notifyItemRemoved(position)
    }
    fun setSpeakerClickListener(listener: (String) -> Unit) {
        onSpeakerClickListener = listener
    }

    fun setQuery(tempQuery : String){
        query = tempQuery
    }

    private fun highlightText(fullText: String, searchText: String, color: Int): CharSequence {
        val spannable = android.text.SpannableString(fullText)
        if (searchText.isEmpty()) return spannable

        val lowerFull = fullText.lowercase()
        val lowerSearch = searchText.lowercase()

        var startIndex = lowerFull.indexOf(lowerSearch)
        while (startIndex >= 0) {
            spannable.setSpan(
                android.text.style.ForegroundColorSpan(color),
                startIndex,
                startIndex + searchText.length,
                android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            startIndex = lowerFull.indexOf(lowerSearch, startIndex + searchText.length)
        }
        return spannable
    }
}