package ru.kondachok.core2.core.flow.suspendeusecase

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.kondachok.core2.core.Init
import ru.kondachok.core2.core.State
import ru.kondachok.core2.core.SuspendFun

class SuspendStateChannelFlow<IN, OUT>(
    fx: SuspendFun<IN, OUT>,
    beforeFx: SuspendFun<State<OUT>, State<OUT>?>? = null,
    onResponseFx: SuspendFun<OUT, State<OUT>?>? = null,
    afterFx: SuspendFun<State<OUT>, State<OUT>?>? = null,
    errorFx: SuspendFun<Throwable, State<OUT>?>? = null,
    finallyFx: SuspendFun<Unit, State<OUT>?>? = null
) : SuspendFlowUseCase<IN, OUT>(fx, beforeFx, onResponseFx, afterFx, errorFx, finallyFx),
    Flow<State<OUT>> {

    //Аналогично работе Channel<State<OUT>>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
    //При подписке срабатывает последнее значение из буффера и не происходит replay
    //хранит только одно поледнее значение
    private val flow = MutableSharedFlow<State<OUT>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private var state: State<OUT> = Init

    override val value: State<OUT>
        get() = state

    override suspend fun collect(collector: FlowCollector<State<OUT>>) {
        flow.collect(collector)
    }

    override suspend fun updateState(state: State<OUT>) {
        this.state = state
        flow.emit(state)
    }
}
