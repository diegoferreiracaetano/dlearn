package com.diegoferreiracaetano.dlearn.navigation

/*
class NavigationExtensionsTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun given_nav_entry_with_path_arg_when_access_sduiPath_should_return_correct_value() = runComposeUiTest {
        var capturedPath = ""
        val expectedPath = "home/movies"

        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "start") {
                composable("start") {}
                composable("target/{${AppNavigationRoute.ARG_PATH}}") { backStackEntry ->
                    capturedPath = backStackEntry.sduiPath
                }
            }
            navController.navigate("target/$expectedPath")
        }

        waitForIdle()
        assertEquals(expectedPath, capturedPath)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun given_nav_entry_with_params_arg_when_access_sduiParams_should_return_parsed_map() = runComposeUiTest {
        var capturedParams: Map<String, String>? = null
        val paramsString = "id:123,type:movie"

        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "start") {
                composable("start") {}
                composable("target?${AppNavigationRoute.ARG_PARAMS}={${AppNavigationRoute.ARG_PARAMS}}") { backStackEntry ->
                    capturedParams = backStackEntry.sduiParams
                }
            }
            navController.navigate("target?${AppNavigationRoute.ARG_PARAMS}=$paramsString")
        }

        waitForIdle()
        assertEquals("123", capturedParams?.get("id"))
        assertEquals("movie", capturedParams?.get("type"))
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun given_nav_entry_with_enum_arg_when_readEnumOrDefault_should_return_enum_value() = runComposeUiTest {
        var capturedEnum = TestEnum.VALUE_B
        val expectedEnum = TestEnum.VALUE_A

        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "start") {
                composable("start") {}
                composable("target/{type}") { backStackEntry ->
                    capturedEnum = backStackEntry.readEnumOrDefault("type", TestEnum.VALUE_B)
                }
            }
            navController.navigate("target/${expectedEnum.name}")
        }

        waitForIdle()
        assertEquals(expectedEnum, capturedEnum)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun given_nav_entry_with_string_arg_when_readOrDefault_should_return_value() = runComposeUiTest {
        var capturedValue = ""
        val expectedValue = "test_value"

        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "start") {
                composable("start") {}
                composable("target/{key}") { backStackEntry ->
                    capturedValue = backStackEntry.readOrDefault("key", "")
                }
            }
            navController.navigate("target/$expectedValue")
        }

        waitForIdle()
        assertEquals(expectedValue, capturedValue)
    }

    enum class TestEnum { VALUE_A, VALUE_B }
}


 */
