package pe.edu.upeu.bibliomobil.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable fun ValidatedTextField(value:String,onValueChange:(String)->Unit,label:String,error:String?,modifier:Modifier=Modifier,keyboardOptions:KeyboardOptions=KeyboardOptions.Default){OutlinedTextField(value,onValueChange,modifier,label={Text(label)},isError=error!=null,supportingText=error?.let{{Text(it)}},singleLine=true,keyboardOptions=keyboardOptions)}
@Composable fun MensajeExito(texto:String,modifier:Modifier=Modifier){Card(modifier,colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.tertiaryContainer)){Text(texto,Modifier.padding(12.dp),color=MaterialTheme.colorScheme.onTertiaryContainer)}}
@Composable fun EstadoVacio(icono:ImageVector,titulo:String,descripcion:String,modifier:Modifier=Modifier,error:Boolean=false,accion:(()->Unit)?=null){Column(modifier.padding(24.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(8.dp)){Icon(icono,null,tint=if(error)MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,modifier=Modifier.size(48.dp));Text(titulo,style=MaterialTheme.typography.titleMedium);Text(descripcion,style=MaterialTheme.typography.bodyMedium);if(accion!=null)Button(onClick=accion){Text("Reintentar")}}}
