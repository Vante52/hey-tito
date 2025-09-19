package com.example.heytito.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.heytito.presentation.ui.components.BottomNavItem
import com.example.heytito.presentation.ui.components.BottomNavigationBar
import com.example.heytito.presentation.ui.screens.*

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Rutas donde se muestra la bottom bar
    val bottomBarRoutes = remember {
        setOf(
            AppScreens.Search.route,
            AppScreens.Chat.route,
            AppScreens.Notifications.route,
            AppScreens.Profile.route,
            AppScreens.SellerDashboard.route,
            AppScreens.StoreProfile.route,
            AppScreens.Statistics.route
        )
    }

    val bottomNavItems = remember {
        listOf(
            BottomNavItem(
                route = AppScreens.Home.route,
                icon = Icons.Default.Home,
                label = "Home"
            ),
            BottomNavItem(
                route = AppScreens.Search.route,
                icon = Icons.Default.Search,
                label = "Search"
            ),
            BottomNavItem(
                route = AppScreens.Chat.route,
                icon = Icons.Default.Email,
                label = "Chat"
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
            if (currentRoute in bottomBarRoutes) {
                BottomNavigationBar(
                    items = bottomNavItems,
                    currentRoute = currentRoute ?: AppScreens.Home.route,
                    onItemClick = { route ->
                        if (currentRoute != route) {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
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
                    /*onLogin = {  si existe login: navController.navigate(AppScreens.Home)  }*/
                )
            }
            composable(AppScreens.Register.route) {
                RegisterScreen(
                    onRegisterClick = { navController.navigate(AppScreens.Preferences.route) },
//                    onBack = { navController.popBackStack() }
                )
            }
            composable(AppScreens.Preferences.route) {
                PreferencesScreen(
                    onContinueClick = { allSelections ->
                        navController.navigate(AppScreens.Home.route) {
                            popUpTo(AppScreens.Welcome.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
//                    onBack = { navController.popBackStack() }
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
            composable(AppScreens.Home.route) {
                // Corrige si tu nombre real es ClienteDashboardScreen (ojo con la "i")
                ClienteDashboardScreen(
                    onMenuClick = { navController.navigate(AppScreens.Search.route) },
                    onLikeClick = { navController.navigate(AppScreens.SellerDashboard.route) },
                    onBookmarkClick = { navController.navigate(AppScreens.ProductDetail.route) }
                )
            }
            composable(AppScreens.Search.route) {
                SearchScreen(
                    onBackClick= { navController.popBackStack() },
                    onSearchClick = { /* productId ->
                        navController.navigate("${AppScreens.ProductDetail}/$productId")
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
                    onCheckoutClick = { navController.navigate(AppScreens.SellerDashboard.route) },
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Seller
            composable(AppScreens.SellerDashboard.route) {
                SellerDashboardScreen(
                    onActionClick = { action ->
                        when (action) {
                            "add_product"   -> navController.navigate(AppScreens.CreateProduct.route)
                            "show_products" -> navController.navigate(AppScreens.MyProducts.route)
                            "my_orders"     -> navController.navigate(AppScreens.Orders.route)
                            "shipments"     -> navController.navigate(AppScreens.UpcomingDeliveries.route)
                            "reviews", "comments" -> navController.navigate(AppScreens.Statistics.route)
                            else -> {}
                        }
                    }
                )
            }
            composable(AppScreens.CreateProduct.route) {
                CreateProductScreen(
                    onSaveDraftClick = { navController.popBackStack() },
                )
            }
            composable(AppScreens.MyProducts.route) {
                MyProductsScreen(
                    onBackClick = { navController.popBackStack() },
                    onAddProductClick = {navController.navigate(AppScreens.CreateProduct.route)},
//                    onProductActionClick = { /* productId -> navController.navigate("${AppScreens.ProductDetail}/$productId") */ }
                )
            }
            composable(AppScreens.Orders.route) {
                OrdersScreen(
                    onOrderClick = { navController.navigate(AppScreens.DeliverySignup.route) },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(AppScreens.DeliverySignup.route) {
                DeliverySignupScreen(
                    onSubmitClick = { navController.navigate(AppScreens.UpcomingDeliveries.route) },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(AppScreens.UpcomingDeliveries.route) {
                UpcomingDeliveriesScreen(
                    onAcceptClick = { navController.navigate(AppScreens.DeliveryPickup.route) },
                )
            }
            composable(AppScreens.DeliveryPickup.route) {
                DeliveryPickupScreen()
            }
            composable(AppScreens.Statistics.route) {
                StatisticsScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Tabs de la bottom bar
            composable(AppScreens.Chat.route) { ChatScreen() }
            composable(AppScreens.Notifications.route) { NotificationsScreen() }
            composable(AppScreens.Profile.route) { ProfileScreen() }

            composable(AppScreens.StoreProfile.route) { StoreProfileScreen() }
            composable(AppScreens.Availability.route) { AvailabilityScreen() }
        }
    }
}
