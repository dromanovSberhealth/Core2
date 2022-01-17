package ru.kondachok.core2.core.flow.suspendeusecase

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import ru.kondachok.core2.core.Init
import ru.kondachok.core2.core.State
import ru.kondachok.core2.core.SuspendFun

class SuspendStateStateFlow<IN, OUT>(
    fx: SuspendFun<IN, OUT>,
    beforeFx: SuspendFun<State<OUT?>, State<OUT?>?>? = null,
    onResponseFx: SuspendFun<OUT?, State<OUT?>?>? = null,
    afterFx: SuspendFun<State<OUT?>, State<OUT?>?>? = null,
    errorFx: SuspendFun<Throwable, State<OUT?>?>? = null,
    finallyFx: SuspendFun<Unit, State<OUT?>?>? = null
) : SuspendFlowUseCase<IN, OUT>(fx, beforeFx, onResponseFx, afterFx, errorFx, finallyFx),
    StateFlow<State<OUT?>> {

    // Аналогично MutableStateFlow<State<OUT?>>, но 
    // не MutableStateFlow потому что при быстром emit значений и одинаковым финальтым значением
    // может не произойти обновление значения
    private val flow = MutableSharedFlow<State<OUT?>>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    ).apply {
        tryEmit(Init)
    }

    override val replayCache: List<State<OUT?>>
        get() = flow.replayCache

    override val value: State<OUT?>
        get() = flow.replayCache.first()

    override suspend fun collect(collector: FlowCollector<State<OUT?>>): Nothing {
        flow.collect(collector)
    }

    override suspend fun updateState(state: State<OUT?>) {
        flow.emit(state)
    }
}
