package ru.kondachok.core2.sample.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kondachok.core2.core.DataMapper
import ru.kondachok.core2.core.SuspendUseCase
import ru.kondachok.core2.core.UseCase
import ru.kondachok.core2.sample.data.AnswersMapper
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
    fun provideMagicBallRepository(
        api: MagicBallApi,
        answersMapper: AnswersMapper
    ): MagicBallRepository {
        return MagicBallRepository(api, answersMapper)
    }
}
