package ru.kondachok.core2.core.flow.usecase

import ru.kondachok.core2.core.Data
import ru.kondachok.core2.core.Error
import ru.kondachok.core2.core.Fun
import ru.kondachok.core2.core.Load
import ru.kondachok.core2.core.State
import ru.kondachok.core2.core.UseCase

abstract class FlowUseCase<IN, OUT>(
    private val fx: Fun<IN, OUT>,
    private val beforeFx: Fun<State<OUT>, State<OUT>?>? = null,
    private val onResponseFx: Fun<OUT, State<OUT>?>? = null,
    private val afterFx: Fun<State<OUT>, State<OUT>?>? = null,
    private val errorFx: Fun<Throwable, State<OUT>?>? = null,
    private val finallyFx: Fun<Unit, State<OUT>?>? = null
) : UseCase<IN, Unit> {

    abstract val value: State<OUT>

    override fun invoke(arg: IN) {
        try {
            updateState(beforeFx?.invoke(value) ?: Load)
            val response = fx.invoke(arg)
            updateState(
                onResponseFx?.invoke(response) ?: Data(response)
            )
            afterFx?.invoke(value)?.let { updateState(it) }
        } catch (error: Throwable) {
            updateState(errorFx?.invoke(error) ?: Error(error))
        } finally {
            finallyFx?.invoke(Unit)?.let { updateState(it) }
        }
    }

    abstract fun updateState(state: State<OUT>)
}
