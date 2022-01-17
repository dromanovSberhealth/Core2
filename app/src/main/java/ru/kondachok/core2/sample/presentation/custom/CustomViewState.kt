package ru.kondachok.core2.sample.presentation.custom

import ru.kondachok.core2.core.ViewState

sealed class CustomViewState: ViewState

object StateInit: CustomViewState()

object StateClicked: CustomViewState()


