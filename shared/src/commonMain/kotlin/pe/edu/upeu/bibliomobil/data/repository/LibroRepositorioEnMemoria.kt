package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

class LibroRepositorioEnMemoria(private val latenciaMs: Long = 350L) : LibroRepository {
    private val mutex = Mutex()
    private val libros = mutableListOf<Libro>()
    override suspend fun registrar(libro: Libro): Libro { delay(latenciaMs); return mutex.withLock { libro.copy(id = (libros.maxOfOrNull { it.id } ?: 0L) + 1).also(libros::add) } }
    override suspend fun listar(): List<Libro> { delay(latenciaMs); return mutex.withLock { libros.toList() } }
}
