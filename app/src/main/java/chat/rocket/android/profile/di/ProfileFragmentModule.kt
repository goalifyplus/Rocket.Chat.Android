package com.goalify.chat.android.profile.di

import android.arch.lifecycle.LifecycleOwner
import com.goalify.chat.android.dagger.scope.PerFragment
import com.goalify.chat.android.profile.presentation.ProfileView
import com.goalify.chat.android.profile.ui.ProfileFragment
import dagger.Module
import dagger.Provides

@Module
@PerFragment
class ProfileFragmentModule {

    @Provides
    fun profileView(frag: ProfileFragment): ProfileView {
        return frag
    }

    @Provides
    fun provideLifecycleOwner(frag: ProfileFragment): LifecycleOwner {
        return frag
    }
}