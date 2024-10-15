package com.example.colllette.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.colllette.model.Product
import com.example.colllette.ui.theme.BannerCarousel
import com.example.colllette.ui.theme.customBlue
import com.example.colllette.viewmodel.ProductViewModel
import com.example.colllette.viewmodel.UserViewModel
import com.example.colllette.viewmodel.UserViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListingScreen(
    productViewModel: ProductViewModel,
    userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(LocalContext.current.applicationContext as android.app.Application)),
    onNavigateToCart: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val products by productViewModel.products.collectAsState()
    val cart by productViewModel.cart.collectAsState()
    val isLoading by productViewModel.isLoading.collectAsState()
    val error by productViewModel.error.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Collect user data
    val user by userViewModel.user.collectAsState()

    val filteredProducts = products.filter { product ->
        product.name.contains(searchQuery, ignoreCase = true) ||
                product.category.contains(searchQuery, ignoreCase = true)
    }

    // Sample placeholder image URLs
    val bannerImages = listOf(
        "https://as1.ftcdn.net/v2/jpg/04/65/46/52/1000_F_465465254_1pN9MGrA831idD6zIBL7q8rnZZpUCQTy.jpg",
        "https://as2.ftcdn.net/v2/jpg/02/49/50/15/1000_F_249501541_XmWdfAfUbWAvGxBwAM0ba2aYT36ntlpH.jpg"
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Our Products",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = customBlue
                    )
                },
                actions = {
                    // User Icon
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = customBlue
                        )
                    }

                    // Cart Icon
                    Box {
                        IconButton(onClick = onNavigateToCart) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Go to Cart", tint = customBlue)
                        }
                        if (cart?.items?.isNotEmpty() == true) {
                            Text(
                                text = cart?.items?.size.toString(),
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-8).dp, y = 8.dp)
                                    .size(18.dp)
                                    .background(Color.Red, CircleShape)
                                    .wrapContentSize(Alignment.Center),
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Welcome Message with User's Name
            user?.let {
                Text(
                    text = "Welcome, ${it.firstName}!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = customBlue,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                placeholder = { Text("Search products or categories...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )
            // Banner Carousel
            BannerCarousel(
                imageUrls = bannerImages,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (error != null) {
                Text(text = error ?: "An unknown error occurred", color = MaterialTheme.colorScheme.error)
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredProducts) { product ->
                        ProductCard(
                            product = product,
                            onAddToCart = {
                                productViewModel.addToCart(product)
                                scope.launch {
                                    snackbarHostState.showSnackbar("${product.name} added to cart")
                                }
                            },
                            onProductClick = {
                                productViewModel.fetchProductDetails(product.id)
                                onNavigateToProductDetails(product.id)
                            }
                        )
                    }
                }
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    product: Product,
    onAddToCart: () -> Unit,
    onProductClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.6f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = onProductClick
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Add product image at the top of the card
            AsyncImage(
                model = product.imageUrl ?: "https://via.placeholder.com/150", // Fallback URL if imageUrl is null
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentScale = ContentScale.Crop // Crop the image to fill the width
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    CategoryBadge(category = product.category)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$${product.price}",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onAddToCart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                colors = ButtonDefaults.buttonColors(containerColor = customBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = "Add to Cart",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add to Cart", fontSize = 14.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun CategoryBadge(category: String, modifier: Modifier = Modifier) {
    val badgeColor = remember(category) {
        when (category.lowercase()) {
            "shirts" -> Color(0xFF1E88E5)
            "t-shirts" -> Color(0xFFD81B60)
            "trousers" -> Color(0xFF43A047)
            "skirts" -> Color(0xFFFFB300)
            "shorts" -> Color(0xFF8E24AA)
            else -> Color(0xFF607D8B)
        }
    }

    Surface(
        color = badgeColor,
        shape = CircleShape,
        modifier = modifier
    ) {
        Text(
            text = category,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}