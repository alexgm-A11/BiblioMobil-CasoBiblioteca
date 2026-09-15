package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.LibroInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase

class LibroViewModel(private val registrarLibro:RegistrarLibroUseCase,private val listarLibros:ListarLibrosUseCase):ViewModel(){
 private val _uiState=MutableStateFlow(LibroUiState()); val uiState:StateFlow<LibroUiState> = _uiState.asStateFlow()
 init{cargarLibros()}
 fun cargarLibros()=viewModelScope.launch{_uiState.update{it.copy(fase=FaseLibros.Cargando)}; listarLibros().fold({lista->_uiState.update{it.copy(fase=if(lista.isEmpty())FaseLibros.SinLibros else FaseLibros.ConLibros(lista.map{l->l.aUi()}))}},{_uiState.update{it.copy(fase=FaseLibros.Error("No se pudo cargar el catálogo"))}})}
 fun onTituloChange(v:String)=editar{copy(titulo=v,errorTitulo=null)}; fun onAutorChange(v:String)=editar{copy(autor=v,errorAutor=null)}; fun onAnioChange(v:String)=editar{copy(anio=v,errorAnio=null)}; fun onEjemplaresChange(v:String)=editar{copy(ejemplares=v,errorEjemplares=null)}
 private fun editar(cambio:FormularioLibro.()->FormularioLibro){_uiState.update{it.copy(formulario=it.formulario.cambio(),mensajeExito=null)}}
 fun registrar(){if(_uiState.value.registrando)return; val f=_uiState.value.formulario; viewModelScope.launch{_uiState.update{it.copy(registrando=true,mensajeExito=null)}; registrarLibro(f.titulo,f.autor,f.anio,f.ejemplares).fold({libro->_uiState.update{it.copy(formulario=FormularioLibro(),registrando=false,mensajeExito="Libro \"${libro.titulo}\" registrado correctamente")};cargarLibros()},{e->if(e is LibroInvalidoException)_uiState.update{it.copy(registrando=false,formulario=it.formulario.copy(errorTitulo=e.errores.titulo,errorAutor=e.errores.autor,errorAnio=e.errores.anio,errorEjemplares=e.errores.ejemplares))}else _uiState.update{it.copy(registrando=false,fase=FaseLibros.Error("No se pudo cargar el catálogo"))}})}}
}
