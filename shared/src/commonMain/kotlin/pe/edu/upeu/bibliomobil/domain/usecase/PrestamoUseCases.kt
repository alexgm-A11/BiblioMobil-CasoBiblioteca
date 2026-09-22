package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.DetallePrestamo
import pe.edu.upeu.bibliomobil.domain.model.EstadoPrestamo
import pe.edu.upeu.bibliomobil.domain.model.Prestamo
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository
import pe.edu.upeu.bibliomobil.domain.repository.PrestamoRepository

data class ErroresDePrestamo(
    val libro: String? = null,
    val lector: String? = null,
    val dias: String? = null
)

class PrestamoInvalidoException(val errores: ErroresDePrestamo) : IllegalArgumentException("Datos del préstamo inválidos")

class RegistrarPrestamoUseCase(
    private val libros: LibroRepository,
    private val lectores: LectorRepository,
    private val prestamos: PrestamoRepository
) {
    suspend operator fun invoke(libroId: Long?, lectorId: Long?, diasTexto: String): Result<Prestamo> = resultadoDe {
        val dias = diasTexto.trim().toIntOrNull()
        val errores = ErroresDePrestamo(
            libro = if (libroId == null) "Seleccione un libro" else null,
            lector = if (lectorId == null) "Seleccione un lector" else null,
            dias = if (dias == null || dias !in 1..DetallePrestamo.DIAS_MAXIMOS) "Ingrese un plazo de 1 a 7 días" else null
        )
        if (errores.libro != null || errores.lector != null || errores.dias != null) {
            throw PrestamoInvalidoException(errores)
        }
        val libro = libros.listar().firstOrNull { it.id == libroId }
            ?: throw PrestamoInvalidoException(ErroresDePrestamo(libro = "Seleccione un libro válido"))
        val lector = lectores.listar().firstOrNull { it.id == lectorId }
            ?: throw PrestamoInvalidoException(ErroresDePrestamo(lector = "Seleccione un lector válido"))
        if (libro.ejemplares == 0) {
            throw PrestamoInvalidoException(ErroresDePrestamo(libro = "No hay ejemplares disponibles para préstamo"))
        }
        requireNotNull(dias)
        prestamos.registrar(
            Prestamo(0L, lector, listOf(DetallePrestamo(libro, dias)), EstadoPrestamo.Entregado),
            libro.ejemplares
        )
    }
}

class ListarPrestamosUseCase(private val prestamos: PrestamoRepository) {
    suspend operator fun invoke(): Result<List<Prestamo>> = resultadoDe { prestamos.listar() }
}
