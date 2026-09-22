package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.bibliomobil.domain.model.EstadoPrestamo
import pe.edu.upeu.bibliomobil.domain.model.Prestamo
import pe.edu.upeu.bibliomobil.domain.repository.PrestamoRepository

class PrestamoRepositorioEnMemoria : PrestamoRepository {
    private val mutex = Mutex()
    private val prestamos = mutableListOf<Prestamo>()

    override suspend fun registrar(prestamo: Prestamo, ejemplares: Int): Prestamo = mutex.withLock {
        val ocupados = prestamos.count { actual ->
            actual.detalles.any { it.libro.id == prestamo.detalles.single().libro.id } &&
                actual.estado != EstadoPrestamo.Devuelto
        }
        require(ocupados < ejemplares) { "No hay ejemplares disponibles para préstamo" }
        prestamo.copy(id = (prestamos.maxOfOrNull { it.id } ?: 0L) + 1L).also(prestamos::add)
    }

    override suspend fun listar(): List<Prestamo> = mutex.withLock { prestamos.toList() }
}
