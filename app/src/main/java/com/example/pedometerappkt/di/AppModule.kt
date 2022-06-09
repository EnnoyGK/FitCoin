package com.example.pedometerappkt.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.example.pedometerappkt.db.SessionDatabase
import com.example.pedometerappkt.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.pedometerappkt.other.Constants.KEY_NAME
import com.example.pedometerappkt.other.Constants.KEY_SHOP_TYPE
//import com.example.pedometerappkt.other.Constants.KEY_WEIGHT
import com.example.pedometerappkt.other.Constants.SESSION_DATABASE_NAME
import com.example.pedometerappkt.other.Constants.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        SessionDatabase::class.java,
        SESSION_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideSessionDao(db: SessionDatabase) = db.getSessionDao()

    @Singleton
    @Provides
    fun provideShopDao(db: SessionDatabase) = db.getShopDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app : Context) =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideName(sharedPref: SharedPreferences) = sharedPref.getString(KEY_NAME, "") ?: ""

    @Singleton
    @Provides
    fun provideShopType(sharedPref: SharedPreferences) = sharedPref.getString(KEY_SHOP_TYPE, "") ?: ""

   /* @Singleton
    @Provides
    fun provideWeight(sharedPref: SharedPreferences) = sharedPref.getFloat(KEY_WEIGHT, 80f)
*/
    @Singleton
    @Provides
    fun provideFirstTimeToggle(sharedPref: SharedPreferences) = sharedPref.getBoolean(KEY_FIRST_TIME_TOGGLE, true)
}