package pe.edu.upeu.bibliomobil

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import pe.edu.upeu.bibliomobil.navigation.*
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.inicio.InicioScreen
import pe.edu.upeu.bibliomobil.presentation.lector.LectorScreen
import pe.edu.upeu.bibliomobil.presentation.libro.LibroScreen
import pe.edu.upeu.bibliomobil.ui.theme.BiblioMobilTheme

private val ScreenSaver=Saver<Screen,String>(save={it.clave},restore={Screen.desde(it)})
@Composable fun App(){KoinContext{var oscuro by rememberSaveable{mutableStateOf(false)};BiblioMobilTheme(oscuro){val drawer=rememberDrawerState(DrawerValue.Closed);val scope=rememberCoroutineScope();var actual by rememberSaveable(stateSaver=ScreenSaver){mutableStateOf<Screen>(Screen.Inicio)};val navegar:(Screen)->Unit={actual=it;scope.launch{drawer.close()}};ModalNavigationDrawer(drawerState=drawer,drawerContent={ModalDrawerSheet{Column(Modifier.fillMaxHeight()){Row(Modifier.padding(20.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(12.dp)){Icon(Icons.Default.LocalLibrary,null);Text("BiblioMobil",style=MaterialTheme.typography.titleLarge)};HorizontalDivider();DESTINOS.forEach{d->NavigationDrawerItem(label={Text(d.titulo)},selected=actual==d.screen,onClick={navegar(d.screen)},icon={Icon(d.icono,null)},modifier=Modifier.padding(horizontal=12.dp))};Spacer(Modifier.weight(1f));Row(Modifier.fillMaxWidth().padding(20.dp),verticalAlignment=Alignment.CenterVertically){Text("Modo oscuro",Modifier.weight(1f));Switch(oscuro,{oscuro=it})}}}},content={Scaffold(topBar={TopAppBar(title={Text(DESTINOS.first{it.screen==actual}.titulo)},navigationIcon={IconButton({scope.launch{drawer.open()}}){Icon(Icons.Default.Menu,"Menú")}})}){padding->when(actual){Screen.Inicio->InicioScreen(navegar,Modifier.padding(padding));Screen.Libros->LibroScreen(koinViewModel(),Modifier.padding(padding));Screen.Lectores->LectorScreen(koinViewModel(),Modifier.padding(padding));Screen.Prestamos->EstadoVacio(Icons.Default.Bookmark,"Préstamos en construcción","Este módulo estará disponible en una próxima versión.",Modifier.padding(padding).fillMaxSize())}}})}}}
