package pe.edu.upeu.bibliomobil.presentation.inicio

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import pe.edu.upeu.bibliomobil.navigation.Screen

private data class Acceso(val titulo:String,val descripcion:String,val screen:Screen,val icono:ImageVector)
private val ACCESOS=listOf(Acceso("Registrar libros","Administra el catálogo",Screen.Libros,Icons.Default.MenuBook),Acceso("Registrar lectores","Gestiona la cartera",Screen.Lectores,Icons.Default.People),Acceso("Registrar préstamos","Consulta los préstamos",Screen.Prestamos,Icons.Default.Bookmark))
@Composable fun InicioScreen(onNavegar:(Screen)->Unit,modifier:Modifier=Modifier){Column(modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)){Column(Modifier.fillMaxWidth(),horizontalAlignment=Alignment.CenterHorizontally){Icon(Icons.Default.LocalLibrary,null,Modifier.size(72.dp),tint=MaterialTheme.colorScheme.primary);Text("BiblioMobil",style=MaterialTheme.typography.headlineLarge);Text("Tu biblioteca, siempre disponible")};Text("Qué puedes hacer",style=MaterialTheme.typography.titleLarge);ACCESOS.forEach{a->Card(onClick={onNavegar(a.screen)},modifier=Modifier.fillMaxWidth()){Row(Modifier.padding(16.dp),horizontalArrangement=Arrangement.spacedBy(14.dp),verticalAlignment=Alignment.CenterVertically){Icon(a.icono,null);Column{Text(a.titulo,style=MaterialTheme.typography.titleMedium);Text(a.descripcion)}}}}}}
