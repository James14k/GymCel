# GymCel 💪

Una aplicación Android moderna para gestión de entrenamientos y rutinas de gimnasio, desarrollada con Kotlin y Jetpack Compose.

## 📋 Descripción

GymCel es una aplicación móvil diseñada para ayudar a los usuarios a gestionar sus rutinas de entrenamiento, registrar sesiones de ejercicio (treinos), buscar ejercicios en línea y hacer seguimiento de su progreso físico. La aplicación combina almacenamiento local con acceso a APIs externas para proporcionar una experiencia completa de gestión de entrenamientos.

## ✨ Características Principales

### 🏠 Pantalla de Inicio
- Vista de resumen con estadísticas del usuario
- Visualización de sesiones pendientes
- Acceso rápido a rutinas activas
- Contador de entrenamientos completados

### 📝 Gestión de Rutinas
- Creación y edición de rutinas personalizadas
- Asignación de rutinas a días específicos de la semana
- Organización de ejercicios dentro de cada rutina
- Sistema de control maestro para gestión de entidades relacionadas

### 🏋️ Registro de Entrenamientos (Treinos)
- Inicio de sesiones de entrenamiento basadas en rutinas
- Registro detallado de ejercicios realizados
- Control de series, repeticiones y cargas
- Sistema RIR (Reps In Reserve) para intensidad
- Gestión de tiempos de descanso
- Notas personalizadas por sesión

### 🔍 Búsqueda de Ejercicios
- Integración con ExerciseDB API para búsqueda de ejercicios
- Visualización de detalles de ejercicios con GIFs animados
- Búsqueda por nombre con soporte de paginación
- Selección de ejercicios para agregar a rutinas

### 📊 Log de Entrenamientos
- Historial completo de sesiones realizadas
- Visualización de entrenamientos pasados
- Seguimiento de progreso a lo largo del tiempo

## 🏗️ Arquitectura

El proyecto sigue una arquitectura limpia (Clean Architecture) con separación de capas:

### Capas de la Aplicación

1. **Presentación (`presentacion/`)**
   - UI Components: Componentes reutilizables de Jetpack Compose
   - Screens: Pantallas principales de la aplicación
   - ViewModels: Lógica de presentación y manejo de estado
   - Navigation: Configuración de navegación con Navigation Compose

2. **Dominio (`domain/`)**
   - Model: Modelos de dominio (Rutina, Treino, Ejercicio, etc.)
   - Data: Interfaces de repositorios y servicios
   - UseCase: Casos de uso de la aplicación

3. **Datos (`data/`)**
   - API: Servicios de API con Retrofit
   - Local: Base de datos Room y DAOs
   - Repository: Implementaciones de repositorios
   - Mapper: Conversión entre modelos de dominio y entidades

### Patrones de Diseño Utilizados

- **Repository Pattern**: Abstracción de acceso a datos
- **Factory Pattern**: Creación de instancias de DAOs y ViewModels
- **MVVM**: Model-View-ViewModel para separación de responsabilidades
- **Dependency Injection**: Inyección manual de dependencias

## 🛠️ Tecnologías y Dependencias

### Lenguaje y Framework
- **Kotlin 2.0.21**
- **Android SDK**: Min SDK 26, Target SDK 36, Compile SDK 36
- **Java**: Versión 17

### UI y Compose
- **Jetpack Compose**: Framework de UI declarativa
- **Material Design 3**: Sistema de diseño moderno
- **Navigation Compose**: Navegación entre pantallas
- **Coil**: Carga de imágenes y GIFs

### Persistencia
- **Room Database**: Base de datos local SQLite
- **Room KTX**: Extensiones de Kotlin para Room
- Versión de base de datos: 6

### Networking
- **Retrofit 2.9.0**: Cliente HTTP para APIs REST
- **Gson**: Serialización/deserialización JSON
- **OkHttp**: Cliente HTTP con logging interceptor
- **APIs Integradas**:
  - ExerciseDB API (`https://www.exercisedb.dev/api/v1/`)
  - JSONPlaceholder API (para pruebas)

