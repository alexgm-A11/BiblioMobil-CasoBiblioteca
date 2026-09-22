package pe.edu.upeu.bibliomobil

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import pe.edu.upeu.bibliomobil.data.repository.PrestamoRepositorioEnMemoria
import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.PrestamoRepository
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.ListarPrestamosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.PrestamoInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarPrestamoUseCase
import pe.edu.upeu.bibliomobil.presentation.prestamo.FasePrestamos
import pe.edu.upeu.bibliomobil.presentation.prestamo.PrestamoViewModel

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class PrestamoTest {
    private val libro = Libro(1, "El principito", "Saint-Exupéry", 1943, 1)
    private val lector = Lector(1, "Ana", "ana@upeu.edu.pe", null)
    private lateinit var libros: FakeLibroRepository
    private lateinit var lectores: FakeLectorRepository
    private lateinit var prestamos: PrestamoRepository
    private val dispatcher = UnconfinedTestDispatcher()

    @BeforeTest fun preparar() {
        Dispatchers.setMain(dispatcher)
        libros = FakeLibroRepository().also { it.datos += libro }
        lectores = FakeLectorRepository().also { it.datos += lector }
        prestamos = PrestamoRepositorioEnMemoria()
    }

    @AfterTest fun restaurar() { Dispatchers.resetMain() }

    private fun registrar() = RegistrarPrestamoUseCase(libros, lectores, prestamos)

    @Test fun registraLibroLectorYPlazo() = runTest {
        val creado = registrar()(1, 1, "7").getOrThrow()
        assertEquals(1L, creado.id)
        assertEquals(libro, creado.detalles.single().libro)
        assertEquals(lector, creado.lector)
        assertEquals(7, creado.detalles.single().dias)
        assertEquals(listOf(creado), ListarPrestamosUseCase(prestamos)().getOrThrow())
    }

    @Test fun rechazaPlazoMayorAUnaSemana() = runTest {
        val error = registrar()(1, 1, "8").exceptionOrNull()
        assertIs<PrestamoInvalidoException>(error)
        assertEquals("Ingrese un plazo de 1 a 7 días", error.errores.dias)
    }

    @Test fun exigeLibroYLectorRegistrados() = runTest {
        val error = registrar()(null, null, "7").exceptionOrNull()
        assertIs<PrestamoInvalidoException>(error)
        assertEquals("Seleccione un libro", error.errores.libro)
        assertEquals("Seleccione un lector", error.errores.lector)
        assertTrue(prestamos.listar().isEmpty())
    }

    @Test fun evitaPrestarMasEjemplaresQueLosDisponibles() = runTest {
        registrar()(1, 1, "7").getOrThrow()
        val segundo = registrar()(1, 1, "7")
        assertTrue(segundo.isFailure)
        assertEquals(1, prestamos.listar().size)
    }

    @Test fun viewModelMuestraRegistroEnListado() = runTest {
        val vm = PrestamoViewModel(
            ListarLibrosUseCase(libros), ListarLectoresUseCase(lectores),
            ListarPrestamosUseCase(prestamos), registrar()
        )
        assertIs<FasePrestamos.SinPrestamos>(vm.uiState.value.fase)
        vm.onLibroChange(1)
        vm.onLectorChange(1)
        vm.onDiasChange("5")
        vm.registrar()
        val fase = vm.uiState.value.fase
        assertIs<FasePrestamos.ConPrestamos>(fase)
        assertEquals(5, fase.prestamos.single().detalles.single().dias)
        assertEquals(null, vm.uiState.value.formulario.libroId)
    }
}
