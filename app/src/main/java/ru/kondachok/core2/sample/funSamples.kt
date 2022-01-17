package ru.kondachok.core2.sample

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.kondachok.core2.core.DataMapper
import ru.kondachok.core2.core.Fun
import ru.kondachok.core2.core.SuspendDataMapper
import ru.kondachok.core2.core.SuspendFun

// Перегружаемые функции

fun overloadFun(fx: Fun<Int, String>) {
    print(fx.invoke(42))
}

suspend fun overloadFun(fx: SuspendFun<Int, String>) {
    print(fx.invoke(42))
}

// Не перегружаемые функции

fun notOverloadFun(fx: Fun<Int, String>) {
    print(fx.invoke(42))
}

// примеры

class IntToStrDataMapper: DataMapper<Int, String> {
    override fun invoke(arg: Int): String = arg.toString()
}

class IntToStrSuspendDataMapper: SuspendDataMapper<Int, String> {
    override suspend fun invoke(arg: Int): String = arg.toString()
}

val defMapper = DataMapper<Int, String> { it.toString() }
val defSuspendMapper = SuspendDataMapper<Int, String> { it.toString() }

fun main() {
    // примеры не перегружаемых функций
    notOverloadFun { it.toString() }
    notOverloadFun(Fun { it.toString() })
    notOverloadFun(DataMapper { it.toString() })
    notOverloadFun(defMapper)
    notOverloadFun(IntToStrDataMapper())

    // примеры перегружаемого кода

    overloadFun(Fun { it.toString() })
    overloadFun(DataMapper { it.toString() })
    overloadFun(defMapper)
    overloadFun(IntToStrDataMapper())

    GlobalScope.launch {
        overloadFun(SuspendFun { it.toString() })
        overloadFun(SuspendDataMapper {  it.toString() })
        overloadFun(defSuspendMapper)
        overloadFun(IntToStrSuspendDataMapper())
    }
}
