package ru.kondachok.core2.sample.data

import javax.inject.Inject
import ru.kondachok.core2.core.UseCase

class CleanAnswersUseCase @Inject constructor(
    private val repository: MagicBallRepository
) : UseCase<Unit, Unit> {
    override fun invoke(arg: Unit): Unit {
        return repository.clearAnswers()
    }
}
