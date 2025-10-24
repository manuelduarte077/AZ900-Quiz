# AZ-900 Quiz Application

Una aplicación web interactiva para practicar el examen de certificación AZ-900 de Microsoft Azure, construida con Kotlin Multiplatform y Compose Multiplatform.

## 📦 Instalación y Desarrollo

### Desarrollo Local

```bash
# Ejecutar en modo desarrollo
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

### Deploy a GitHub Pages

El proyecto está configurado para deploy automático a GitHub Pages usando GitHub Actions.

#### Configuración Inicial

1. **Habilitar GitHub Pages** en tu repositorio:
   - Ve a Settings → Pages
   - Selecciona "GitHub Actions" como fuente

2. **El workflow se ejecutará automáticamente** cuando hagas push a la rama `main`

#### Deploy Manual

```bash
# Build local para GitHub Pages (opcional, para testing)
./gradlew :composeApp:githubPagesBuild
```

#### URL de Deploy

- **GitHub Pages**: `https://tu-usuario.github.io/AZ900-Quiz/`

## 📁 Estructura del Proyecto

```
AZ900-Quiz/
├── .github/
│   └── workflows/
│       └── deploy.yml             # GitHub Actions para deploy
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
├── .nojekyll                      # Configuración GitHub Pages
├── package.json                   # Scripts npm
└── README.md                      # Documentación
```

## 🔧 Configuración

### API Endpoint

La aplicación consume preguntas desde:
```
https://raw.githubusercontent.com/manuelduarte077/AZ900-Quiz/refs/heads/main/api/questions.json
```