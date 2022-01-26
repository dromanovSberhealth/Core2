package ru.kondachok.core2.sample.data

import kotlin.random.Random
import ru.kondachok.core2.sample.data.api.MagicBallApi
import ru.kondachok.core2.sample.domian.Answer

class MagicBallRepository(
    private val api: MagicBallApi,
    private val answersMapper: AnswersMapper
) {

    private var cache: List<Answer> = emptyList()

    fun clearAnswers() {
        cache = emptyList()
    }

    suspend fun getAnswers(force: Boolean = false): List<Answer>? {
        if(Random.nextInt(0, 10) == 2) return null
        if (force) clearAnswers()
        if (cache.isEmpty()) {
            cache = answersMapper.invoke(api.getAnswers())
        }
        return cache
    }
}
