package ru.kondachok.core2.sample.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.kondachok.core2.core.Fun
import ru.kondachok.core2.core.Resource
import ru.kondachok.core2.core.SuspendFun
import ru.kondachok.core2.core.SuspendUseCase
import ru.kondachok.core2.core.UseCase
import ru.kondachok.core2.core.flow.StateFlowBuilder
import ru.kondachok.core2.core.map
import ru.kondachok.core2.core.requiredResource
import ru.kondachok.core2.sample.data.CleanAnswersUseCase
import ru.kondachok.core2.sample.data.GetAnswersUseCase
import ru.kondachok.core2.sample.domian.Answer

@HiltViewModel
class MainViewModel @Inject constructor(
    private val clearAnswersUseCase: CleanAnswersUseCase,
    getAnswersUseCase: GetAnswersUseCase,
    stateFlowBuilder: StateFlowBuilder
) : ViewModel() {

    private val answersRes = stateFlowBuilder
        .from(getAnswersUseCase)
        .before {
            Log.d("MainViewModel", "before: $it")
        }
        .after { Log.d("MainViewModel", "after: $it") }
        .error {
            it.printStackTrace()
            Log.d("MainViewModel", "error: $it")
        }
        .finally {
            Log.d("MainViewModel", "finally")
        }
        .buildStateFlow()

    private val answersReloadRes = stateFlowBuilder
        .from(SuspendFun<Boolean, List<Answer>?> { arg ->
            delay(2000) // тестовая задержка
            getAnswersUseCase.invoke(arg)
        })
        .buildChannelFlow()

    val answerFlow: Flow<Resource<String?>> = answersRes
        .map { state ->
            state.requiredResource().map(Fun { it?.get(Random.nextInt(it.size))?.text })
        }

    val answerReloadFlow: Flow<Resource<Unit?>> = answersReloadRes.map { it.requiredResource().map(Unit) }

    fun onAnswerClicked() = viewModelScope.launch {
        answersRes.invoke(false)
    }
    // проверка CancellationException()
    //.cancel()


    fun onNoLoadClicked() {
        clearAnswersUseCase.invoke(Unit)
    }

    fun onReloadClicked() = viewModelScope.launch {
        answersReloadRes.invoke(true)
    }
}
