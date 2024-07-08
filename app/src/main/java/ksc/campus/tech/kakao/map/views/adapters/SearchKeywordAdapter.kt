package ksc.campus.tech.kakao.map.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ksc.campus.tech.kakao.map.R

class KeywordDiffUtil(private val oldList: List<String>, private val newList: List<String>): DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

}

class SearchKeywordAdapter(
    private val inflater: LayoutInflater,
    private val clickCallback: SearchKeywordClickCallback
) : RecyclerView.Adapter<SearchKeywordAdapter.SearchKeywordViewHolder>() {
    private var items: List<String> = listOf()

    class SearchKeywordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameText: TextView
        val deleteText: View

        init {
            nameText = itemView.findViewById(R.id.text_search_keyword)
            deleteText = itemView.findViewById(R.id.view_delete_keyword)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchKeywordViewHolder {
        val view = inflater.inflate(R.layout.item_search_keyword, parent, false)
        val holder = SearchKeywordViewHolder(view)
        view.setOnClickListener {
            clickCallback.clickKeyword(holder.nameText.text.toString())
        }
        holder.deleteText.setOnClickListener {
            clickCallback.clickRemove(holder.nameText.text.toString())
        }
        return holder
    }

    override fun onBindViewHolder(holder: SearchKeywordViewHolder, position: Int) {
        val item = items[position]
        holder.nameText.text = item
    }

    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemCount(): Int = items.size

    fun updateKeywords(keywords: List<String>) {
        val diffUtil = KeywordDiffUtil(items, keywords)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        items = keywords
        diffResult.dispatchUpdatesTo(this)
    }
}