package com.example.store_app.application.injection

import com.example.store_app.domain.RepositoryImpl
import com.example.store_app.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityModule {
    @Binds
    abstract fun bindIRepository(repositoryImpl: RepositoryImpl): Repository
}