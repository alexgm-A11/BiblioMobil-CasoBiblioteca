package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import pe.edu.upeu.bibliomobil.presentation.components.*

@Composable fun LectorScreen(viewModel:LectorViewModel,modifier:Modifier=Modifier){val state by viewModel.uiState.collectAsState();LazyColumn(modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){item{Text("Cartera de lectores",style=MaterialTheme.typography.headlineSmall)};item{Card{Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){val f=state.formulario;ValidatedTextField(f.nombre,viewModel::onNombreChange,"Nombre",f.errorNombre,Modifier.fillMaxWidth());ValidatedTextField(f.correo,viewModel::onCorreoChange,"Correo",f.errorCorreo,Modifier.fillMaxWidth(),KeyboardOptions(keyboardType=KeyboardType.Email));ValidatedTextField(f.telefono,viewModel::onTelefonoChange,"Teléfono (opcional)",f.errorTelefono,Modifier.fillMaxWidth(),KeyboardOptions(keyboardType=KeyboardType.Phone));Button(viewModel::registrar,Modifier.fillMaxWidth(),enabled=!state.registrando){Text(if(state.registrando)"Registrando…" else "Registrar")}}}};state.mensajeExito?.let{item{MensajeExito(it,Modifier.fillMaxWidth())}};when(val fase=state.fase){FaseLectores.Cargando->item{Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){CircularProgressIndicator(Modifier.size(24.dp));Text("Cargando lectores…")}};FaseLectores.SinLectores->item{EstadoVacio(Icons.Default.People,"Sin lectores","Aún no hay lectores registrados.")};is FaseLectores.Error->item{EstadoVacio(Icons.Default.People,"Error",fase.mensaje,error=true,accion=viewModel::cargarLectores)};is FaseLectores.ConLectores->{item{Text(if(fase.lectores.size==1)"1 lector" else "${fase.lectores.size} lectores",style=MaterialTheme.typography.titleMedium)};items(fase.lectores,key={it.id}){lector->Card(Modifier.fillMaxWidth()){Column(Modifier.padding(14.dp)){Text(lector.nombre,style=MaterialTheme.typography.titleMedium);Text(lector.correo);Text(lector.telefono)}}}}}}}
