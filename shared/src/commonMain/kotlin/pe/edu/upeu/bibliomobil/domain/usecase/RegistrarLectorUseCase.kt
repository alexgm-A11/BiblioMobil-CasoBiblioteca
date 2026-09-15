package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

data class ErroresDeLector(val nombre: String? = null, val correo: String? = null, val telefono: String? = null) {
    val hayErrores get() = nombre != null || correo != null || telefono != null
}
class LectorInvalidoException(val errores: ErroresDeLector) : IllegalArgumentException("Lector inválido")

class RegistrarLectorUseCase(private val repository: LectorRepository) {
    private val correoValido = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    suspend operator fun invoke(nombre: String, correo: String, telefono: String): Result<Lector> = resultadoDe {
        val nombreLimpio = nombre.trim()
        val correoLimpio = correo.trim()
        val telefonoLimpio = telefono.trim().ifEmpty { null }
        val errores = ErroresDeLector(
            nombre = if (nombreLimpio.isEmpty()) "El nombre es obligatorio" else null,
            correo = when { correoLimpio.isEmpty() -> "El correo es obligatorio"; !correoValido.matches(correoLimpio) -> "El correo no tiene un formato válido"; else -> null },
            telefono = if (telefonoLimpio != null && !Regex("\\d{6,9}").matches(telefonoLimpio)) "El teléfono debe tener entre 6 y 9 dígitos" else null
        )
        if (errores.hayErrores) throw LectorInvalidoException(errores)
        repository.registrar(Lector(0L, nombreLimpio, correoLimpio, telefonoLimpio))
    }
}
