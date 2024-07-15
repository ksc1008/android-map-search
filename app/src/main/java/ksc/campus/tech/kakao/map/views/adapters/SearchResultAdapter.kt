package ksc.campus.tech.kakao.map.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ksc.campus.tech.kakao.map.R
import ksc.campus.tech.kakao.map.models.SearchResult


class SearchResultAdapter(
    val onItemClicked: ((item: SearchResult, index: Int) -> Unit)
) :
    ListAdapter<SearchResult, SearchResultAdapter.SearchResultViewHolder>(object :
        DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean =
            oldItem == newItem

    }) {
    class SearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val resultName: TextView
        val resultAddress: TextView
        val resultType: TextView
        var resultId: String = ""

        init {
            resultName = itemView.findViewById(R.id.text_result_name)
            resultAddress = itemView.findViewById(R.id.text_result_address)
            resultType = itemView.findViewById(R.id.text_result_type)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        val holder = SearchResultViewHolder(view)
        view.setOnClickListener {
            onItemClicked(
                SearchResult(
                    holder.resultId,
                    holder.resultName.text.toString(),
                    holder.resultAddress.text.toString(),
                    holder.resultType.text.toString()
                ),
                holder.bindingAdapterPosition
            )
        }
        return holder
    }


    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val item = currentList[position]
        holder.resultId = item.id
        holder.resultAddress.text = item.address
        holder.resultName.text = item.name
        holder.resultType.text = item.type
    }
}