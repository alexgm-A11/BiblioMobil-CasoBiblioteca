# Parte II - Respuestas

## 1. Migración a un backend REST

Cuando llegue el backend cambiarán las implementaciones de `data/repository` por repositorios REST y la selección en `dataModule`; también se agregará el cliente HTTP mediante `platformModule` si requiere configuración específica. Permanecerán intactos los modelos, interfaces y casos de uso de `domain`, los ViewModels y las pantallas de `presentation`, porque datos implementa puertos definidos por dominio y las dependencias apuntan hacia la capa de dominio.

## 2. Entradas numéricas como String

Si `RegistrarLibroUseCase` recibiera `anio` y `ejemplares` como `Int`, perdería la capacidad de distinguir un campo vacío de un texto no numérico y no podría devolver los mensajes exactos para cada error. La pantalla o el ViewModel tendría que convertir y validar antes de invocar el caso de uso, duplicando reglas de negocio en presentación y haciendo que otros clientes pudieran comportarse de forma distinta.

## 3. Repositorio Koin como factory

Con `factory`, cada ViewModel recibiría una instancia distinta del repositorio en memoria: un libro registrado podría no aparecer al volver a la pantalla o en otra instancia, y los datos parecerían desaparecer. Con `single`, libros y lectores comparten la misma fuente de datos durante la ejecución, que es el comportamiento esperado hasta sustituirla por persistencia real.

## Salida de pruebas

```text
> Task :shared:testAndroidHostTest

39 tests completed, 0 failed
BUILD SUCCESSFUL
```

La verificación final también ejecutó correctamente `:androidApp:assembleDebug` y `:shared:compileKotlinIosSimulatorArm64`.
