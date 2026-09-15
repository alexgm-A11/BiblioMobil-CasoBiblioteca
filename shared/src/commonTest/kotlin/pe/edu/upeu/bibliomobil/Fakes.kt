package pe.edu.upeu.bibliomobil

import pe.edu.upeu.bibliomobil.domain.model.*
import pe.edu.upeu.bibliomobil.domain.repository.*

class FakeLibroRepository(var falla:Boolean=false):LibroRepository{val datos=mutableListOf<Libro>();override suspend fun registrar(libro:Libro):Libro{if(falla)error("fallo");return libro.copy(id=(datos.size+1).toLong()).also(datos::add)};override suspend fun listar():List<Libro>{if(falla)error("fallo");return datos.toList()}}
class FakeLectorRepository(var falla:Boolean=false):LectorRepository{val datos=mutableListOf<Lector>();override suspend fun registrar(lector:Lector):Lector{if(falla)error("fallo");return lector.copy(id=(datos.size+1).toLong()).also(datos::add)};override suspend fun listar():List<Lector>{if(falla)error("fallo");return datos.toList()}}
