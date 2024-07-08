package ksc.campus.tech.kakao.map.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ksc.campus.tech.kakao.map.R
import ksc.campus.tech.kakao.map.models.SearchResult

class SearchResultDiffUtil(
    private val oldList: List<SearchResult>,
    private val newList: List<SearchResult>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return (oldItem.type == newItem.type && oldItem.address == newItem.address && oldItem.name == newItem.name)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return (oldItem.type == newItem.type && oldItem.address == newItem.address && oldItem.name == newItem.name)
    }

}

class SearchResultAdapter(
    val onItemClicked: ((item: SearchResult, index: Int) -> Unit)
) :
    RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {
    class SearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val resultName: TextView
        val resultAddress: TextView
        val resultType: TextView

        init {
            resultName = itemView.findViewById(R.id.text_result_name)
            resultAddress = itemView.findViewById(R.id.text_result_address)
            resultType = itemView.findViewById(R.id.text_result_type)
        }
    }

    var results: List<SearchResult> = listOf()

    override fun getItemCount(): Int =results.size

    fun updateResult(results: List<SearchResult>) {
        val diffUtil = SearchResultDiffUtil(this.results, results)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.results = results
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        val holder = SearchResultViewHolder(view)
        view.setOnClickListener {
            onItemClicked(
                SearchResult(
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
        val item = results[position]
        holder.resultAddress.text = item.address
        holder.resultName.text = item.name
        holder.resultType.text = item.type
    }
}