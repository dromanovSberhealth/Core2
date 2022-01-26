package ru.kondachok.core2.sample.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.kondachok.core2.core.Load
import ru.kondachok.core2.core.State
import ru.kondachok.core2.core.dataOrNull
import ru.kondachok.core2.core.flow.StateFlowBuilder
import ru.kondachok.core2.sample.data.GetAnswersUseCase
import ru.kondachok.core2.sample.domian.Answer

@HiltViewModel
class ListViewModel @Inject constructor(
    getAnswersUseCase: GetAnswersUseCase,
    stateFlowBuilder: StateFlowBuilder
) : ViewModel() {

    // пример расширения состояний через Refresh
    private val answersRes = stateFlowBuilder
        .from(getAnswersUseCase)
        .beforeState { state ->
            state.dataOrNull()?.let { Refresh(it) } ?: Load
        }
        .buildStateFlow()

    val answersFlow: Flow<State<List<Answer>?>> = answersRes

    fun init() = viewModelScope.launch {
        answersRes.invoke(false)
    }

    fun refresh() = viewModelScope.launch {
        answersRes.invoke(true)
    }
}

