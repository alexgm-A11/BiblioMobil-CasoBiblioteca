package pe.edu.upeu.bibliomobil.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
import pe.edu.upeu.bibliomobil.data.repository.*
import pe.edu.upeu.bibliomobil.domain.repository.*
import pe.edu.upeu.bibliomobil.domain.usecase.*
import pe.edu.upeu.bibliomobil.presentation.lector.LectorViewModel
import pe.edu.upeu.bibliomobil.presentation.libro.LibroViewModel
import pe.edu.upeu.bibliomobil.presentation.prestamo.PrestamoViewModel

val dataModule=module{single<LibroRepository>{LibroRepositorioEnMemoria()};single<LectorRepository>{LectorRepositorioEnMemoria()};single<PrestamoRepository>{PrestamoRepositorioEnMemoria()}}
val domainModule=module{factory{RegistrarLibroUseCase(get())};factory{ListarLibrosUseCase(get())};factory{RegistrarLectorUseCase(get())};factory{ListarLectoresUseCase(get())};factory{RegistrarPrestamoUseCase(get(),get(),get())};factory{ListarPrestamosUseCase(get())}}
val presentationModule=module{viewModel{LibroViewModel(get(),get())};viewModel{LectorViewModel(get(),get())};viewModel{PrestamoViewModel(get(),get(),get(),get())}}
expect val platformModule:Module
fun initKoin(config:KoinApplication.()->Unit={}):KoinApplication=startKoin{config();modules(dataModule,domainModule,presentationModule,platformModule)}
