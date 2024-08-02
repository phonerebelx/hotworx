package com.hotsquad.hotsquadlist.koin.module

import com.hotsquad.hotsquadlist.network.domain.DataSource
import com.hotsquad.hotsquadlist.repository.AppRepository
import com.hotsquad.hotsquadlist.repository.UserRepository
import org.koin.dsl.module

val RepositoryModule = module {

    /**
     * Define a Singleton of [DataSource]
     * Get Single instance of [DataSource]
     */
    single {
        DataSource()
    }

    /**
     * Define a Singleton of [AppRepository]
     * Get Single instance of [AppRepository]
     */
    single {
        AppRepository()
    }

    /**
     * Define a Singleton of [UserRepository]
     * Get Single instance of [UserRepository]
     */
    single {
        UserRepository()
    }
}