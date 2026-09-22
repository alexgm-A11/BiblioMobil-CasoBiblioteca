package pe.edu.upeu.bibliomobil.presentation.prestamo

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.model.Prestamo

data class FormularioPrestamo(
    val libroId: Long? = null,
    val lectorId: Long? = null,
    val dias: String = "7",
    val errorLibro: String? = null,
    val errorLector: String? = null,
    val errorDias: String? = null
)

sealed interface FasePrestamos {
    data object Cargando : FasePrestamos
    data object SinPrestamos : FasePrestamos
    data class ConPrestamos(val prestamos: List<Prestamo>) : FasePrestamos
    data class Error(val mensaje: String) : FasePrestamos
}

data class PrestamoUiState(
    val libros: List<Libro> = emptyList(),
    val lectores: List<Lector> = emptyList(),
    val formulario: FormularioPrestamo = FormularioPrestamo(),
    val fase: FasePrestamos = FasePrestamos.Cargando,
    val registrando: Boolean = false,
    val mensajeExito: String? = null,
    val mensajeError: String? = null
)
