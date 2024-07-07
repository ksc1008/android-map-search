package campus.tech.kakao.map.views

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.view_models.SearchActivityViewModel
import campus.tech.kakao.map.views.adapters.SearchKeywordAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var searchResultFragmentContainer: FragmentContainerView
    private lateinit var searchInput: SearchView
    private lateinit var keywordRecyclerView: RecyclerView
    private val searchViewModel: SearchActivityViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initiateViews()

        setInitialValueToAdapter()
        initiateLiveDataObservation()
    }

    private fun initiateLiveDataObservation() {
        searchViewModel.searchText.observe(this) {
            searchInput.setQuery(it, false)
        }
        searchViewModel.keywords.observe(this) {
            (keywordRecyclerView.adapter as? SearchKeywordAdapter)?.updateKeywords(it.asReversed())
            setKeywordRecyclerViewActive(it.isNotEmpty())
        }
    }

    private fun setInitialValueToAdapter() {
        searchViewModel.keywords.value?.let {
            (keywordRecyclerView.adapter as? SearchKeywordAdapter)?.updateKeywords(it)
        }
    }

    private fun initiateSaveKeywordRecyclerView() {
        val adapter = SearchKeywordAdapter(LayoutInflater.from(this), {
            searchViewModel.clickKeyword(it)
        }, {
            searchViewModel.clickKeywordDeleteButton(it)
        })

        keywordRecyclerView.adapter = adapter
        keywordRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setKeywordRecyclerViewActive(active: Boolean) {
        keywordRecyclerView.isVisible = active
    }

    private fun initiateSearchView(){

        searchInput.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewModel.submitQuery(query?:"")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun initiateViews() {
        searchInput = findViewById(R.id.input_search)
        keywordRecyclerView = findViewById(R.id.saved_search_bar)
        searchResultFragmentContainer = findViewById(R.id.fragment_container_search_result)
        keywordRecyclerView = findViewById(R.id.saved_search_bar)
        initiateSearchView()
        initiateSaveKeywordRecyclerView()
    }
}
