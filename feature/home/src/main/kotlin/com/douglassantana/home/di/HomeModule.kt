package com.douglassantana.home.di

import com.douglassantana.domain.model.ScreenSource
import com.douglassantana.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val homeModule = module {
    viewModel { (source: ScreenSource) ->
        HomeViewModel(fetchScreen = get { parametersOf(source) })
    }
}