### Arquitectura y Lifecycle
- **Lifecycle Runtime KTX**: Manejo del ciclo de vida
- **ViewModel Compose**: ViewModels para Compose
- **Coroutines**: Programación asíncrona

### Testing
- **JUnit**: Testing unitario
- **Espresso**: Testing de UI
- **Compose UI Testing**: Testing de componentes Compose
- **Mockito**: Mocking para tests
- **Room Testing**: Utilidades para testing de base de datos

## 📁 Estructura del Proyecto

```
GymCel/
├── app/
│   ├── build.gradle.kts          # Configuración del módulo app
│   ├── proguard-rules.pro        # Reglas de ProGuard
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/cl/duocuc/gymcel/
│       │   │   ├── MainActivity.kt           # Actividad principal
│       │   │   ├── AppConstants.kt           # Constantes y configuración
│       │   │   ├── AppRoutes.kt              # Definición de rutas
│       │   │   ├── core/                     # Núcleo de la aplicación
│       │   │   │   └── navigation/
│       │   │   ├── data/                      # Capa de datos
│       │   │   │   ├── api/                  # Servicios de API
│       │   │   │   │   ├── exercise/
│       │   │   │   │   └── placeholder/
│       │   │   │   ├── local/                # Base de datos local
│       │   │   │   │   ├── dao/              # Data Access Objects
│       │   │   │   │   ├── db/               # Configuración de Room
│       │   │   │   │   ├── entities/         # Entidades de Room
│       │   │   │   │   └── master/           # Control maestro
│       │   │   │   ├── mapper/               # Mappers de datos
│       │   │   │   ├── repository/           # Implementaciones de repositorios
│       │   │   │   ├── service/              # Servicios de datos
│       │   │   │   └── FactoryProvider.kt    # Factory de dependencias
│       │   │   ├── domain/                   # Capa de dominio
│       │   │   │   ├── data/                 # Interfaces de datos
│       │   │   │   ├── model/                # Modelos de dominio
│       │   │   │   └── usecase/              # Casos de uso
│       │   │   └── presentacion/             # Capa de presentación
│       │   │       ├── AppNavGraph.kt        # Grafo de navegación
│       │   │       ├── factory/              # Factories de ViewModels
│       │   │       ├── ui/
│       │   │       │   ├── components/        # Componentes UI reutilizables
│       │   │       │   ├── screens/          # Pantallas principales
│       │   │       │   └── theme/            # Tema de la aplicación
│       │   │       └── viewmodel/            # ViewModels
│       │   └── res/                          # Recursos Android
│       ├── androidTest/                      # Tests instrumentados
│       └── test/                            # Tests unitarios
├── build.gradle.kts                        # Configuración del proyecto raíz
├── gradle/
│   ├── libs.versions.toml                  # Catálogo de versiones
│   └── wrapper/                            # Gradle wrapper
├── gradle.properties
├── settings.gradle.kts
└── README.md
```

## 🚀 Instalación y Configuración

### Requisitos Previos

