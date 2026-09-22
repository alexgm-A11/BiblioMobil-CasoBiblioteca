# BiblioMobil

Aplicación Kotlin Multiplatform independiente para administrar el catálogo y los lectores de una biblioteca universitaria.

El módulo de préstamos permite seleccionar un libro y un lector registrados, fijar un plazo de 1 a 7 días y consultar debajo los préstamos creados. No permite prestar más ejemplares simultáneos que los registrados en el libro. Los datos se mantienen en memoria durante la ejecución de la aplicación.

## Arquitectura

- Clean Architecture: `domain`, `data`, `presentation`.
- MVVM con `StateFlow`.
- Inyección de dependencias con Koin.
- UI compartida con Compose Multiplatform para Android e iOS.

## Verificación

```powershell
.\gradlew.bat :shared:testAndroidHostTest :androidApp:assembleDebug :shared:compileKotlinIosSimulatorArm64
```

Resultado verificado: 39 pruebas, 0 fallos y compilación satisfactoria para Android e iOS Simulator ARM64.
