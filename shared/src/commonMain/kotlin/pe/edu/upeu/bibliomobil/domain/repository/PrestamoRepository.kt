package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Prestamo

/** Puerto para registrar préstamos y consultar su historial durante la sesión. */
interface PrestamoRepository {
    suspend fun registrar(prestamo: Prestamo, ejemplares: Int): Prestamo
    suspend fun listar(): List<Prestamo>
}
