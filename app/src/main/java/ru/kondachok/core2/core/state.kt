package ru.kondachok.core2.core

// Global State interface

/**
 * State - глобальный маркерный интерфейс отвечающий за какое либо состояние чего либо.
 * К состояниям относятся: состояние загрузки данных, состояние view слоя
 *
 * @param T
 * @constructor Create empty State
 */
interface State<out T>

/**
 * Is a - функция расширения для проверки принадлежности текущего объекта к указанному классу,
 * дочернему от State
 *
 * @param K
 * @param T
 * @return
 */
inline fun <T, reified K : State<T>> State<T>.isA(): Boolean {
    return this is K
}

/**
 * Require as - функция расширения для приведения текущего объекта к указанному классу,
 * дочернему от State, в противном случае выбрасывается исключение ClassCastException
 *
 * @param K
 * @param T
 * @return
 */
inline fun <T, reified K : State<T>> State<T>.requiredAs(): K {
    return if (this is K) this else throw ClassCastException("$this is not ${K::class}")
}

// Global ViewState

/**
 * View state - глобальный маркерный класс для пометки кастомного состояния view слоя.
 * Требуется чтобы более явно разграничить состояние загрузки и view состояние,
 * с сохранением использования расширений для интерфейса State
 *
 * @constructor Create empty View state
 */
interface ViewState : State<Nothing>

// Global Res state

/**
 * Res - глобальный sealed класс для обозначения состояния загрузки данных.
 *
 * Жц: init -> load -> (Data/Error/Cancel)
 *
 * @param T
 * @constructor Create empty Res
 */
sealed class Resource<out T> : State<T>

// Init Res

/**
 * Init - состояние помечающее, что загрузка данных еще не происходила
 *
 * @constructor Create empty Init
 */
object Init : Resource<Nothing>()

/**
 * Is init - возвращает резульат проверки, является ли текущий объект классом Init
 */
val <T> State<T>.isInit: Boolean
    get() = this.isA<T, Init>()

/**
 * On init - выполняет функцию, указанную в блоке Fun
 * , если текущий объект является классом Init
 *
 * @param T
 * @param block
 */
fun <T> State<T>.onInit(block: Fun<Unit, Unit>) {
    if (this is Init) block.invoke(Unit)
}

/**
 * On init - выполняет suspend функцию, указанную в блоке SuspendFun
 * , если текущий объект является классом Init
 *
 * @param T
 * @param block
 */
suspend fun <T> State<T>.onInit(block: SuspendFun<Unit, Unit>) {
    if (this is Init) block.invoke(Unit)
}

// Load Res

/**
 * Load - состояние помечающее, что загрузка данных в процессе
 *
 * @constructor Create empty Load
 */
object Load : Resource<Nothing>()

/**
 * Is Load - возвращает резульат проверки, является ли текущий объект классом Load
 */
val <T> State<T>.isLoad: Boolean
    get() = this.isA<T, Load>()

/**
 * On load - выполняет функцию, указанную в блоке Fun
 * , если текущий объект является классом Load
 *
 * @param T
 * @param block
 */
fun <T> State<T>.onLoad(block: Fun<Unit, Unit>) {
    if (this is Load) block.invoke(Unit)
}

/**
 * On load - выполняет suspend функцию, указанную в блоке SuspendFun
 * , если текущий объект является классом Load
 *
 * @param T
 * @param block
 */
suspend fun <T> State<T>.onLoad(block: SuspendFun<Unit, Unit>) {
    if (this is Load) block.invoke(Unit)
}

// Data Res

/**
 * Data - состояние помечающее, что загрузка данных успешно завершена
 *
 * @param T
 * @property data - возвращаемые данные
 * @constructor Create empty Data
 */
data class Data<T>(var value: T) : Resource<T>()

/**
 * Is data - возвращает резульат проверки, является ли текущий объект классом Data от типа T
 */
val <T> State<T>.isData: Boolean
    get() = this.isA<T, Data<T>>()

/**
 * On data  - выполняет функцию, указанную в блоке Fun
 * , если текущий объект является классом Data от типа T
 *
 * @param T
 * @param block
 */
fun <T> State<T>.onData(block: Fun<T, Unit>) {
    if (this is Data<T>) block.invoke(this.value)
}

/**
 * On data  - выполняет suspend функцию, указанную в блоке SuspendFun
 * , если текущий объект является классом Data от типа T
 *
 * @param T
 * @param block
 */
suspend fun <T> State<T>.onData(block: SuspendFun<T, Unit>) {
    if (this is Data<T>) block.invoke(this.value)
}

/**
 * Data or null - если текущий объект является классом Data от типа T, возвращает значение объекта
 *
 * @param T
 * @return
 */
fun <T> State<T>.dataOrNull(): T? {
    return if (this is Data<T>) this.value else null
}

// Error Res

/**
 * Error - состояние помечающее, что загрузка произошла с ошибкой
 *
 * @param T
 * @property throwable - возвращаемая ошибка
 * @constructor Create empty Error
 */
data class Error<out T>(val throwable: Throwable) : Resource<T>()

/**
 * Is error - возвращает резульат проверки, является ли текущий объект классом Error от типа T
 */
