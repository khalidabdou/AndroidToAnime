package com.compose.androidtoanime.data.di

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener
import com.compose.androidtoanime.data.model.BillingClientProvider
import com.compose.androidtoanime.data.roomDatabase
import com.compose.androidtoanime.preferences.abstraction.PricingRepository
import com.compose.androidtoanime.preferences.implimentation.DataStoreRepositoryImpl
import com.compose.androidtoanime.preferences.implimentation.PricingRepositoryImpl


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
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

    @Singleton
    @Provides
    fun providePricingRepository(
        billingClient: BillingClientProvider
    ): PricingRepository = PricingRepositoryImpl(billingClient)


    @Singleton
    @Provides
    fun billingClientProvider(
        @ApplicationContext context: Context,
        updateListener: PurchasesUpdatedListener
    ): BillingClient {
        return BillingClient
            .newBuilder(context)
            .enablePendingPurchases()
            .setListener(updateListener)
            .build()
    }


}