- **Android Studio**: Hedgehog o superior
- **JDK**: Versión 17 o superior
- **Android SDK**: API Level 26 o superior
- **Gradle**: Versión incluida en el proyecto (Gradle Wrapper)

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/James14k/GymCel.git
   cd GymCel
   ```

2. **Abrir en Android Studio**
   - Abre Android Studio
   - Selecciona "Open an Existing Project"
   - Navega a la carpeta del proyecto y selecciónala

3. **Sincronizar Gradle**
   - Android Studio debería sincronizar automáticamente
   - Si no, ve a `File > Sync Project with Gradle Files`

4. **Configurar el dispositivo**
   - Conecta un dispositivo Android físico o
   - Crea un emulador con API Level 26 o superior

5. **Ejecutar la aplicación**
   - Haz clic en el botón "Run" (▶️) o presiona `Shift + F10`
   - Selecciona el dispositivo objetivo
   - La aplicación se compilará e instalará automáticamente

## 📱 Uso de la Aplicación

### Navegación Principal

La aplicación cuenta con una barra de navegación inferior con tres secciones principales:

1. **Inicio**: Pantalla principal con resumen y estadísticas
2. **Log**: Historial de entrenamientos realizados
3. **Crear Rutina**: Formulario para crear nuevas rutinas

### Flujos Principales

#### Crear una Rutina
1. Navega a "Crear Rutina" desde el menú inferior
2. Completa el formulario con nombre y descripción
3. Selecciona el día de la semana (opcional)
4. Agrega ejercicios buscándolos en la base de datos
5. Guarda la rutina

#### Iniciar un Entrenamiento
1. Desde la pantalla de inicio, selecciona "Continuar Treino" si hay uno pendiente
2. O navega a "Seleccionar Rutina" para comenzar uno nuevo
3. Selecciona la rutina que deseas realizar
4. Registra series, repeticiones y cargas para cada ejercicio
5. Completa el entrenamiento

#### Buscar Ejercicios
1. Desde cualquier pantalla que permita agregar ejercicios
2. Utiliza la búsqueda para encontrar ejercicios en ExerciseDB
3. Visualiza detalles y GIFs animados
4. Selecciona el ejercicio para agregarlo a tu rutina

## 🧪 Testing

El proyecto incluye una suite completa de tests:

### Tests Unitarios
- Ubicación: `app/src/test/`
- Ejemplo: `ExampleUnitTest.kt`

### Tests Instrumentados
- Ubicación: `app/src/androidTest/`
- Incluye tests para:
  - DAOs (RutinaDaoTest, TreinoDaoTest, ItemRutinaDaoTest)
  - Repositorios (RepositoryTest, DaoFactoryTest)
  - Componentes UI (CenteredTextTest, EjercicioListItemTest, etc.)
  - Actividades (MainActivityTest)

### Ejecutar Tests

```bash
# Tests unitarios
./gradlew test

# Tests instrumentados
./gradlew connectedAndroidTest

# Todos los tests
./gradlew test connectedAndroidTest
```

## 📊 Modelos de Datos Principales

### Rutina
- Representa una rutina de entrenamiento
- Contiene nombre, descripción y día asignado
- Relación 1:N con DetalleRutina (ejercicios)

### Treino
- Representa una sesión de entrenamiento realizada
- Vinculado a una Rutina
- Contiene timestamp, estado de completitud y notas
- Relación 1:N con DetalleTreino (ejercicios realizados)

### Ejercicio
- Información de un ejercicio obtenida de ExerciseDB
- Contiene ID, nombre y GIF animado
- Relación 1:1 con DetalleEjercicio

### DetalleTreino
- Registro de un ejercicio específico en un entrenamiento
- Contiene repeticiones, carga, RIR y tiempo de descanso

## 🔧 Configuración Avanzada

### Base de Datos
- **Nombre**: `gymcel.db`
- **Versión actual**: 6
- **Migraciones**: Configuradas con `fallbackToDestructiveMigration()`

### APIs Externas
- **ExerciseDB**: Base URL configurada en `AppConstants.kt`
- **Timeout**: 30 segundos para conexión, lectura y escritura
- **Logging**: Habilitado para debugging (nivel BODY)

### ProGuard
- Archivo de reglas: `app/proguard-rules.pro`
- Minificación deshabilitada en release (configurable)

## 🤝 Contribución

Para contribuir al proyecto:

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

### Estándares de Código
- Seguir las convenciones de Kotlin
- Mantener la arquitectura de capas
- Escribir tests para nuevas funcionalidades
- Documentar funciones complejas

## 📝 Notas de Desarrollo

### Estado del Proyecto
- Versión: 1.0
- Version Code: 1
- Package: `cl.duocuc.gymcel`

### Características Pendientes o Notas
- Hay un comentario FIXME en `AppNavGraph.kt` sobre el manejo de `treinoId` con valor 0L
- El sistema de control maestro está implementado para gestión de entidades relacionadas
- La aplicación utiliza inyección manual de dependencias (considerar Hilt/Dagger en el futuro)

## 👥 Autor

Hawk Durant

## 🔗 Recursos Adicionales

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Retrofit](https://square.github.io/retrofit/)
- [ExerciseDB API](https://www.exercisedb.dev/)
- [Material Design 3](https://m3.material.io/)

---

**GymCel** - Tu compañero de entrenamiento 💪

