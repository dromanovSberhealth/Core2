package ru.kondachok.core2.sample.presentation.list

import ru.kondachok.core2.core.Fun
import ru.kondachok.core2.core.State
import ru.kondachok.core2.core.SuspendFun
import ru.kondachok.core2.core.isA

// Refresh State

data class Refresh<T>(val value: T) : State<T>

val <T> State<T>.isRefresh: Boolean
    get() = this.isA<T, Refresh<T>>()

fun <T> State<T>.onRefresh(block: Fun<T, Unit>) {
    if (this is Refresh<T>) block.invoke(this.value)
}

suspend fun <T> State<T>.onRefresh(block: SuspendFun<T, Unit>) {
    if (this is Refresh<T>) block.invoke(this.value)
}

fun <T> State<T>.refreshOrNull(): T? {
    return if (this is Refresh<T>) this.value else null
}
