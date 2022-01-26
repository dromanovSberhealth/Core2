package ru.kondachok.core2.core.flow.usecase

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import ru.kondachok.core2.core.Fun
import ru.kondachok.core2.core.Init
import ru.kondachok.core2.core.State

class StateStateFlow<IN, OUT>(
    fx: Fun<IN, OUT>,
    beforeFx: Fun<State<OUT>, State<OUT>?>? = null,
    onResponseFx: Fun<OUT, State<OUT>?>? = null,
    afterFx: Fun<State<OUT>, State<OUT>?>? = null,
    errorFx: Fun<Throwable, State<OUT>?>? = null,
    finallyFx: Fun<Unit, State<OUT>?>? = null
) : FlowUseCase<IN, OUT>(fx, beforeFx, onResponseFx, afterFx, errorFx, finallyFx),
    StateFlow<State<OUT>> {

    // Аналогично MutableStateFlow<State<OUT>>, но
    // не MutableStateFlow потому что при быстром emit значений и одинаковым финальтым значением
    // может не произойти обновление значения
    private val flow = MutableSharedFlow<State<OUT>>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    ).apply {
        tryEmit(Init)
    }

    override val replayCache: List<State<OUT>>
        get() = flow.replayCache

    override val value: State<OUT>
        get() = flow.replayCache.first()

    override suspend fun collect(collector: FlowCollector<State<OUT>>): Nothing {
        flow.collect(collector)
    }

    override fun updateState(state: State<OUT>) {
        flow.tryEmit(state)
    }
}
