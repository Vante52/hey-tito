package com.example.heytito.presentation.navigation

sealed class AppScreens (val route: String) {
    object Welcome : AppScreens("welcome")
    object Register : AppScreens("register")
    object Preferences : AppScreens("preferences")
    object Home : AppScreens("home")
    object Search : AppScreens("search")
    object ProductDetail : AppScreens("product_detail")
    object Cart : AppScreens("cart")
    object SellerDashboard : AppScreens("seller_dashboard")
    object CreateProduct : AppScreens("create_product")
    object MyProducts : AppScreens("my_products")
    object Orders : AppScreens("orders")
    object DeliverySignup : AppScreens("delivery_signup")
    object UpcomingDeliveries : AppScreens("upcoming_deliveries")
    object DeliveryPickup : AppScreens("delivery_pickup")
    object Statistics : AppScreens("statistics")
    object Chat : AppScreens("chat")
    object Notifications : AppScreens("notifications")
    object Profile : AppScreens("profile")
    object StoreProfile : AppScreens("store_profile")
    object Availability : AppScreens("availability")
}
