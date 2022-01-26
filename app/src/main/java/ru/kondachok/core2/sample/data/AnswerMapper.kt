package ru.kondachok.core2.sample.data

import javax.inject.Inject
import ru.kondachok.core2.core.DataMapper
import ru.kondachok.core2.sample.data.api.ApiAnswer
import ru.kondachok.core2.sample.domian.Answer

class AnswerMapper @Inject constructor() : DataMapper<ApiAnswer, Answer> {
    override fun invoke(arg: ApiAnswer): Answer {
        return Answer(arg.text)
    }
}
