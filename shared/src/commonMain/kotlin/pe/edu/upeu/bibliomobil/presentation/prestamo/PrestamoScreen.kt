package pe.edu.upeu.bibliomobil.presentation.prestamo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.components.MensajeExito
import pe.edu.upeu.bibliomobil.presentation.components.ValidatedTextField

@Composable
fun PrestamoScreen(viewModel: PrestamoViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(viewModel) { viewModel.cargarPrestamos() }
    LazyColumn(modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text("Registro de préstamos", style = MaterialTheme.typography.headlineSmall) }
        item {
            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val formulario = state.formulario
                    SelectorPrestamo(
                        etiqueta = "Libro",
                        seleccionado = state.libros.firstOrNull { it.id == formulario.libroId }?.titulo ?: "Seleccione un libro",
                        opciones = state.libros.map { it.id to "${it.titulo} · ${it.ejemplares} ejemplares" },
                        error = formulario.errorLibro,
                        onSeleccionar = viewModel::onLibroChange
                    )
                    SelectorPrestamo(
                        etiqueta = "Lector",
                        seleccionado = state.lectores.firstOrNull { it.id == formulario.lectorId }?.nombre ?: "Seleccione un lector",
                        opciones = state.lectores.map { it.id to it.nombre },
                        error = formulario.errorLector,
                        onSeleccionar = viewModel::onLectorChange
                    )
                    ValidatedTextField(
                        formulario.dias, viewModel::onDiasChange, "Días para devolver (máximo 7)",
                        formulario.errorDias, Modifier.fillMaxWidth(),
                        KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Button(viewModel::registrar, Modifier.fillMaxWidth(), enabled = !state.registrando) {
                        Text(if (state.registrando) "Registrando…" else "Registrar")
                    }
                }
            }
        }
        state.mensajeExito?.let { item { MensajeExito(it, Modifier.fillMaxWidth()) } }
        state.mensajeError?.let { item { Text(it, color = MaterialTheme.colorScheme.error) } }
        item { Text("Listado de préstamos", style = MaterialTheme.typography.titleLarge) }
        when (val fase = state.fase) {
            FasePrestamos.Cargando -> item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CircularProgressIndicator(Modifier.size(24.dp))
                    Text("Cargando préstamos…")
                }
            }
            FasePrestamos.SinPrestamos -> item {
                EstadoVacio(Icons.Default.Bookmark, "Sin préstamos", "Aún no hay préstamos registrados.")
            }
            is FasePrestamos.Error -> item {
                EstadoVacio(Icons.Default.Bookmark, "Error", fase.mensaje, error = true, accion = viewModel::cargarPrestamos)
            }
            is FasePrestamos.ConPrestamos -> {
                item {
                    Text(if (fase.prestamos.size == 1) "1 préstamo" else "${fase.prestamos.size} préstamos", style = MaterialTheme.typography.titleMedium)
                }
                items(fase.prestamos, key = { it.id }) { prestamo ->
                    val detalle = prestamo.detalles.single()
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(detalle.libro.titulo, style = MaterialTheme.typography.titleMedium)
                            Text("Lector: ${prestamo.lector.nombre}")
                            Text("Plazo: ${detalle.dias} ${if (detalle.dias == 1) "día" else "días"}")
                            Text("Estado: Entregado")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectorPrestamo(
    etiqueta: String,
    seleccionado: String,
    opciones: List<Pair<Long, String>>,
    error: String?,
    onSeleccionar: (Long) -> Unit
) {
    var abierto by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth()) {
        Text(etiqueta, style = MaterialTheme.typography.labelLarge)
        OutlinedButton(onClick = { abierto = true }, modifier = Modifier.fillMaxWidth(), enabled = opciones.isNotEmpty()) {
            Text(seleccionado)
        }
        DropdownMenu(expanded = abierto, onDismissRequest = { abierto = false }) {
            opciones.forEach { (id, texto) ->
                DropdownMenuItem(text = { Text(texto) }, onClick = { onSeleccionar(id); abierto = false })
            }
        }
        error?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
        if (opciones.isEmpty()) Text("Registre un ${etiqueta.lowercase()} primero", style = MaterialTheme.typography.bodySmall)
    }
}
