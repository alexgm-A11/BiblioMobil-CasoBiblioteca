package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

class LectorRepositorioEnMemoria(private val latenciaMs: Long = 350L) : LectorRepository {
    private val mutex = Mutex()
    private val lectores = mutableListOf<Lector>()
    override suspend fun registrar(lector: Lector): Lector { delay(latenciaMs); return mutex.withLock { lector.copy(id = (lectores.maxOfOrNull { it.id } ?: 0L) + 1).also(lectores::add) } }
    override suspend fun listar(): List<Lector> { delay(latenciaMs); return mutex.withLock { lectores.toList() } }
}
