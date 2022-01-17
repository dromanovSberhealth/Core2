package ru.kondachok.core2.sample.data

import ru.kondachok.core2.core.DataMapper
import ru.kondachok.core2.sample.data.api.ApiAnswer
import ru.kondachok.core2.sample.data.api.MagicBallApi
import ru.kondachok.core2.sample.domian.Answer

class MagicBallRepository(
    private val api: MagicBallApi,
    private val answersMapper: DataMapper<List<ApiAnswer>, List<Answer>>
) {

    private var cache: List<Answer> = emptyList()

    fun clearAnswers() {
        cache = emptyList()
    }

    suspend fun getAnswers(force: Boolean = false): List<Answer> {
        if (force) clearAnswers()
        if (cache.isEmpty()) {
            cache = answersMapper.invoke(api.getAnswers())
        }
        return cache
    }
}
