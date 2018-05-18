package com.goalify.chat.android.chatroom.di

import com.goalify.chat.android.chatroom.service.MessageService
import com.goalify.chat.android.dagger.module.AppModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module abstract class MessageServiceProvider {
    @ContributesAndroidInjector(modules = [AppModule::class])
    abstract fun provideMessageService(): MessageService
}