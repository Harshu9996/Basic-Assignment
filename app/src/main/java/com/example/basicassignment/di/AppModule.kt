package com.example.basicassignment.di

import android.content.Context
import com.example.basicassignment.data.supabaseDb.SupabaseDAO
import com.example.basicassignment.data.supabaseDb.SupabaseDB
import com.example.basicassignment.domain.repository
import com.example.basicassignment.data.repository.repositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideSupabaseDB() : SupabaseDB {
        return SupabaseDB()
    }



    @Provides
    @Singleton
    fun provideSupabaseDAO() : SupabaseDAO {
        return SupabaseDB()
    }

    @Provides
    @Singleton
    fun providerepository(supabaseDAO: SupabaseDAO) : repository {
        return repositoryImpl(supabaseDAO)
    }


}