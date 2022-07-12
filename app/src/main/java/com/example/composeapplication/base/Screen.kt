//package com.example.composeapplication.base
//
//import androidx.compose.animation.ExperimentalAnimationApi
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.navigation.*
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.navigation
//import com.example.composeapplication.extend.ProvideNavHostController
//import com.example.composeapplication.ui.screen.ArticleScreen
//import com.google.accompanist.navigation.animation.AnimatedNavHost
//
//abstract class Screen(private val path: String) {
//    // 跟路由
//    abstract val root: String
//
//    open val arguments: List<NamedNavArgument> = emptyList()
//    open val deepLinks: List<NavDeepLink> = emptyList()
//
//    //必填参数
//    private val requiredArgs: MutableList<String> = mutableListOf()
//
//    //可选参数名+默认值 map
//    private val optionalArgs: MutableMap<String, Any?> = mutableMapOf()
//
//    private val routePath by lazy {
//        if (root.isEmpty()) path else "$root/${path}"
//    }
//
//    /**
//     * 自动生成的 route ，配置 DSL 时使用
//     * route = rootRoute/path/必填参数?可选参数
//     */
//    val route: String by lazy {
//        val sb = StringBuilder(routePath)
//        //解析参数时将 必选/可选参数 分别保存起来
//        for (argument in arguments) {
//            if (argument.argument.isNullable || argument.argument.isDefaultValuePresent) {
//                // 可选参数
//                sb.append("?${argument.name}={${argument.name}}")
//                optionalArgs[argument.name] = argument.argument.defaultValue
//            } else {
//                sb.append("/{${argument.name}}")
//            }
//        }
//        sb.toString()
//    }
//
//    /*** 通用方法
//     * 根据传入的 map 生成 navigate 方法所需 route
//     * map 中必须包括所有的必填参数
//     * @param args Map<String, Any> key:参数名,value:参数值
//     * @return String 调用 navigate 方法所需 route
//     */
//    fun creteRoute(args: Map<String, Any> = emptyMap()): String {
//        val sb = StringBuilder(routePath)
//        if (args.isEmpty() && requiredArgs.isNotEmpty()) {
//            throw  IllegalArgumentException("param [args:Map<String,Any>] can't be empty")
//        }
//        for (requiredArg in requiredArgs) {
//            if (!args.containsKey(requiredArg)) {
//                throw  IllegalArgumentException("required argument $requiredArg can't find in param [args:Map<String,Any>]")
//            }
//            sb.append("/${args[requiredArg]}")
//        }
//        for (optionalArg in optionalArgs) {
//            //参数中有可选参数
//            if (args.containsKey(optionalArg.key)) {
//                sb.append("?${optionalArg.key}={${args[optionalArg.key]}}")
//            } else if (optionalArg.value != null) { // 可选参数有默认值
//                sb.append("?${optionalArg.key}={${optionalArg.value}}")
//            }
//        }
//        return sb.toString()
//    }
//}
//
//fun NavGraphBuilder.composableScreen(
//    screen: Screen,
//    content: @Composable (NavBackStackEntry) -> Unit
//) {
//    composable(
//        route = screen.route,
//        arguments = screen.arguments,
//        deepLinks = screen.deepLinks,
//        content = content
//    )
//}
//
//
//abstract class ScreenNavGraph(
//    protected val navController: NavController,
//    private val startScreen: Screen
//) {
//
//    protected abstract val composeScreens: NavGraphBuilder.() -> Unit
//
//    fun create(builder: NavGraphBuilder) {
//        builder.run {
//            navigation(startDestination = startScreen.route, route = startScreen.root) {
//                composeScreens.invoke(this)
//            }
//        }
//    }
//}
//
//private class ArticleGraph(navController: NavHostController) :
//    ArticleScreens.NavGraph(navController)
//
//sealed class ArticleScreens(path: String) : Screen(path) {
//    override val root: String
//        get() = "home"
//
//    object ArticleScreen : ArticleScreens("article")
//
//    //abstract 是为涉及跨模块路由时定义抽象方法或属性
//    abstract class NavGraph(navController: NavHostController) :
//        ScreenNavGraph(navController, ArticleScreen) {
//
//        override val composeScreens: NavGraphBuilder.() -> Unit = {
//            composableScreen(ArticleScreen) {
//                ArticleScreen(navController)
//            }
//        }
//    }
//}
//
//
//
//@OptIn(ExperimentalAnimationApi::class)
//@Composable
//fun AppNavHost(modifier: Modifier, navController: NavHostController) {
//    ProvideNavHostController(navHostController = navController) {
//        AnimatedNavHost(
//            modifier = modifier,
//            navController = navController,
//            startDestination = ArticleScreens.ArticleScreen.root
//        ) {
//            ArticleGraph(navController).create(this)
//        }
//    }
//}
