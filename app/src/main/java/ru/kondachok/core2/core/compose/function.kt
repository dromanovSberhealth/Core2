package ru.kondachok.core2.core.compose

import androidx.compose.runtime.Composable

typealias ComposeFun<IN, OUT> = @Composable (IN) -> OUT
