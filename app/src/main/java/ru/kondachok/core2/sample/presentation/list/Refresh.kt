package ru.kondachok.core2.sample.presentation.list

import ru.kondachok.core2.core.Data
import ru.kondachok.core2.core.State
import ru.kondachok.core2.core.Fun
import ru.kondachok.core2.core.Res
import ru.kondachok.core2.core.SuspendFun
import ru.kondachok.core2.core.isA

// Refresh State

data class Refresh<out T>(private val data: T?) : State<T?> {

    val isEmpty: Boolean
        get() = data == null

    val isNotEmpty: Boolean
        get() = data != null

    val value: T?
        get() = data

    val reqValue: T
        get() = data ?: throw NullPointerException("Required data is empty from: $this")
}

val <T> State<T?>.isRefresh: Boolean
    get() = this.isA<T, Refresh<T>>()

fun <T> State<T?>.onRefresh(block: Fun<T?, Unit>) {
    if (this is Refresh<T>) block.invoke(this.value)
}

fun <T> State<T?>.onRefreshReq(block: Fun<T, Unit>) {
    if (this is Refresh<T> && this.isNotEmpty) block.invoke(this.reqValue)
}

suspend fun <T> State<T?>.onRefresh(block: SuspendFun<T?, Unit>) {
    if (this is Refresh<T>) block.invoke(this.value)
}

suspend fun <T> State<T?>.onRefreshReq(block: SuspendFun<T, Unit>) {
    if (this is Refresh<T> && this.isNotEmpty) block.invoke(this.reqValue)
}

fun <T> State<T?>.refreshOrNull(): T? {
    return if (this is Refresh<T>) this.value else null
}
