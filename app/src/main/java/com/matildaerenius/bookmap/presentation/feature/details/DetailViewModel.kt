package com.matildaerenius.bookmap.presentation.feature.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matildaerenius.bookmap.domain.model.Book
import com.matildaerenius.bookmap.domain.repository.BookRepository
import com.matildaerenius.bookmap.domain.usecase.GetBookDetailsUseCase
import com.matildaerenius.bookmap.presentation.common.state.UiState
import com.matildaerenius.bookmap.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getBookDetailsUseCase: GetBookDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Book>>(UiState.Loading)
    val uiState: StateFlow<UiState<Book>> = _uiState.asStateFlow()

    private var currentBookId: Int? = null

    init {
        currentBookId = savedStateHandle.get<Int>("bookId")
        fetchBookDetails()
    }

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.OnRetryClick -> fetchBookDetails()
            is DetailEvent.OnToggleFavorite -> {
                Log.d("BookDetail", "Användaren klickade på favorit!")
            }
        }
    }

private fun fetchBookDetails() {
    val bookId = currentBookId
    if (bookId == null) {
        _uiState.value = UiState.Error("Kunde inte hitta boken. ID saknas.")
        return
    }

    viewModelScope.launch {
        _uiState.value = UiState.Loading

        when (val result = getBookDetailsUseCase(bookId)) {
            is Resource.Success -> {
                Log.d("BildTest", "LÄNK TILL BILDEN ÄR: >>${result.data.imageUrl}<<")
                _uiState.value = UiState.Success(result.data)
            }
            is Resource.Error -> {
                val errorMessage = result.error.name
                Log.e("BookDetail", "Fel vid hämtning av bokID: $bookId. Fel: $errorMessage")
                _uiState.value = UiState.Error("Ett fel uppstod när boken skulle laddas.")
            }
        }
    }
}
}