val <T> State<T>.isError: Boolean
    get() = this.isA<T, Error<T>>()

/**
 * On error - выполняет функцию, указанную в блоке Fun
 * , если текущий объект является классом Error от типа T
 *
 * @param T
 * @param block
 */
fun <T> State<T>.onError(block: Fun<Throwable, Unit>) {
    if (this is Error<T>) block.invoke(this.throwable)
}

/**
 * On error - выполняет suspend функцию, указанную в блоке Fun
 * , если текущий объект является классом Error от типа T
 *
 * @param T
 * @param block
 */
suspend fun <T> State<T>.onError(block: SuspendFun<Throwable, Unit>) {
    if (this is Error<T>) block.invoke(this.throwable)
}

/**
 * Error or null - если текущий объект является классом Error от типа T, возвращает значение
 *
 * @param T
 * @return
 */
fun <T> State<T>.errorOrNull(): Throwable? {
    return if (this is Error<T>) this.throwable else null
}

// Null Res

/**
 * Cancel состояние помечающее, что загрузка началась, но была отменена
 *
 * @constructor Create empty Cancel
 */
object Cancel : Resource<Nothing>()

/**
 * Is cancel - возвращает резульат проверки, является ли текущий объект классом Cancel
 */
val <T> State<T>.isCancel: Boolean
    get() = this.isA<T, Cancel>()

/**
 * On cancel - выполняет функцию, указанную в блоке Fun
 * , если текущий объект является классом Cancel
 *
 * @param T
 * @param block
 */
fun <T> State<T>.onCancel(block: Fun<Unit, Unit>) {
    if (this is Cancel) block.invoke(Unit)
}

/**
 * On cancel - выполняет suspend функцию, указанную в блоке SuspendFun
 * , если текущий объект является классом Cancel
 *
 * @param T
 * @param block
 */
suspend fun <T> State<T>.onCancel(block: SuspendFun<Unit, Unit>) {
    if (this is Cancel) block.invoke(Unit)
}

// Res

/**
 * Req res - возвращает класс State приведенный к классу Res или ошибки
 *
 * @param T
 * @return
 */
fun <T> State<T>.requiredResource(): Resource<T> {
    return this.requiredAs()
}

/**
 * On - выполняет указанную функцию, в зависимости от текущего состояния ресурса
 *
 * @param T
 * @param init
 * @param load
 * @param cancel
 * @param data
 * @param error
 */
fun <T> Resource<T>.on(
    init: Fun<Unit, Unit> = Fun {},
    load: Fun<Unit, Unit> = Fun {},
    cancel: Fun<Unit, Unit> = Fun {},
    data: Fun<T, Unit> = Fun {},
    error: Fun<Throwable, Unit> = Fun {},
) {
    when (this) {
        is Init -> init.invoke(Unit)
        is Load -> load.invoke(Unit)
        is Cancel -> cancel.invoke(Unit)
        is Data -> data.invoke(this.value)
        is Error -> error.invoke(this.throwable)
    }
}

/**
 * On - выполняет указанную suspend функцию, в зависимости от текущего состояния ресурса
 *
 * @param T
 * @param init
 * @param load
 * @param cancel
 * @param data
 * @param error
 */
suspend fun <T> Resource<T>.on(
    init: SuspendFun<Unit, Unit> = SuspendFun {},
    load: SuspendFun<Unit, Unit> = SuspendFun {},
    cancel: Fun<Unit, Unit> = Fun {},
    data: SuspendFun<T, Unit> = SuspendFun {},
    error: SuspendFun<Throwable, Unit> = SuspendFun {},
) {
    when (this) {
        is Init -> init.invoke(Unit)
        is Load -> load.invoke(Unit)
        is Cancel -> cancel.invoke(Unit)
        is Data -> data.invoke(this.value)
        is Error -> error.invoke(this.throwable)
    }
}

/**
 * As other res - приводит текущий ресурс к ресурсу с новыми данными
 *
 * @param T
 * @param N
 * @param data
 * @return
 */
fun <T, N> Resource<T>.map(data: N): Resource<N> {
    return when (this) {
        is Init -> Init
        is Load -> Load
        is Cancel -> Cancel
        is Data -> Data(data)
        is Error -> Error(this.throwable)
    }
}

/**
 * As other res - приводит текущий ресурс к ресурсу с новыми данныи от указанной Fun
 *
 * @param T
 * @param N
 * @param mapper
 * @return
 */
fun <T, N> Resource<T>.map(mapper: Fun<T, N>): Resource<N> {
    return when (this) {
        is Init -> Init
        is Load -> Load
        is Cancel -> Cancel
        is Data -> Data(mapper.invoke(this.value))
        is Error -> Error(this.throwable)
    }
}

/**
 * As other res - приводит текущий ресурс к ресурсу с новыми данныи от указанной SuspendFun
 *
 * @param T
 * @param N
 * @param mapper
 * @return
 */
suspend fun <T, N> Resource<T>.map(mapper: SuspendFun<T, N>): Resource<N> {
    return when (this) {
        is Init -> Init
        is Load -> Load
        is Cancel -> Cancel
        is Data -> Data(mapper.invoke(this.value))
        is Error -> Error(this.throwable)
    }
}
