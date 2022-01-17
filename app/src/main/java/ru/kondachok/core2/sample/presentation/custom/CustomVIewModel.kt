package ru.kondachok.core2.sample.presentation.custom

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class CustomVIewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<CustomViewState>(StateInit)

    val state: Flow<CustomViewState> = _state

    fun onClick() {
        _state.value = StateClicked
    }

}

