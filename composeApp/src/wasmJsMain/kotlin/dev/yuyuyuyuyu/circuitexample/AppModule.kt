package dev.yuyuyuyuyu.circuitexample

import org.koin.dsl.module

val appModule = module {
    single<EmailRepository> { EmailRepository() }
}
