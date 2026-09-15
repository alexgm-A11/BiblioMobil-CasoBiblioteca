package pe.edu.upeu.bibliomobil

import kotlinx.coroutines.test.runTest
import kotlin.test.*
import pe.edu.upeu.bibliomobil.domain.usecase.*

class RegistrarLibroUseCaseTest{
 private fun caso(repo:FakeLibroRepository=FakeLibroRepository())=RegistrarLibroUseCase(repo)
 private suspend fun error(t:String="T",a:String="A",anio:String="2000",e:String="1")=(caso()(t,a,anio,e).exceptionOrNull() as LibroInvalidoException).errores
 @Test fun aceptaLibroValido()=runTest{assertTrue(caso()(" T "," A ","1998","3").isSuccess)}
 @Test fun tituloObligatorio()=runTest{assertEquals("El título es obligatorio",error(t="").titulo)}
 @Test fun autorObligatorio()=runTest{assertEquals("El autor es obligatorio",error(a=" ").autor)}
 @Test fun anioObligatorio()=runTest{assertEquals("El año es obligatorio",error(anio="").anio)}
 @Test fun anioEntero()=runTest{assertEquals("El año debe ser un número entero",error(anio="dos mil").anio)}
 @Test fun anioRango()=runTest{assertEquals("El año debe estar entre 1450 y 2026",error(anio="1400").anio)}
 @Test fun ejemplaresObligatorios()=runTest{assertEquals("Los ejemplares son obligatorios",error(e="").ejemplares)}
 @Test fun ejemplaresEnteros()=runTest{assertEquals("Los ejemplares deben ser un número entero",error(e="1.5").ejemplares)}
 @Test fun ejemplaresNoNegativos()=runTest{assertEquals("Los ejemplares no pueden ser negativos",error(e="-1").ejemplares)}
 @Test fun repositorioAsignaId()=runTest{assertEquals(1,caso()("T","A","2000","1").getOrThrow().id)}
 @Test fun propagaFallo()=runTest{assertTrue(caso(FakeLibroRepository(true))("T","A","2000","1").isFailure)}
}
class RegistrarLectorUseCaseTest{
 private fun caso(repo:FakeLectorRepository=FakeLectorRepository())=RegistrarLectorUseCase(repo)
 @Test fun aceptaLector()=runTest{assertTrue(caso()("Ana","ana@upeu.edu.pe","").isSuccess)}
 @Test fun nombreObligatorio()=runTest{val e=caso()("","a@b.pe","").exceptionOrNull() as LectorInvalidoException;assertEquals("El nombre es obligatorio",e.errores.nombre)}
 @Test fun correoInvalido()=runTest{val e=caso()("Ana","correo","").exceptionOrNull() as LectorInvalidoException;assertEquals("El correo no tiene un formato válido",e.errores.correo)}
 @Test fun telefonoCorto()=runTest{val e=caso()("Ana","a@b.pe","123").exceptionOrNull() as LectorInvalidoException;assertEquals("El teléfono debe tener entre 6 y 9 dígitos",e.errores.telefono)}
 @Test fun telefonoBlancoEsNull()=runTest{assertNull(caso()("Ana","a@b.pe","  ").getOrThrow().telefono)}
}
