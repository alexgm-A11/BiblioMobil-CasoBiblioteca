# BiblioMobil

Aplicación Kotlin Multiplatform independiente para administrar el catálogo y los lectores de una biblioteca universitaria.

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
