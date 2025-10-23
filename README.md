# AZ-900 Quiz Application

Una aplicación web interactiva para practicar el examen de certificación AZ-900 de Microsoft Azure, construida con Kotlin Multiplatform y Compose Multiplatform.

## 🚀 Características

- **428 preguntas reales** del examen AZ-900
- **Carga dinámica** desde API externa
- **Interfaz moderna** con Material Design 3
- **Optimizado para WebAssembly** (Wasm)
- **Responsive design** para todos los dispositivos
- **Manejo de errores** robusto
- **Pantallas de carga** y feedback visual

## 📦 Instalación y Desarrollo

### Desarrollo Local

```bash
# Ejecutar en modo desarrollo
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

## 📁 Estructura del Proyecto

```
AZ900-Quiz/
├── composeApp/
│   ├── src/
│   │   ├── webMain/kotlin/
│   │   │   └── dev/donmanuel/az900quiz/
│   │   │       ├── data/           # Modelos de datos
│   │   │       ├── service/        # Servicios (API)
│   │   │       ├── screens/        # Pantallas de la app
│   │   │       ├── composables/    # Componentes reutilizables
│   │   │       └── App.kt          # Punto de entrada
│   │   └── webMain/resources/       # Recursos web
│   └── build.gradle.kts           # Configuración Gradle
├── gradle/
│   └── libs.versions.toml         # Versiones de dependencias
├── vercel.json                    # Configuración Vercel
├── package.json                   # Scripts npm
└── README.md                      # Documentación
```

## 🔧 Configuración

### API Endpoint

La aplicación consume preguntas desde:
```
https://raw.githubusercontent.com/manuelduarte077/AZ900-Quiz/refs/heads/main/api/questions.json
```