package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.LectorInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase

class LectorViewModel(private val registrarLector:RegistrarLectorUseCase,private val listarLectores:ListarLectoresUseCase):ViewModel(){
 private val _uiState=MutableStateFlow(LectorUiState());val uiState:StateFlow<LectorUiState> = _uiState.asStateFlow();init{cargarLectores()}
 fun cargarLectores()=viewModelScope.launch{_uiState.update{it.copy(fase=FaseLectores.Cargando)};listarLectores().fold({lista->_uiState.update{it.copy(fase=if(lista.isEmpty())FaseLectores.SinLectores else FaseLectores.ConLectores(lista.map{l->l.aUi()}))}},{_uiState.update{it.copy(fase=FaseLectores.Error("No se pudo cargar la cartera de lectores"))}})}
 fun onNombreChange(v:String)=editar{copy(nombre=v,errorNombre=null)};fun onCorreoChange(v:String)=editar{copy(correo=v,errorCorreo=null)};fun onTelefonoChange(v:String)=editar{copy(telefono=v,errorTelefono=null)}
 private fun editar(cambio:FormularioLector.()->FormularioLector){_uiState.update{it.copy(formulario=it.formulario.cambio(),mensajeExito=null)}}
 fun registrar(){if(_uiState.value.registrando)return;val f=_uiState.value.formulario;viewModelScope.launch{_uiState.update{it.copy(registrando=true,mensajeExito=null)};registrarLector(f.nombre,f.correo,f.telefono).fold({lector->_uiState.update{it.copy(formulario=FormularioLector(),registrando=false,mensajeExito="Lector \"${lector.nombre}\" registrado correctamente")};cargarLectores()},{e->if(e is LectorInvalidoException)_uiState.update{it.copy(registrando=false,formulario=it.formulario.copy(errorNombre=e.errores.nombre,errorCorreo=e.errores.correo,errorTelefono=e.errores.telefono))}else _uiState.update{it.copy(registrando=false,fase=FaseLectores.Error("No se pudo cargar la cartera de lectores"))}})}}
}
