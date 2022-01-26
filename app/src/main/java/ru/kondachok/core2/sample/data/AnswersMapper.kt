package ru.kondachok.core2.sample.data

import javax.inject.Inject
import ru.kondachok.core2.core.DataMapper
import ru.kondachok.core2.sample.data.api.ApiAnswer
import ru.kondachok.core2.sample.domian.Answer

class AnswersMapper @Inject constructor(
    private val answerMapper: AnswerMapper
) : DataMapper<List<ApiAnswer>, List<Answer>> {
    override fun invoke(arg: List<ApiAnswer>): List<Answer> {
        return arg.map { answerMapper.invoke(it) }
    }
}
