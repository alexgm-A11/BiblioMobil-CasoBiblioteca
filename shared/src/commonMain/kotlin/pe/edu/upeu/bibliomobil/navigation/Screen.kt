package pe.edu.upeu.bibliomobil.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val clave:String){data object Inicio:Screen("inicio");data object Libros:Screen("libros");data object Lectores:Screen("lectores");data object Prestamos:Screen("prestamos");companion object{fun desde(clave:String)=DESTINOS.firstOrNull{it.screen.clave==clave}?.screen?:Inicio}}
data class Destino(val screen:Screen,val titulo:String,val icono:ImageVector)
val DESTINOS=listOf(Destino(Screen.Inicio,"Inicio",Icons.Default.Home),Destino(Screen.Libros,"Libros",Icons.Default.MenuBook),Destino(Screen.Lectores,"Lectores",Icons.Default.People),Destino(Screen.Prestamos,"Préstamos",Icons.Default.Bookmark))
