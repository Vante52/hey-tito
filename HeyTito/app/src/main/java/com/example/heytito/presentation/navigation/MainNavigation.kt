package com.example.heytito.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.heytito.presentation.ui.components.BottomNavItem
import com.example.heytito.presentation.ui.components.BottomNavigationBar
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.heytito.presentation.ui.screens.vendedor.CreateProductScreen
import com.example.heytito.presentation.ui.screens.vendedor.VendedorDashboardScreen
import com.example.heytito.presentation.ui.screens.auth.LoginScreen
import com.example.heytito.presentation.ui.screens.auth.RegisterScreen
import com.example.heytito.presentation.ui.screens.auth.WelcomeScreen
import com.example.heytito.presentation.ui.screens.cliente.CartScreen
import com.example.heytito.presentation.ui.screens.cliente.ClienteDashboardScreen
import com.example.heytito.presentation.ui.screens.cliente.FavoritesScreen
import com.example.heytito.presentation.ui.screens.cliente.PreferencesFlowScreen
import com.example.heytito.presentation.ui.screens.cliente.ProfileScreen
import com.example.heytito.presentation.ui.screens.common.ChatListScreen
import com.example.heytito.presentation.ui.screens.common.ChatScreen
import com.example.heytito.presentation.ui.screens.common.DeliveryPickupScreen
import com.example.heytito.presentation.ui.screens.common.NotificationsScreen
import com.example.heytito.presentation.ui.screens.common.OrdersScreen
import com.example.heytito.presentation.ui.screens.common.ProductDetailScreen
import com.example.heytito.presentation.ui.screens.common.ProductDetailScreenCamisa
import com.example.heytito.presentation.ui.screens.common.SearchScreen
import com.example.heytito.presentation.ui.screens.common.StoreProfileScreen
import com.example.heytito.presentation.ui.screens.common.TitoChatScreen
import com.example.heytito.presentation.ui.screens.vendedor.MyProductsScreen
import com.example.heytito.presentation.ui.screens.vendedor.StatisticsScreen


