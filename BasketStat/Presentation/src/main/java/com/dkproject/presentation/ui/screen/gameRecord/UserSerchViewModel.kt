package com.dkproject.presentation.ui.screen.gameRecord

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.android.paging3.searchbox.connectPaginator
import com.algolia.instantsearch.compose.item.StatsTextState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.stats.DefaultStatsPresenter
import com.algolia.instantsearch.stats.StatsConnector
import com.algolia.instantsearch.stats.connectView
import com.algolia.search.client.ClientSearch
import com.algolia.search.logging.LogLevel
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.dkproject.domain.model.UserData
import com.dkproject.presentation.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import javax.inject.Inject


@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class UserSearchViewModel @Inject constructor(
):ViewModel() {
    val client   = ClientSearch(
        applicationID = ApplicationID(BuildConfig.ALGOLIA_APPLICATION_ID),
        apiKey = APIKey(BuildConfig.ALGOLIA_API_KEY),
        logLevel = LogLevel.All
    )

    val indexName = IndexName("Users")
    val searcher = HitsSearcher(client, indexName)

    val searchBoxState = SearchBoxState()
    val searchBoxConnector = SearchBoxConnector(searcher)
    val hitsPaginator = Paginator(searcher) { it.deserialize(UserDataUiModel.serializer()) }

    // Stats
    val statsText = StatsTextState()
    val statsConnector = StatsConnector(searcher)

    val connections = ConnectionHandler(searchBoxConnector, statsConnector)

    init {
        connections += searchBoxConnector.connectView(searchBoxState)
        connections += statsConnector.connectView(statsText, DefaultStatsPresenter())
        connections += searchBoxConnector.connectPaginator(hitsPaginator)
    }

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
        connections.clear()
    }

}

@Parcelize
@Serializable
data class UserDataUiModel(
    val userUid: String = "",
    val userNickName: String = "",
    val userProfileImageUrl: String = "",
    val userHeight: Double = 0.0,
    val userWeight: Double = 0.0,
    val userPosition: String = ""
): Parcelable {
    fun toDomainModel(): UserData {
        return UserData(
            userUid = userUid,
            userNickName = userNickName,
            userProfileImageUrl = userProfileImageUrl,
            userHeight = userHeight,
            userWeight = userWeight,
            userPosition = userPosition
        )
    }
}

