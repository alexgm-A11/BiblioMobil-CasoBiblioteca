package pe.edu.upeu.bibliomobil.presentation.prestamo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.ListarPrestamosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.PrestamoInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarPrestamoUseCase

class PrestamoViewModel(
    private val listarLibros: ListarLibrosUseCase,
    private val listarLectores: ListarLectoresUseCase,
    private val listarPrestamos: ListarPrestamosUseCase,
    private val registrarPrestamo: RegistrarPrestamoUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PrestamoUiState())
    val uiState: StateFlow<PrestamoUiState> = _uiState.asStateFlow()

    init { cargarPrestamos() }

    fun cargarPrestamos() = viewModelScope.launch {
        _uiState.update { it.copy(fase = FasePrestamos.Cargando, mensajeError = null) }
        val libros = listarLibros().getOrElse {
            mostrarErrorCarga(); return@launch
        }
        val lectores = listarLectores().getOrElse {
            mostrarErrorCarga(); return@launch
        }
        val prestamos = listarPrestamos().getOrElse {
            mostrarErrorCarga(); return@launch
        }
        _uiState.update {
            it.copy(
                libros = libros,
                lectores = lectores,
                fase = if (prestamos.isEmpty()) FasePrestamos.SinPrestamos else FasePrestamos.ConPrestamos(prestamos)
            )
        }
    }

    private fun mostrarErrorCarga() {
        _uiState.update { it.copy(fase = FasePrestamos.Error("No se pudieron cargar los préstamos")) }
    }

    fun onLibroChange(id: Long) {
        _uiState.update { it.copy(formulario = it.formulario.copy(libroId = id, errorLibro = null), mensajeError = null, mensajeExito = null) }
    }

    fun onLectorChange(id: Long) {
        _uiState.update { it.copy(formulario = it.formulario.copy(lectorId = id, errorLector = null), mensajeError = null, mensajeExito = null) }
    }

    fun onDiasChange(valor: String) {
        _uiState.update { it.copy(formulario = it.formulario.copy(dias = valor, errorDias = null), mensajeError = null, mensajeExito = null) }
    }

    fun registrar() {
        if (_uiState.value.registrando) return
        val formulario = _uiState.value.formulario
        _uiState.update { it.copy(registrando = true, mensajeError = null, mensajeExito = null) }
        viewModelScope.launch {
            registrarPrestamo(formulario.libroId, formulario.lectorId, formulario.dias).fold(
                onSuccess = { prestamo ->
                    _uiState.update {
                        val anteriores = (it.fase as? FasePrestamos.ConPrestamos)?.prestamos.orEmpty()
                        it.copy(
                            formulario = FormularioPrestamo(),
                            registrando = false,
                            mensajeExito = "Préstamo de \"${prestamo.detalles.single().libro.titulo}\" registrado correctamente",
                            fase = FasePrestamos.ConPrestamos(anteriores + prestamo)
                        )
                    }
                    cargarPrestamos()
                },
                onFailure = { error ->
                    if (error is PrestamoInvalidoException) {
                        _uiState.update {
                            it.copy(
                                registrando = false,
                                formulario = it.formulario.copy(
                                    errorLibro = error.errores.libro,
                                    errorLector = error.errores.lector,
                                    errorDias = error.errores.dias
                                )
                            )
                        }
                    } else {
                        _uiState.update { it.copy(registrando = false, mensajeError = error.message ?: "No se pudo registrar el préstamo") }
                    }
                }
            )
        }
    }
}
