package pe.edu.upeu.bibliomobil

import kotlin.test.*
import pe.edu.upeu.bibliomobil.domain.model.*

class LibroTest{
 @Test fun rechazaTituloVacio(){assertFails{Libro(1," ","Autor",2000,1)}}
 @Test fun rechazaAutorVacio(){assertFails{Libro(1,"Título"," ",2000,1)}}
 @Test fun rechazaAnioMenor(){assertFails{Libro(1,"T","A",1449,1)}}
 @Test fun rechazaAnioMayor(){assertFails{Libro(1,"T","A",2027,1)}}
 @Test fun reposicionConDos(){assertTrue(Libro(1,"T","A",2000,2).requiereReposicion)}
 @Test fun noReposicionConTres(){assertFalse(Libro(1,"T","A",2000,3).requiereReposicion)}
}
class DetallePrestamoTest{
 private val libro=Libro(1,"T","A",2000,3)
 @Test fun rechazaCeroDias(){assertFails{DetallePrestamo(libro,0)}}
 @Test fun rechazaOchoDias(){assertFails{DetallePrestamo(libro,8)}}
 @Test fun aceptaSieteDias(){assertEquals(7,DetallePrestamo(libro,7).dias)}
 @Test fun multaCuatroDias(){assertEquals(6.0,DetallePrestamo(libro,5).multaPorRetraso(4))}
}
