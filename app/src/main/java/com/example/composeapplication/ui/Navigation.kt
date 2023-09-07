package com.example.composeapplication.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.composeapplication.R
import com.example.composeapplication.Utils
import com.example.composeapplication.const.Constants
import com.example.composeapplication.viewmodel.ArticleViewModel

@ExperimentalFoundationApi
@Composable
fun Navigation() {
    val articleViewModel: ArticleViewModel = viewModel()
    val baseTitle = stringResource(id = R.string.app_name)
    val (title, setTitle) = remember { mutableStateOf(baseTitle) }
    val (canPop, setCanPop) = remember { mutableStateOf(false) }
    val navController = rememberNavController()
    navController.addOnDestinationChangedListener { controller, _, _ ->
        setCanPop(controller.previousBackStackEntry != null)
    }
    val isCurrentMovieDetail = remember { mutableStateOf(false) }
    val toolBarIcon = remember {
        mutableStateOf(Icons.Default.Search)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title, color = Color.Blue) },
                navigationIcon = { TopBar(canPop, navController, toolBarIcon) },

                )
        },
        bottomBar = {

        }

    ) {
        NavHost(
            navController, modifier = Modifier.padding(it), startDestination = Screen.Find
                .route
        ) {
            composable(Screen.Find.route) {
                FindScreen(navController, setTitle, articleViewModel)
                isCurrentMovieDetail.value = false
            }
            composable(
                route = Constants.ROUTE_DETAIL,
                arguments = listOf(navArgument(Constants.ROUTE_DETAIL_KEY) {
                    type = NavType.StringType
                })
            ) {
//                DetailScreen(
//                    backStackEntry.arguments?.getString(Constants.ROUTE_DETAIL_KEY)!!,
//                    setTitle,
//                    movieViewModel
//                )
                Utils.logDebug(
                    Utils.TAG_LAUNCH,
                    "Navigate to MovieDetail movieDetail:${isCurrentMovieDetail.value}"
                )
                isCurrentMovieDetail.value = true
            }
            composable(Screen.Store.route) {
//                StoreScreen(setTitle)
                isCurrentMovieDetail.value = false
            }
            composable(Screen.Favourite.route) {
//                FavouriteScreen(setTitle)
                isCurrentMovieDetail.value = false
            }
            composable(Screen.Profile.route) {
//                ProfileScreen(setTitle)
                isCurrentMovieDetail.value = false
            }
        }
    }
}

//@Composable
//fun DetailScreen(movieId:String, setTitle: (String) -> Unit, movieViewModel: MovieViewModel){
//    val movies = movieViewModel.movies.observeAsState(emptyList())
//    movies.value.firstOrNull{
//        it.imdbID == movieId
//    }?.let {
//        setTitle(it.Title)
//        LaunchedEffect(it.imdbID){
//            movieViewModel.getMovieComposeCoroutines(it.imdbID)
//        }
//        val moviePro: State<MoviePro> = movieViewModel.moviePro.observeAsState(MoviePro(it.Title))
//        Detail(moviePro.value)
//    }
//}
@ExperimentalFoundationApi
@Composable
fun FindScreen(
    navController: NavHostController,
    setTitle: (String) -> Unit,
    articleViewModel: ArticleViewModel
) {
    setTitle(stringResource(id = R.string.screen_find))
    Find(articleViewModel = articleViewModel) { movie ->
        setTitle("")
        navController.navigate(Constants.ROUTE_DETAIL_PRE + movie.imdbID)
    }
}

@Composable
private fun TopBar(
    canPop: Boolean,
    navController: NavHostController,
    toolBarIcon: MutableState<ImageVector>
) {
    if (canPop) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Outlined.ArrowBack, "back", tint = Color.Blue)
        }
    } else {
        IconButton(onClick = {}) {
            Icon(
                toolBarIcon.value,
                "main",
                tint = Color.Blue
            )
        }
    }
}

sealed class Screen(val route: String, val icon: ImageVector, @StringRes val resourceId: Int) {
    data object Find : Screen(Constants.ROUTE_FIND, Icons.Default.Search, R.string.tab_find)
    data object Store : Screen(Constants.ROUTE_STORE, Icons.Default.Store, R.string.tab_store)
    data object Favourite :
        Screen(Constants.ROUTE_FAVOURITE, Icons.Default.Favorite, R.string.tab_favourite)

    data object Profile :
        Screen(Constants.ROUTE_PROFILE, Icons.Default.AccountCircle, R.string.tab_profile)
}