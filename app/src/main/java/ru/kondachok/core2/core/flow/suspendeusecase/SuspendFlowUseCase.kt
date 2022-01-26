package ru.kondachok.core2.core.flow.suspendeusecase

import kotlinx.coroutines.CancellationException
import ru.kondachok.core2.core.Cancel
import ru.kondachok.core2.core.Data
import ru.kondachok.core2.core.Error
import ru.kondachok.core2.core.Load
import ru.kondachok.core2.core.State
import ru.kondachok.core2.core.SuspendFun
import ru.kondachok.core2.core.SuspendUseCase

abstract class SuspendFlowUseCase<IN, OUT>(
    private val fx: SuspendFun<IN, OUT>,
    private val beforeFx: SuspendFun<State<OUT>, State<OUT>?>? = null,
    private val onResponseFx: SuspendFun<OUT, State<OUT>?>? = null,
    private val afterFx: SuspendFun<State<OUT>, State<OUT>?>? = null,
    private val errorFx: SuspendFun<Throwable, State<OUT>?>? = null,
    private val finallyFx: SuspendFun<Unit, State<OUT>?>? = null
) : SuspendUseCase<IN, Unit> {

    abstract val value: State<OUT>

    override suspend fun invoke(arg: IN) {
        try {
            updateState(beforeFx?.invoke(value) ?: Load)
            val response = fx.invoke(arg)
            updateState(
                onResponseFx?.invoke(response) ?: Data<OUT>(response)
            )
            afterFx?.invoke(value)?.let { updateState(it) }
        } catch (error: Throwable) {
            if(error is CancellationException) {
                updateState(Cancel)
            } else {
                updateState(errorFx?.invoke(error) ?: Error(error))
            }
        } finally {
            finallyFx?.invoke(Unit)?.let { updateState(it) }
        }
    }

    abstract suspend fun updateState(state: State<OUT>)
}
