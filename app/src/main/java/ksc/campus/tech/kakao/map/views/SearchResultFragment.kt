package ksc.campus.tech.kakao.map.views

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ksc.campus.tech.kakao.map.R
import ksc.campus.tech.kakao.map.models.SearchResult
import ksc.campus.tech.kakao.map.view_models.SearchActivityViewModel
import ksc.campus.tech.kakao.map.views.adapters.SearchResultAdapter

class SearchResultFragment : Fragment() {
    private lateinit var searchResultRecyclerView: RecyclerView
    private lateinit var noResultHelpText: View
    private val viewModel by activityViewModels<SearchActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    private fun setInitialValueToAdapter() {
        viewModel.searchResult.value?.let {
            (searchResultRecyclerView.adapter as? SearchResultAdapter)?.updateResult(it)
        }
    }

    private fun initiateRecyclerView(view: View) {
        searchResultRecyclerView = view.findViewById(R.id.list_search_result)
        searchResultRecyclerView.adapter =
            SearchResultAdapter { item: SearchResult, _: Int ->
                viewModel.clickSearchResultItem(item)
            }
        searchResultRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        searchResultRecyclerView.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun initiateSearchResultLiveDataObservation() {
        viewModel.searchResult.observe(viewLifecycleOwner) {
            (searchResultRecyclerView.adapter as? SearchResultAdapter)?.updateResult(it)
            setNoResultHelpTextActive(it.isEmpty())
        }
    }

    private fun setNoResultHelpTextActive(active: Boolean) {
        noResultHelpText.isVisible = active
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noResultHelpText = view.findViewById(R.id.text_no_result)

        initiateRecyclerView(view)
        initiateSearchResultLiveDataObservation()
        setInitialValueToAdapter()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.switchContent(SearchActivityViewModel.Companion.ContentType.MAP)
                }

            })
    }
}