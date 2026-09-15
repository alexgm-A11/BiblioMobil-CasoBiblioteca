package pe.edu.upeu.bibliomobil.domain.model

data class Lector(val id: Long, val nombre: String, val correo: String, val telefono: String?) {
    init { require(nombre.isNotBlank()); require(correo.isNotBlank()); require(telefono == null || telefono.isNotBlank()) }
}
