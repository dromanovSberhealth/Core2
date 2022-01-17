package ru.kondachok.core2.core.flow

import javax.inject.Inject
import javax.inject.Singleton
import ru.kondachok.core2.core.Fun
import ru.kondachok.core2.core.State
import ru.kondachok.core2.core.SuspendFun
import ru.kondachok.core2.core.flow.suspendeusecase.SuspendStateChannelFlow
import ru.kondachok.core2.core.flow.suspendeusecase.SuspendStateStateFlow
import ru.kondachok.core2.core.flow.usecase.StateChannelFlow
import ru.kondachok.core2.core.flow.usecase.StateStateFlow

@Singleton
class StateFlowBuilder @Inject constructor() {

    fun <IN, OUT> from(fx: Fun<IN, OUT>): Builder<IN, OUT> {
        return Builder(fx)
    }

    fun <IN, OUT> from(fx: SuspendFun<IN, OUT>): SuspendBuilder<IN, OUT> {
        return SuspendBuilder(fx)
    }

    class Builder<IN, OUT>(private var fx: Fun<IN, OUT>) {

        private var beforeFx: Fun<State<OUT?>, State<OUT?>?>? = null
        private var onResponseFx: Fun<OUT?, State<OUT?>?>? = null
        private var afterFx: Fun<State<OUT?>, State<OUT?>?>? = null
        private var errorFx: Fun<Throwable, State<OUT?>?>? = null
        private var finallyFx: Fun<Unit, State<OUT?>?>? = null

        fun beforeState(fx: Fun<State<OUT?>, State<OUT?>?>) = this.apply {
            this.beforeFx = fx
        }

        fun before(fx: Fun<State<OUT?>, Unit>) = this.apply {
            this.beforeFx = Fun {
                fx.invoke(it)
                null
            }
        }

        fun onResponseState(fx: Fun<OUT?, State<OUT?>?>) = this.apply {
            this.onResponseFx = fx
        }

        fun onResponse(fx: Fun<OUT?, Unit>) = this.apply {
            this.onResponseFx = Fun {
                fx.invoke(it)
                null
            }
        }

        fun afterState(fx: Fun<State<OUT?>, State<OUT?>?>) = this.apply {
            this.afterFx = fx
        }

        fun after(fx: Fun<State<OUT?>, Unit>) = this.apply {
            this.afterFx = Fun {
                fx.invoke(it)
                null
            }
        }

        fun errorState(fx: Fun<Throwable, State<OUT?>?>) = this.apply {
            this.errorFx = fx
        }

        fun error(fx: Fun<Throwable, Unit>) = this.apply {
            this.errorFx = Fun {
                fx.invoke(it)
                null
            }
        }

        fun finallyState(fx: Fun<Unit, State<OUT?>?>) = this.apply {
            this.finallyFx = fx
        }

        fun finally(fx: Fun<Unit, Unit>) = this.apply {
            this.finallyFx = Fun {
                fx.invoke(it)
                null
            }
        }

        fun buildStateFlow(): StateStateFlow<IN, OUT> {
            return StateStateFlow(
                fx,
                beforeFx,
                onResponseFx,
                afterFx,
                errorFx,
                finallyFx
            )
        }

        fun buildChannelFlow(): StateChannelFlow<IN, OUT> {
            return StateChannelFlow(
                fx,
                beforeFx,
                onResponseFx,
                afterFx,
                errorFx,
                finallyFx
            )
        }
    }

    class SuspendBuilder<IN, OUT: Any?>(private var fx: SuspendFun<IN, OUT>) {

        private var beforeFun: SuspendFun<State<OUT?>, State<OUT?>?>? = null
        private var onResponseFun: SuspendFun<OUT?, State<OUT?>?>? = null
        private var afterFun: SuspendFun<State<OUT?>, State<OUT?>?>? = null
        private var errorFun: SuspendFun<Throwable, State<OUT?>?>? = null
        private var finallyFun: SuspendFun<Unit, State<OUT?>?>? = null

        fun beforeState(fx: SuspendFun<State<OUT?>, State<OUT?>?>) = this.apply {
            this.beforeFun = fx
        }

        fun before(fx: SuspendFun<State<OUT?>, Unit>) = this.apply {
            this.beforeFun = SuspendFun {
                fx.invoke(it)
                null
            }
        }

        fun onResponseState(fx: SuspendFun<OUT?, State<OUT?>?>) = this.apply {
            this.onResponseFun = fx
        }

        fun onResponse(fx: Fun<OUT?, Unit>) = this.apply {
            this.onResponseFun = SuspendFun {
                fx.invoke(it)
                null
            }
        }

        fun afterState(fx: SuspendFun<State<OUT?>, State<OUT?>?>) = this.apply {
            this.afterFun = fx
        }

        fun after(fx: SuspendFun<State<OUT?>, Unit>) = this.apply {
            this.afterFun = SuspendFun {
                fx.invoke(it)
                null
            }
        }

        fun errorState(fx: SuspendFun<Throwable, State<OUT?>?>) = this.apply {
            this.errorFun = fx
        }

        fun error(fx: SuspendFun<Throwable, Unit>) = this.apply {
            this.errorFun = SuspendFun {
                fx.invoke(it)
                null
            }
        }

        fun finallyState(fx: SuspendFun<Unit, State<OUT?>?>) = this.apply {
            this.finallyFun = fx
        }

        fun finally(fx: SuspendFun<Unit, Unit>) = this.apply {
            this.finallyFun = SuspendFun {
                fx.invoke(it)
                null
            }
        }

        fun buildStateFlow(): SuspendStateStateFlow<IN, OUT> {
            return SuspendStateStateFlow(
                fx,
                beforeFun,
                onResponseFun,
                afterFun,
                errorFun,
                finallyFun
            )
        }

        fun buildChannelFlow(): SuspendStateChannelFlow<IN, OUT> {
            return SuspendStateChannelFlow(
                fx,
                beforeFun,
                onResponseFun,
                afterFun,
                errorFun,
                finallyFun
            )
        }
    }
}
