package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.components.MensajeExito
import pe.edu.upeu.bibliomobil.presentation.components.ValidatedTextField

@Composable fun LibroScreen(viewModel:LibroViewModel,modifier:Modifier=Modifier){val state by viewModel.uiState.collectAsState();LazyColumn(modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){item{Text("Catálogo de libros",style=MaterialTheme.typography.headlineSmall)};item{Card{Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){val f=state.formulario;ValidatedTextField(f.titulo,viewModel::onTituloChange,"Título",f.errorTitulo,Modifier.fillMaxWidth());ValidatedTextField(f.autor,viewModel::onAutorChange,"Autor",f.errorAutor,Modifier.fillMaxWidth());Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){ValidatedTextField(f.anio,viewModel::onAnioChange,"Año",f.errorAnio,Modifier.weight(1f),KeyboardOptions(keyboardType=KeyboardType.Number));ValidatedTextField(f.ejemplares,viewModel::onEjemplaresChange,"Ejemplares",f.errorEjemplares,Modifier.weight(1f),KeyboardOptions(keyboardType=KeyboardType.Number))};Button(viewModel::registrar,Modifier.fillMaxWidth(),enabled=!state.registrando){Text(if(state.registrando)"Registrando…" else "Registrar")}}}};state.mensajeExito?.let{item{MensajeExito(it,Modifier.fillMaxWidth())}};when(val fase=state.fase){FaseLibros.Cargando->item{Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){CircularProgressIndicator(Modifier.size(24.dp));Text("Cargando catálogo…")}};FaseLibros.SinLibros->item{EstadoVacio(Icons.Default.MenuBook,"Sin libros","Aún no hay libros registrados.")};is FaseLibros.Error->item{EstadoVacio(Icons.Default.MenuBook,"Error",fase.mensaje,error=true,accion=viewModel::cargarLibros)};is FaseLibros.ConLibros->{item{Text(if(fase.libros.size==1)"1 libro" else "${fase.libros.size} libros",style=MaterialTheme.typography.titleMedium)};items(fase.libros,key={it.id}){libro->Card(Modifier.fillMaxWidth()){Column(Modifier.padding(14.dp)){Text(libro.titulo,style=MaterialTheme.typography.titleMedium);Text(libro.autor);Text(libro.lineaSecundaria);if(libro.requiereReposicion)AssistChip(onClick={},label={Text("Pocos ejemplares")})}}}}}}}
