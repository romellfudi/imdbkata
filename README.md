# IMDB KATA Project

<p align="center"> 
<a href="https://github.com/romellfudi/imdbkata/actions"><img src="https://github.com/romellfudi/imdbkata/workflows/Android%20CI/badge.svg" alt="Continuous Integration"></a>
</p>

### by Romell Freddy Dominguez

[![](https://raw.githubusercontent.com/romellfudi/assets/master/favicon.ico)](https://portfolio.romellfudi.com/)

## Demo

<p align="center"> 
<a href="https://raw.githubusercontent.com/romellfudi/imdbkata/main/snapshots/workflow.gif"><img src="https://github.com/romellfudi/imdbkata/blob/main/snapshots/workflow.gif" alt="Workflow" width="150"></a>
</p>

## CheckList Challenges

- [x] La aplicación debe tener un splash con animación que puede durar 1.5 segundos antes de entrar
  a la siguiente pantalla, este espacio puede ser usado para consultar si el usuario está logueado,
  según esto, decidir si pasar directamente a la pantalla de búsqueda.
- [x] La pantalla de login debe permitir loguear al usuario, persistiendo localmente una vez el
  usuario se ha registrado.
- [x] La pantalla de login debe permitir el registro de la aplicación usando Firebase para guardar
  el usuario y usar registro con redes sociales.
- [x] La pantalla de crear cuenta debe crear un usuario que se persiste de manera local y debe
  implementar restricciones o validaciones para un password (min 8 caracteres, mayúscula, minúscula,
  carácter especial y número) así como no permitir correos duplicados.
- [x] La pantalla de búsqueda debe listar las películas obtenidas a través del API sugerida y debe
  poder filtrar por nombre usando la barra de búsqueda. Adicionalmente, se debe guardar en la base
  de datos el último resultado del API local para ser mostrado en la pantalla de búsqueda en la
  siguiente vez que el usuario abre la aplicación.
- [x] Presentar el diagrama de la arquitectura para la demo.
- [x] Validaciones adicionales al flujo pueden ser asumidas por cada desarrollador.

## Additional CheckList

- [x] Creación de casos de [prueba unitarios](home/src/test/java/com/example/home).
- [x] Configurar el Continuous Integration en GITHUB para probar el correcto functionamiento del
  proyecto.
- [x] Verificar que los artefactos(reporte de casos de prueba y la construcción del APK) se
  construyeron correctamente en el workflow del CI.
- [x] Validación del campo nombre de usuario en la pantalla de registro.
- [x] Camino Guest para el usuario que no se ha registrado.
- [x] Configuración de librerías para las pruebas instrumentales con Kaspresso.
- [x] Creación de casos de [pruebas instrumentales](app/src/androidTest/java/com/example/imdbkata/KataInstrumentalTest.kt).

## Software Android Clean Architecture

<p align="center">
<a href="https://raw.githubusercontent.com/romellfudi/imdbkata/main/snapshots/IMDB_KATA_CLEANARQUITECTURE.jpg"><img src="https://github.com/romellfudi/imdbkata/blob/main/snapshots/IMDB_KATA_CLEANARQUITECTURE.jpg" alt="Workflow" width="800"></a>
</p>

## RoomDatabase - Unit Test Cases
```kotlin
class GenreDaoTest {
  fun `test replaceTable and deleteAll functions`() = ...
}
class MovieDaoTest {
  fun `test insert and get movie`() = ...
  fun `test replaceTable and deleteAll functions`() = ...
}
```

## ApiRest - Unit Test Cases
```kotlin
class ApiTest {
  fun `test getting movies from api`() = ...
  fun `test getting error from the api`() = ...
  fun `test get loading state from the api`() = ...
  fun `test the a list of movies from the api`() = ...
}
```

## ViewModels & UseCases - Unit Test Cases
```kotlin
class SplashViewModelTest {
  fun `test user is not logged`() = ...
  fun `test user is logged`() = ...
}
class RegisterViewModelTest {
  fun `test no register an invalid data`() = ...
  fun `test register an valid data`() = ...
}
class LoginViewModelTest {
  fun `test Sign in as Anonymous`() = ...
  fun `test Sign in as Email and Password`() = ...
  fun `test Sign in as Facebook`() = ...
  fun `test Sign in as Google user`() = ...
}
class HomeViewModelTest {
  fun `test fetch Movie data`() = ...
  fun `test fetch Genre data`() = ...
}
```

## Instrumental Test with Kaspresso
```kotlin
    @Test
    fun goodWorkflow() = run {
        step("Open Splash screen") { ... }
        step("Register new user") { ... }
        step("Open Login screen & fill fields to sign in") { ... }
        step("Open Home screen") { ... }
    }

    @Test
    fun wrongEmailWorkflow() = run {
        step("Trying to register new user with wrong email format") { ... }
    }

    @Test
    fun wrongPasswordWorkflow() = run {
        step("Trying to register new user with wrong password format") { ... }
    }

    @Test
    fun goodWorkflowAsGuest() = run {
        step("Open Splash screen") { ... }
        step("Go to Home screen as guest user") { ... }
        step("Open Home screen") { ... }
    }
```

## License
[![GNU GPLv3 Image](https://www.gnu.org/graphics/gplv3-127x51.png)](http://www.gnu.org/licenses/gpl-3.0.en.html)

IMDB KATA is a Free Software: You can use, study share and improve it at your
will. Specifically you can redistribute and/or modify it under the terms of the
[GNU General Public License](https://www.gnu.org/licenses/gpl.html) as
published by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.  