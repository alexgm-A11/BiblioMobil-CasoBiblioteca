package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Lector

/** Puerto de lectores para registrar una persona y consultar la cartera de lectores. */
interface LectorRepository {
    suspend fun registrar(lector: Lector): Lector
    suspend fun listar(): List<Lector>
}
