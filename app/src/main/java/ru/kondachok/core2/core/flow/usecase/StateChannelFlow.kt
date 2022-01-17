package ru.kondachok.core2.core.flow.usecase

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import ru.kondachok.core2.core.Fun
import ru.kondachok.core2.core.Init
import ru.kondachok.core2.core.State

class StateChannelFlow<IN, OUT>(
    fx: Fun<IN, OUT>,
    beforeFx: Fun<State<OUT?>, State<OUT?>?>? = null,
    onResponseFx: Fun<OUT?, State<OUT?>?>? = null,
    afterFx: Fun<State<OUT?>, State<OUT?>?>? = null,
    errorFx: Fun<Throwable, State<OUT?>?>? = null,
    finallyFx: Fun<Unit, State<OUT?>?>? = null
) : FlowUseCase<IN, OUT>(fx, beforeFx, onResponseFx, afterFx, errorFx, finallyFx),
    Flow<State<OUT?>> {

    //Аналогично работе Channel<State<OUT?>>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
    //При подписке срабатывает последнее значение из буффера и не происходит replay
    //хранит только одно поледнее значение
    private val flow = MutableSharedFlow<State<OUT?>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private var state: State<OUT?> = Init

    override val value: State<OUT?>
        get() = state

    override suspend fun collect(collector: FlowCollector<State<OUT?>>) {
        flow.collect(collector)
    }

    override fun updateState(state: State<OUT?>) {
        this.state = state
        flow.tryEmit(state)
    }
}
