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
import ru.kondachok.core2.core.DataMapper
import ru.kondachok.core2.core.Res
import ru.kondachok.core2.core.SuspendFun
import ru.kondachok.core2.core.SuspendUseCase
import ru.kondachok.core2.core.UseCase
import ru.kondachok.core2.core.asOtherRes
import ru.kondachok.core2.core.flow.StateFlowBuilder
import ru.kondachok.core2.core.reqRes
import ru.kondachok.core2.sample.domian.Answer

@HiltViewModel
class MainViewModel @Inject constructor(
    private val clearAnswersUseCase: UseCase<Unit, Unit>,
    getAnswersUseCase: SuspendUseCase<Boolean, List<Answer>>,
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
        .from(SuspendFun<Boolean, List<Answer>> {
            delay(2000) // тестовая задержка
            getAnswersUseCase.invoke(it)
        })
        .buildChannelFlow()

    val answerFlow: Flow<Res<String?>> = answersRes
        .map { state ->
            state.reqRes().asOtherRes(DataMapper { it?.get(Random.nextInt(it.size))?.text })
        }

    val answerReloadFlow: Flow<Res<Unit?>> = answersReloadRes.map { it.reqRes().asOtherRes(Unit) }

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
