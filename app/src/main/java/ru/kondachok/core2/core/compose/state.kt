package ru.kondachok.core2.core.compose

import androidx.compose.runtime.Composable
import ru.kondachok.core2.core.Cancel
import ru.kondachok.core2.core.Data
import ru.kondachok.core2.core.Init
import ru.kondachok.core2.core.Load
import ru.kondachok.core2.core.Error
import ru.kondachok.core2.core.State

@Composable
inline fun <T> State<T?>.onInit(block: ComposeFun<Unit, Unit>) {
    if (this is Init) block.invoke(Unit)
}

// Load Res

@Composable
inline fun <T> State<T?>.onLoad(block: ComposeFun<Unit, Unit>) {
    if (this is Load) block.invoke(Unit)
}

// Data Res

@Composable
inline fun <T> State<T?>.onData(block: ComposeFun<T?, Unit>) {
    if (this is Data<T>) block.invoke(this.value)
}

@Composable
inline fun <T> State<T?>.onDataReq(block: ComposeFun<T, Unit>) {
    if (this is Data<T> && this.isNotEmpty) block.invoke(this.reqValue)
}


// Cancel Res

@Composable
inline fun <T> State<T?>.onCancel(block: ComposeFun<Unit, Unit>) {
    if (this is Cancel) block.invoke(Unit)
}

// Error Res

@Composable
inline fun <T> State<T?>.onError(block: ComposeFun<Throwable, Unit>) {
    if (this is Error<T?>) block.invoke(this.throwable)
}
