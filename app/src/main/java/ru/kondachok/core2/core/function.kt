package ru.kondachok.core2.core


/**
 * Fun - глобальный функциональный интерфейс отвечающий за передачу и вызов функций.
 *
 * @param IN
 * @param OUT
 * @constructor Create empty Fun
 */
fun interface Fun<IN, OUT> {
    fun invoke(arg: IN): OUT
}

/**
 * SuspendFun - глобальный функциональный интерфейс отвечающий за передачу и вызов suspend функций.
 *
 * @param IN
 * @param OUT
 * @constructor Create empty Suspend fun
 */
fun interface SuspendFun<IN, OUT> {
    suspend fun invoke(arg: IN): OUT
}

/**
 * Use case - глобальный функциональный интерфейс отвечающий для декларации действий
 * над репозиториями/менеджерами.
 *
 * @param IN
 * @param OUT
 * @constructor Create empty Use case
 */
fun interface UseCase<IN, OUT> : Fun<IN, OUT>

/**
 * Suspend use case - глобальный функциональный интерфейс отвечающий для декларации suspend действий
 * над репозиториями/менеджерами.
 *
 * @param IN
 * @param OUT
 * @constructor Create empty Suspend use case
 */
fun interface SuspendUseCase<IN, OUT> : SuspendFun<IN, OUT>


/**
 * Data mapper - глобальный функциональный интерфейс отвечающий за преобразования
 * одного типа данных к другому
 *
 * @param IN
 * @param OUT
 * @constructor Create empty Data mapper
 */
fun interface DataMapper<IN, OUT> : Fun<IN, OUT>

/**
 * Suspend data mapper - глобальный функциональный интерфейс отвечающий за преобразования
 * одного типа данных к другому с использованием корутин
 *
 * @param IN
 * @param OUT
 * @constructor Create empty Suspend data mapper
 */
fun interface SuspendDataMapper<IN, OUT> : SuspendFun<IN, OUT>


/**
 * As suspend - функция преобразования обычной функции в suspend функцию
 *
 * Пример
 * val a = Fun<Int, Int> { it * 2 }
 *
 * suspend fun invoke(sfx: SuspendFun<Int, Int>) = ...
 *
 * suspend fun main() {
 *   invoke(asSuspend(a))
 *   invoke(asSuspend { it * 3})
 * }
 *
 * @param IN
 * @param OUT
 * @param fx
 * @return
 */
fun<IN, OUT> asSuspend(fx: Fun<IN, OUT>): SuspendFun<IN, OUT> = SuspendFun { fx.invoke(it) }
