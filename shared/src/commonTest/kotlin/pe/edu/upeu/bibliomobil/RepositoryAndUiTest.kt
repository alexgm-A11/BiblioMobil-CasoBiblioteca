package pe.edu.upeu.bibliomobil

import kotlinx.coroutines.test.runTest
import kotlin.test.*
import pe.edu.upeu.bibliomobil.data.repository.*
import pe.edu.upeu.bibliomobil.domain.model.*
import pe.edu.upeu.bibliomobil.presentation.lector.aUi
import pe.edu.upeu.bibliomobil.presentation.libro.aUi

class LibroRepositorioEnMemoriaTest{
 @Test fun idsCorrelativos()=runTest{val r=LibroRepositorioEnMemoria(0);assertEquals(1,r.registrar(Libro(0,"A","B",2000,1)).id);assertEquals(2,r.registrar(Libro(0,"C","D",2001,2)).id)}
 @Test fun listaEnOrden()=runTest{val r=LibroRepositorioEnMemoria(0);r.registrar(Libro(0,"Primero","A",2000,1));r.registrar(Libro(0,"Segundo","B",2001,2));assertEquals(listOf("Primero","Segundo"),r.listar().map{it.titulo})}
}
class PresentacionTest{
 @Test fun lineaPlural(){assertEquals("1998 · 3 ejemplares",Libro(1,"T","A",1998,3).aUi().lineaSecundaria)}
 @Test fun lineaSingular(){assertEquals("1998 · 1 ejemplar",Libro(1,"T","A",1998,1).aUi().lineaSecundaria)}
 @Test fun telefonoAusente(){assertEquals("No registrado",Lector(1,"Ana","a@b.pe",null).aUi().telefono)}
}
