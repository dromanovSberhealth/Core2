package ru.kondachok.core2.sample.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kondachok.core2.core.DataMapper
import ru.kondachok.core2.core.SuspendUseCase
import ru.kondachok.core2.core.UseCase
import ru.kondachok.core2.sample.data.MagicBallRepository
import ru.kondachok.core2.sample.data.api.ApiAnswer
import ru.kondachok.core2.sample.data.api.MagicBallApi
import ru.kondachok.core2.sample.domian.Answer

@Module
@InstallIn(SingletonComponent::class)
class MagicBallModule {

    @Provides
    fun provideMagicBallApi() = MagicBallApi()

    @Provides
    fun provideAnswerMapper() = DataMapper<ApiAnswer, Answer> { Answer(it.text) }

    @Provides
    fun provideAnswersMapper(
        answerMapper: DataMapper<ApiAnswer, Answer>
    ) = DataMapper<List<ApiAnswer>, List<Answer>> { items ->
        items.map { answerMapper.invoke(it) }
    }

    @Provides
    fun provideMagicBallRepository(
        api: MagicBallApi,
        answersMapper: DataMapper<List<ApiAnswer>, List<Answer>>
    ): MagicBallRepository {
        return MagicBallRepository(api, answersMapper)
    }

    @Provides
    fun provideClearAnswersUseCase(repository: MagicBallRepository) = UseCase<Unit, Unit> {
        repository.clearAnswers()
    }

    @Provides
    fun provideGetAnswersUseCase(repository: MagicBallRepository) = SuspendUseCase<Boolean, List<Answer>> {
        repository.getAnswers(it)
    }
}
