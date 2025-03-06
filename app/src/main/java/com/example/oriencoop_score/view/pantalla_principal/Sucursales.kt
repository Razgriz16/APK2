package com.example.oriencoop_score.view.pantalla_principal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
/*
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
*/
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import com.example.oriencoop_score.model.Branch
import com.example.oriencoop_score.model.City
import com.example.oriencoop_score.model.Region
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SucursalesScreen(navController: NavController) {
    // Sample data - you can replace this with your actual data from API
    val regions = remember {
        mutableStateListOf(
            Region(
                name = "Región de Valparaíso",
                cities = emptyList()
            ),
            Region(
                name = "Metropolitana de Santiago",
                cities = emptyList()
            ),
            Region(
                name = "Región de O'Higgins",
                cities = emptyList()
            ),
            Region(
                name = "Región del Maule",
                cities = listOf(
                    City(
                        name = "Cauquenes",
                        branches = emptyList()
                    ),
                    City(
                        name = "Curicó",
                        branches = emptyList()
                    ),
                    City(
                        name = "Molina",
                        branches = emptyList()
                    ),
                    City(
                        name = "Linares",
                        branches = emptyList()
                    ),
                    City(
                        name = "Parral",
                        branches = emptyList()
                    ),
                    City(
                        name = "San Javier",
                        branches = emptyList()
                    ),
                    City(
                        name = "Constitución",
                        branches = emptyList()
                    ),
                    City(
                        name = "Curepto",
                        branches = emptyList()
                    ),
                    City(
                        name = "San Clemente",
                        branches = emptyList()
                    ),
                    City(
                        name = "Talca",
                        branches = listOf(
                            Branch(
                                name = "Talca - 14 Oriente",
                                address = "14 Oriente 968 - Talca",
                                phone = "71-2201102",
                                agent = "Sra. Madeleine Bravo P.",
                                schedule = listOf(
                                    "Continuado: 8.50 a 17.10 hrs. (L a J)",
                                    "Continuado: 8.50 a 16.10 hrs. (V)"
                                ),
                                latitude = -35.4272,
                                longitude = -71.6553
                            ),
                            Branch(
                                name = "Talca 1 Sur",
                                address = "1 Sur 814 - Talca",
                                phone = "71-2201097",
                                agent = "Sra. Yolanda Albornoz V.",
                                schedule = listOf(
                                    "Continuado: 8.50 a 17.10 hrs. (L a J)",
                                    "Continuado: 8.50 a 16.10 hrs. (V)"
                                ),
                                latitude = -35.4278,
                                longitude = -71.6636
                            )
                        )
                    )
                )
            ),
            Region(
                name = "Región del Bio Bio",
                cities = emptyList()
            ),
            Region(
                name = "Región de Ñuble",
                cities = emptyList()
            ),
            Region(
                name = "Región de La Araucanía",
                cities = emptyList()
            )
        )
    }

    val context = LocalContext.current
    var selectedBranch by remember { mutableStateOf<Branch?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sucursales") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(bottom = 16.dp)) {
                BottomBar(navController = navController, currentRoute = navController.currentDestination?.route ?: "")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            // Show branch details and map if a branch is selected
            selectedBranch?.let { branch ->
                BranchDetailView(
                    branch = branch,
                    onBackClick = { selectedBranch = null },
                    onOpenMap = { lat, lng, address ->
                        val gmmIntentUri = Uri.parse("geo:$lat,$lng?q=${Uri.encode(address)}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        if (mapIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(mapIntent)
                        }
                    }
                )
            } ?: run {
                // Show the list of regions and branches
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(regions) { region ->
                        RegionItem(
                            region = region,
                            onRegionClick = {
                                region.isExpanded.value = !region.isExpanded.value
                            },
                            onCityClick = { city ->
                                city.isExpanded.value = !city.isExpanded.value
                            },
                            onBranchClick = { branch ->
                                selectedBranch = branch
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RegionItem(
    region: Region,
    onRegionClick: () -> Unit,
    onCityClick: (City) -> Unit,
    onBranchClick: (Branch) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onRegionClick() }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (region.isExpanded.value) Icons.Default.KeyboardArrowDown else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = if (region.isExpanded.value) "Collapse" else "Expand",
                tint = Color(0xFFF39C12), // Amber color from the image
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = region.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF333333)
            )
        }

        if (region.isExpanded.value) {
            region.cities.forEach { city ->
                CityItem(
                    city = city,
                    onCityClick = { onCityClick(city) },
                    onBranchClick = onBranchClick,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }

    HorizontalDivider(
        color = Color.LightGray,
        thickness = 0.5.dp,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun CityItem(
    city: City,
    onCityClick: () -> Unit,
    onBranchClick: (Branch) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCityClick() }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (city.isExpanded.value) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                contentDescription = if (city.isExpanded.value) "Collapse" else "Expand",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = city.name,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = Color(0xFF007BFF)
            )
        }

        if (city.isExpanded.value) {
            city.branches.forEach { branch ->
                BranchItem(
                    branch = branch,
                    onBranchClick = { onBranchClick(branch) },
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
fun BranchItem(
    branch: Branch,
    onBranchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onBranchClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = branch.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFFF39C12) // Amber color
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "Dirección: ${branch.address}",
                fontSize = 14.sp
            )
            Text(
                text = "Teléfono: ${branch.phone}",
                fontSize = 14.sp
            )
            Text(
                text = "Ver detalles...",
                color = Color.Blue,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun BranchDetailView(
    branch: Branch,
    onBackClick: () -> Unit,
    onOpenMap: (Double, Double, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Title with back button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver"
                )
            }
            Text(
                text = branch.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF39C12) // Amber color
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Map
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            /*
            val branchLocation = LatLng(branch.latitude, branch.longitude)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(branchLocation, 15f)
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = branchLocation),
                    title = branch.name,
                    snippet = branch.address
                )
            }
*/
            // Map controls
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                Button(
                    onClick = { onOpenMap(branch.latitude, branch.longitude, branch.address) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
                ) {
                    Text("Cómo llegar")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Branch info
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Información de Sucursal",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Dirección:",
                    fontWeight = FontWeight.Bold
                )
                Text(text = branch.address)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Teléfono:",
                    fontWeight = FontWeight.Bold
                )
                Text(text = branch.phone)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Agente:",
                    fontWeight = FontWeight.Bold
                )
                Text(text = branch.agent)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Horarios:",
                    fontWeight = FontWeight.Bold
                )
                branch.schedule.forEach { schedule ->
                    Text(text = schedule)
                }
            }
        }
    }
}

@Composable
fun BottomNavItem(
    label: String,
    icon: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        icon()
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) Color(0xFF007BFF) else Color.Gray
        )
    }
}

@Composable
fun Spacer(height: Int) {
    Spacer(modifier = Modifier.height(height.dp))
}