@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Rutas donde se muestra la bottom bar
    val bottomBarRoutes = remember {
        setOf(
            AppScreens.Cart.route,
            AppScreens.Notifications.route,
            AppScreens.Profile.route,
            AppScreens.DeliveryPickup.route,
            AppScreens.Favorites.route
        )
    }
    val homeRouteForBar = when {
        currentRoute?.startsWith("home/Vendedor") == true -> AppScreens.Home.withRole("Vendedor")
        else -> AppScreens.Home.withRole("Cliente")
    }

    val bottomNavItems = remember {
        listOf(
            BottomNavItem(
                route = homeRouteForBar,                 // ← Home según pantalla actual
                icon = Icons.Default.Home,
                label = "Home"
            ),
            BottomNavItem(
                route = AppScreens.Cart.route,
                icon = Icons.Default.ShoppingCart,
                label = "Cart"
            ),
            BottomNavItem(
                route = AppScreens.ChatList.route,
                icon = Icons.Default.Email,
                label = "Chat_list"
            ),
            BottomNavItem(
                route = AppScreens.Notifications.route,
                icon = Icons.Default.Notifications,
                label = "Notifications",
                badgeCount = 5
            ),
            BottomNavItem(
                route = AppScreens.Profile.route,
                icon = Icons.Default.Person,
                label = "Profile",
                isProfile = true
            )
        )
    }

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes||
                currentRoute?.startsWith("home/") == true) {
                BottomNavigationBar(
                    items = bottomNavItems,
                    currentRoute = currentRoute ?: homeRouteForBar,
                    onItemClick = { route ->
                        if (currentRoute != route) {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    profileImageUrl = null // evita errores si aún no tienes foto
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AppScreens.Welcome.route, // <- inicia en Welcome, como pediste
            modifier = Modifier.padding(paddingValues)
        ) {
            // Onboarding
            composable(AppScreens.Welcome.route) {
                WelcomeScreen(
                    onCreateAccount = { navController.navigate(AppScreens.Register.route) },
                    onContinueWithGoogle = { navController.navigate(AppScreens.Login.route) },
                    onContinueWithApple = { navController.navigate((AppScreens.Login.route)) }
                    /*onLogin = {  si existe login: navController.navigate(AppScreens.Home)  }*/
                )
            }

            composable (AppScreens.Login.route) {
                LoginScreen(
                    onLoginClick = { navController.navigate(AppScreens.Home.withRole("Cliente")) }, //TODO: Logica para manejar el rol del usuario cuando ya hayan modelos
                    onBackClick = { navController.popBackStack() },
                    onForgotPasswordClick = {}
                )
            }
            composable(AppScreens.Register.route) {
                var role by remember { mutableStateOf("Cliente") } // por defecto
                RegisterScreen(
                    onRoleClick = { role = it },
                    onRegisterClick = { navController.navigate(AppScreens.Home.withRole(role)) },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(AppScreens.Preferences.route) {
                PreferencesFlowScreen(
                    onBackToRegister = { navController.popBackStack() },
                    onFinishClick = { selections ->
                        // Aquí ya tienes un Map<PreferenceType, Set<String>>
                        // con lo que eligió el usuario en cada categoría
                        // TODO: Guardar en ViewModel / backend
                        navController.navigate(AppScreens.Home.route) {
                            popUpTo(AppScreens.Welcome.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }


            // Cliente (Home) + principales
            composable(
                route = AppScreens.Home.route,
                arguments = listOf(navArgument("role") { type = NavType.StringType })
            ) { backStackEntry ->
                val role = backStackEntry.arguments?.getString("role") ?: "Cliente"
                if (role.equals("Vendedor", ignoreCase = true)) {
                    VendedorDashboardScreen(
                        onEnviosClick = {},
                        onComentariosClick = {},
                        onEstadisticasClick = {navController.navigate(AppScreens.Estadisticas.route)},
                        onMisPedidosClick = {},
                        onAgregarProductoClick = {navController.navigate(AppScreens.CreateProduct.route)},
                        onMostrarProductosClick = {navController.navigate(AppScreens.MyProducts.route)}
                    )
                } else {
                    ClienteDashboardScreen(
                        onBackClick = { navController.popBackStack() },
                        onFollowClick = { },
                        onProductClick = { navController.navigate(AppScreens.ProductDetail.route) },
                        onStoreClick = { navController.navigate(AppScreens.StoreProfile.route) },
                        onFilterClick = { navController.navigate(AppScreens.Search.route) },
                        onOpenComments = { /* ... */ },
                        onAddToCart = { /* ... */ }
                    )
                }
            }

            composable(AppScreens.Search.route) {
                SearchScreen(
                    onBackClick = { navController.popBackStack() },
                    onSearchClick = { /* productId ->
                        navControllimport com.example.compose.heytitoTheme
er.navigate("${AppScreens.ProductDetail}/$productId")
                       */ navController.navigate(AppScreens.Home.route)
                    }
                )
            }
            composable(AppScreens.ProductDetail.route) {
                ProductDetailScreen(
                    onBuyClick = { navController.navigate(AppScreens.Cart.route) },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(AppScreens.Cart.route) {
                CartScreen(
                    onCheckoutClick = { navController.navigate(AppScreens.DeliveryPickup.route) },
                    onBackClick = { navController.popBackStack() }
                )
            }


            composable(AppScreens.Orders.route) {
                OrdersScreen(
                    //onOrderClick = { navController.navigate(AppScreens..route) },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(AppScreens.DeliveryPickup.route) {
                DeliveryPickupScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Tabs de la bottom bar
            composable(AppScreens.Chat.route) {
                ChatScreen(
                    onBackClick = { navController.popBackStack() })
            }
            // MainNavigation.kt (dentro del NavHost)
            composable(AppScreens.ChatList.route) {
                ChatListScreen(
                    onOpenChat = { chatId, isTito ->
                        if (isTito) {
                            navController.navigate(AppScreens.TitoChat.route)
                        } else {
                            navController.navigate("chat/$chatId")
                        }
                    },
                    onBackClick = { navController.popBackStack() },
                    onNewChat = { /* ... */ }
                )
            }

            composable(AppScreens.Chat.route) {
                ChatScreen(
                    onBackClick = { navController.popBackStack() },
                    onMoreClick = {},
                    onCallClick = {}
                )
            }
            composable(AppScreens.TitoChat.route) {
                TitoChatScreen(
                    onBackClick = { navController.popBackStack() },
                    onViewProduct = {
                        navController.navigate(AppScreens.ProductDetailCamisa.route)
                    },
                    onViewProfile = {
                        // Si quieres que "Ver perfil" también navegue:
                        navController.navigate(AppScreens.StoreProfile.route)
                    }
                )
            }
            composable (AppScreens.ProductDetailCamisa.route){
                ProductDetailScreenCamisa (
                    onBuyClick = { navController.navigate(AppScreens.Cart.route) },
                    onBackClick = { navController.popBackStack() }
                ){  }
            }


            composable (AppScreens.Favorites.route ){
                FavoritesScreen(
                    onBack = { navController.popBackStack() },
                    onCartClick = {},
                    onAddCategory = {},
                    onOpenProduct = {}
                )
            }


            composable(AppScreens.Notifications.route) {
                NotificationsScreen(
                    onNotificationClick = {},
                    onMarkAllRead = {}
                )
            }

            composable(AppScreens.Profile.route) {
                ProfileScreen(
                    onBackClick = { navController.popBackStack() },
                    onSavedClick = { navController.navigate(AppScreens.Favorites.route) },
                    onLogoutClick = { navController.navigate(AppScreens.Welcome.route) }
                )
            }

            composable(AppScreens.StoreProfile.route) {
                StoreProfileScreen(
                    onBackClick = { navController.popBackStack() },
                    onFollowClick = {},
                    onProductClick = {}
                )
            }
            composable (AppScreens.Estadisticas.route ){
                StatisticsScreen(
                    onBackClick = {navController.popBackStack()}
                )
            }
            composable(AppScreens.CreateProduct.route){
                CreateProductScreen (
                    onCloseClick = {navController.popBackStack()},
                    onPublishClick = {},
                    onSaveDraftClick = {}
                )
            }
            composable (AppScreens.MyProducts.route){
                MyProductsScreen(
                    onBackClick = { navController.popBackStack() },
                    onAddProductClick = { navController.navigate(AppScreens.CreateProduct.route) }
                    //onProductActionClick = {}
                )
            }
        }
    }
}

