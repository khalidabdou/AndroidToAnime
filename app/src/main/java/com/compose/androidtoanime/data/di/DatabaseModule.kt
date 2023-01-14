package com.compose.androidtoanime.data.di


import android.content.Context
import com.compose.androidtoanime.data.BillingUpdateListener
import com.compose.androidtoanime.data.model.BillingClientProvider
import com.compose.androidtoanime.data.roomDatabase
import com.compose.androidtoanime.preferences.implimentation.DataStoreRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = roomDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun provideDao(database: roomDatabase) = database.dao()


    @Singleton
    @Provides
    fun provideDataStoreRepository(
        @ApplicationContext app: Context
    ): DataStoreRepositoryImpl = DataStoreRepositoryImpl(app)


    @Provides
    fun provideBillingClient(
        @ApplicationContext context: Context,
        billingUpdateListener: BillingUpdateListener
    ): BillingClientProvider = BillingClientProvider(context,billingUpdateListener)


}