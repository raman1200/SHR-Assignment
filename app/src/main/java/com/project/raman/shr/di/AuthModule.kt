package com.project.raman.shr.di

import com.google.firebase.auth.FirebaseAuth
import com.project.raman.shr.data.datasource.FirebaseDB
import com.project.raman.shr.data.repository.AuthRepositoryImpl
import com.project.raman.shr.domain.repository.AuthRepository
import com.project.raman.shr.domain.usecase.GetCurrentUserUseCase
import com.project.raman.shr.domain.usecase.SignInWithGoogleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseDB(auth: FirebaseAuth): FirebaseDB =
        FirebaseDB(auth)

    @Provides
    fun provideAuthRepository(
        firebaseDB: FirebaseDB
    ): AuthRepository = AuthRepositoryImpl(firebaseDB)

    @Provides
    fun provideSignInWithGoogleUseCase(repo: AuthRepository): SignInWithGoogleUseCase =
        SignInWithGoogleUseCase(repo)

    @Provides
    fun provideGetCurrentUserUseCase(repo: AuthRepository): GetCurrentUserUseCase =
        GetCurrentUserUseCase(repo)
}
