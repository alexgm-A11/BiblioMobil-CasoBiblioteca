package pe.edu.upeu.bibliomobil

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import kotlin.test.*
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import pe.edu.upeu.bibliomobil.data.repository.LibroRepositorioEnMemoria
import pe.edu.upeu.bibliomobil.di.*
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository
import pe.edu.upeu.bibliomobil.domain.usecase.*
import pe.edu.upeu.bibliomobil.presentation.libro.*

class LibroViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()
    @BeforeTest fun preparar() { Dispatchers.setMain(dispatcher) }
    @AfterTest fun restaurar() { Dispatchers.resetMain() }
    private fun vm(repo: FakeLibroRepository) = LibroViewModel(RegistrarLibroUseCase(repo), ListarLibrosUseCase(repo))
    @Test fun arrancaSinLibros() = runTest { assertIs<FaseLibros.SinLibros>(vm(FakeLibroRepository()).uiState.value.fase) }
    @Test fun muestraLineaSecundaria() = runTest { val repo=FakeLibroRepository();repo.datos+=Libro(1,"T","A",1998,3);val fase=vm(repo).uiState.value.fase as FaseLibros.ConLibros;assertEquals("1998 · 3 ejemplares",fase.libros.single().lineaSecundaria) }
    @Test fun pasaAError() = runTest { assertIs<FaseLibros.Error>(vm(FakeLibroRepository(true)).uiState.value.fase) }
    @Test fun validacionQuedaEnFormulario() = runTest { val vm=vm(FakeLibroRepository());vm.registrar();assertEquals("El título es obligatorio",vm.uiState.value.formulario.errorTitulo);assertIs<FaseLibros.SinLibros>(vm.uiState.value.fase) }
    @Test fun registrarLimpiaYRecarga() = runTest { val vm=vm(FakeLibroRepository());vm.onTituloChange("T");vm.onAutorChange("A");vm.onAnioChange("1998");vm.onEjemplaresChange("3");vm.registrar();assertEquals("",vm.uiState.value.formulario.titulo);assertIs<FaseLibros.ConLibros>(vm.uiState.value.fase) }
}

class AppModuleTest {
    @AfterTest fun cerrar() { stopKoin() }
    @Test fun resuelveRepositorioPorInterfaz() { val k=startKoin { modules(dataModule,domainModule,presentationModule) }.koin;assertIs<LibroRepositorioEnMemoria>(k.get<LibroRepository>()) }
    @Test fun repositorioEsUnico() { val k=startKoin { modules(dataModule) }.koin;assertSame(k.get<LibroRepository>(),k.get<LibroRepository>()) }
    @Test fun resuelveCuatroCasos() { val k=startKoin { modules(dataModule,domainModule) }.koin;assertNotNull(k.get<RegistrarLibroUseCase>());assertNotNull(k.get<ListarLibrosUseCase>());assertNotNull(k.get<RegistrarLectorUseCase>());assertNotNull(k.get<ListarLectoresUseCase>()) }
}
