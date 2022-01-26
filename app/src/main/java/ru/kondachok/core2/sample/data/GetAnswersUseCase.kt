package ru.kondachok.core2.sample.data

import javax.inject.Inject
import ru.kondachok.core2.core.SuspendUseCase
import ru.kondachok.core2.sample.domian.Answer

class GetAnswersUseCase @Inject constructor(
    private val repository: MagicBallRepository
) : SuspendUseCase<Boolean, List<Answer>?> {
    override suspend fun invoke(arg: Boolean): List<Answer>? {
        return repository.getAnswers(arg)
    }
}
