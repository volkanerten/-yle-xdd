package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Appliance
import com.example.data.model.FaultCode
import com.example.data.model.FaultDatabase
import com.example.data.model.ServiceRecord
import com.example.ui.viewmodel.DiagnosticUiState
import com.example.ui.viewmodel.ServiceViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

// Clear Navigation Routes
sealed class Screen {
    object Splash : Screen()
    object Dashboard : Screen()
    object AiScanner : Screen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    viewModel: ServiceViewModel,
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    // When we are in Splash or AiScanner, we omit the standard TopBar for custom full-screen visual control!
    Scaffold(
        topBar = {
            if (currentScreen != Screen.Splash && currentScreen != Screen.AiScanner) {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Logo",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "TeknikXDD",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    },
                    navigationIcon = {
                        if (currentScreen != Screen.Dashboard) {
                            IconButton(onClick = { onNavigate(Screen.Dashboard) }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Geri"
                                )
                            }
                        }
                    },
                    actions = {
                        // AI button in TopAppBar leading to custom scanner
                        IconButton(
                            onClick = { onNavigate(Screen.AiScanner) },
                            modifier = Modifier.testTag("go_to_ai_scanner")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = "AI Scanner",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (currentScreen != Screen.Splash && currentScreen != Screen.AiScanner) innerPadding else PaddingValues(0.dp))
        ) {
            when (currentScreen) {
                is Screen.Splash -> {
                    SplashScreen(
                        onSplashFinished = {
                            onNavigate(Screen.Dashboard)
                        }
                    )
                }
                is Screen.Dashboard -> {
                    DashboardScreen(
                        onNavigateToScanner = { onNavigate(Screen.AiScanner) }
                    )
                }
                is Screen.AiScanner -> {
                    AiScannerScreen(
                        viewModel = viewModel,
                        onBack = { onNavigate(Screen.Dashboard) }
                    )
                }
            }
        }
    }
}

// ---------------- SPLASH SCREEN ----------------
@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val scale = remember { Animatable(0.5f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        // Run gorgeous animations concurrently
        scale.animateTo(
            targetValue = 1.1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        scale.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
        )
    }

    LaunchedEffect(key1 = true) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        // Auto-navigate after 2.6 seconds
        delay(2600)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A), // Dark slate
                        Color(0xFF1E293B)
                    )
                )
            )
            .clickable { onSplashFinished() }, // Allow skip on tap
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Animated Container representing Logo
            Box(
                modifier = Modifier
                    .scale(scale.value)
                    .size(110.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        Brush.sweepGradient(
                            colors = listOf(
                                Color(0xFFEF4444), // red-500
                                Color(0xFF3B82F6), // blue-500
                                Color(0xFFEF4444)
                            )
                        )
                    )
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFF0F172A)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "XDD",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Main animated text header
            var textReveal by remember { mutableStateOf(false) }
            LaunchedEffect(true) {
                delay(200)
                textReveal = true
            }

            AnimatedVisibility(
                visible = textReveal,
                enter = fadeIn() + expandVertically()
            ) {
                Text(
                    text = "XDD Hata",
                    color = Color.White,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Tamirciler İçin Pratik Arıza ve Çözüm Portalı",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }

        // Volkan Aras ERTEN yaptı written nicely at the bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "YAPIMCI",
                color = Color.White.copy(alpha = 0.35f),
                fontSize = 10.sp,
                letterSpacing = 3.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Volkan Aras ERTEN yaptı",
                color = Color(0xFF60A5FA), // modern sky blue
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}

// ---------------- DASHBOARD (FAULT GUIDE) ----------------
@Composable
fun DashboardScreen(
    onNavigateToScanner: () -> Unit
) {
    FaultGuideTab(
        onBrandSelectedForInspection = {},
        onNavigateToScanner = onNavigateToScanner
    )
}

// ---------------- TAB 1: FAULT & REPAIR REFERENCE GUIDE ----------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FaultGuideTab(
    onBrandSelectedForInspection: (String) -> Unit,
    onNavigateToScanner: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Tümü") }
    var selectedBrand by remember { mutableStateOf("Tümü") }

    // Selected fault detail sheet state
    var selectedFaultCode by remember { mutableStateOf<FaultCode?>(null) }

    val presetTypes = listOf("Tümü", "Kombi", "Pelet", "Buzdolabı", "Fırın", "Televizyon", "Çamaşır Makinesi", "Bulaşık Makinesi")
    val presetBrands = listOf("Tümü", "DemirDöküm", "Baymak", "Beko", "Arçelik", "Bosch", "Buderus", "Vaillant")

    val filteredFaults = FaultDatabase.list.filter { item ->
        val matchesSearch = item.code.contains(searchQuery, ignoreCase = true) ||
                item.title.contains(searchQuery, ignoreCase = true) ||
                item.reason.contains(searchQuery, ignoreCase = true) ||
                item.part.contains(searchQuery, ignoreCase = true)

        val matchesType = selectedType == "Tümü" || item.type.equals(selectedType, ignoreCase = true)
        val matchesBrand = selectedBrand == "Tümü" || item.brand.equals(selectedBrand, ignoreCase = true)

        matchesSearch && matchesType && matchesBrand
    }

    // Capture visual context if redirected from AI Scanner
    val currentContext = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp, top = 16.dp)
        ) {
            // Smart AI Header Alert triggering Vision Camera
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Akıllı Kamera Marka Bulucu",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Cihazın markasını tam çıkaramadıysanız vizörle otomatik tahmin edin.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 14.sp
                            )
                        }
                        Button(
                            onClick = onNavigateToScanner,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("Aç", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Hata kodu veya arızalı parça ara... (Örn: F01, E4)") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = null)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Category Chips (Cihaz Türü)
            item {
                Column {
                    Text(
                        text = "Ürün Grubu",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(presetTypes) { type ->
                            val isSelected = type == selectedType
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedType = type },
                                label = { Text(type) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Brand Chips (Marka Filtresi)
            item {
                Column {
                    Text(
                        text = "Kombi & Beyaz Eşya Markası",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(presetBrands) { brand ->
                            val isSelected = brand == selectedBrand
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedBrand = brand },
                                label = { Text(brand) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Headers & Counter
            item {
                Text(
                    text = "Arıza Katalog Sonuçları (${filteredFaults.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Dynamic Results Grid Items
            if (filteredFaults.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 30.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("🔍 Sonuç Bulunamadı", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "Lütfen farklı kombinasyonlarda kelimeler veya arıza kodları girmeyi deneyin.",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredFaults) { fault ->
                    FaultGridItem(
                        fault = fault,
                        onClick = {
                            selectedFaultCode = fault
                        }
                    )
                }
            }
        }

        // Full Detail Overlay Drawer
        selectedFaultCode?.let { fault ->
            AlertDialog(
                onDismissRequest = { selectedFaultCode = null },
                properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.88f)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .align(Alignment.BottomCenter),
                confirmButton = {
                    Button(
                        onClick = { selectedFaultCode = null },
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("Anlaşıldı, Kapat", fontWeight = FontWeight.Bold)
                    }
                },
                title = null,
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp)
                    ) {
                        // Drag Indicator Handle
                        Box(
                            modifier = Modifier
                                .size(48.dp, 4.dp)
                                .clip(CircleShape)
                                .background(Color.Gray.copy(alpha = 0.3f))
                                .align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Header Block
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.errorContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = fault.code,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${fault.brand} ${fault.type}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = fault.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(16.dp))

                        // Core Details Container
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            // Section 1: Why it happened (Neden oldu)
                            item {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(18.dp))
                                        Text("1. Hatanın Nedeni Nedir?", fontWeight = FontWeight.Black, color = Color(0xFFD97706), fontSize = 14.sp)
                                    }
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = fault.reason,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color(0xFF78350F),
                                            modifier = Modifier.padding(14.dp),
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }

                            // Section 2: Defective Part (Arızalı parça)
                            item {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Icon(Icons.Default.Build, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                        Text("2. Hangi Parça Değişmeli/Arızalı?", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                                            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                                            .padding(14.dp)
                                    ) {
                                        Text(
                                            text = fault.part,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }
                            }

                            // Section 3: How to install / Repair (Nasıl takılır)
                            item {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(18.dp))
                                        Text("3. Parça Nasıl Değiştirilir / Nasıl Takılır?", fontWeight = FontWeight.Black, color = Color(0xFF16A34A), fontSize = 14.sp)
                                    }
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                                        border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                                        shape = RoundedCornerShape(14.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = "ADIM ADIM TEKNİK SERVİS TAMİR REHBERİ",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = Color(0xFF166534),
                                                letterSpacing = 1.sp
                                            )
                                            Divider(color = Color(0xFFBBF7D0))
                                            Text(
                                                text = fault.installation,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color(0xFF1E293B),
                                                lineHeight = 20.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun FaultGridItem(
    fault: FaultCode,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Error Code Circle
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.errorContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = fault.code,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // Info Descriptions
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = fault.brand,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.LightGray
                    )
                    Text(
                        text = fault.type,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = fault.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Arızalı Parça: ${fault.part}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Detay",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ---------------- TAB 2: ORIGINAL ACTIVE CUSTOMER LEDGER TRACKER ----------------
@Composable
fun CustomerTrackerTab(
    appliances: List<Appliance>,
    records: List<ServiceRecord>,
    onApplianceClick: (Int) -> Unit,
    onAddApplianceClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filtered = appliances.filter {
        it.contactName.contains(searchQuery, ignoreCase = true) ||
                it.brand.contains(searchQuery, ignoreCase = true) ||
                it.model.contains(searchQuery, ignoreCase = true) ||
                it.type.contains(searchQuery, ignoreCase = true)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 90.dp, top = 16.dp)
        ) {
            // Metrik Özet Başlığı
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricCard(
                        title = "Toplam Cihaz",
                        value = appliances.size.toString(),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        icon = Icons.Default.List,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Aktif Arıza",
                        value = records.count { it.status == "Açık" || it.status == "İşlemde" }.toString(),
                        color = MaterialTheme.colorScheme.errorContainer,
                        icon = Icons.Default.Warning,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Search input
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Müşteri ismi veya cihaz marka/modeli ara...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Content List
            if (filtered.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("😞 Kayıtlı müşteri veya cihaz bulunamadı.")
                    }
                }
            } else {
                items(filtered) { item ->
                    val activeFaults = records.filter { it.applianceId == item.id && (it.status == "Açık" || it.status == "İşlemde") }
                    ApplianceCard(
                        appliance = item,
                        activeFaultsCount = activeFaults.size,
                        onClick = { onApplianceClick(item.id) }
                    )
                }
            }
        }

        // Add Floating Action Button for client creation
        FloatingActionButton(
            onClick = onAddApplianceClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .testTag("add_appliance_fab_tab"),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Yeni Cihaz & Müşteri", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ---------------- DESIGN COMPONENT: METRIC CARD ----------------
@Composable
fun MetricCard(
    title: String,
    value: String,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = modifier.height(95.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// ---------------- DESIGN COMPONENT: APPLIANCE CUSTOM CARD ----------------
@Composable
fun ApplianceCard(
    appliance: Appliance,
    activeFaultsCount: Int,
    onClick: () -> Unit
) {
    val emoji = getApplianceEmoji(appliance.type)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("appliance_card_${appliance.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 26.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${appliance.brand} ${appliance.model}".trim(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Konum: ${appliance.serviceAddress.ifBlank { "Belirtilmedi" }}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Müşteri: ${appliance.contactName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                if (activeFaultsCount > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "$activeFaultsCount Aktif",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(Color(0xFFE2F0D9))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Sorunsuz",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF385723),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// ---------------- APPLIANCE DETAIL SCREEN ----------------
@Composable
fun ApplianceDetailScreen(
    appliance: Appliance,
    records: List<ServiceRecord>,
    viewModel: ServiceViewModel,
    onAddServiceClick: () -> Unit,
    onDeleteAppliance: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Cihazı Sil") },
            text = { Text("Bu cihazı ve bağlı tüm arıza kayıtlarını silmek istediğinizden emin misiniz?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirm = false
                    onDeleteAppliance()
                }) {
                    Text("Evet, Sil", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("İptal")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = getApplianceEmoji(appliance.type), fontSize = 32.sp, modifier = Modifier.padding(end = 8.dp))
                            Column {
                                Text(text = appliance.brand, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Text(text = appliance.model.ifBlank { "Model girmediniz" })
                            }
                        }
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Sil", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                    Divider()
                    DetailInfoRow(label = "Seri No", value = appliance.serialNumber.ifBlank { "Belirtilmedi" })
                    DetailInfoRow(label = "Tarih", value = formatDate(appliance.installationDate))
                    DetailInfoRow(label = "Adres / Konum", value = appliance.serviceAddress.ifBlank { "Belirtilmedi" })
                    DetailInfoRow(label = "Müşteri", value = appliance.contactName)
                    DetailInfoRow(label = "Telefon", value = appliance.contactPhone)
                }
            }
        }

        item {
            Button(
                onClick = onAddServiceClick,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Yeni Arıza Kaydı Bildir", fontWeight = FontWeight.Bold)
            }
        }

        item {
            Text(
                text = "Arıza & İş Kaydı Geçmişi (${records.size})",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (records.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("Cihaza ait aktif bir arıza kaydı bulunmuyor.")
                    }
                }
            }
        } else {
            items(records) { record ->
                ServiceRecordCard(record = record, appliance = appliance, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun DetailInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
    }
}

// ---------------- SERVICE RECORD CARD IMPLEMENTATION WITH AI RETRO ACTION ----------------
@Composable
fun ServiceRecordCard(
    record: ServiceRecord,
    appliance: Appliance,
    viewModel: ServiceViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var notesInput by remember { mutableStateOf(record.technicianNotes) }
    var statusSelect by remember { mutableStateOf(record.status) }
    var showNotesDialog by remember { mutableStateOf(false) }

    val diagnosticState by viewModel.diagnosticState.collectAsStateWithLifecycle()

    val statusColor = when (record.status) {
        "Açık" -> MaterialTheme.colorScheme.error
        "İşlemde" -> Color(0xFFD97706)
        "Çözüldü" -> Color(0xFF16A34A)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    if (showNotesDialog) {
        AlertDialog(
            onDismissRequest = { showNotesDialog = false },
            title = { Text("Durum ve Teknik Servis Notu") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("İş Durumu:")
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Açık", "İşlemde", "Çözüldü").forEach { opt ->
                            val isSel = opt == statusSelect
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { statusSelect = opt }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(opt, color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                    OutlinedTextField(
                        value = notesInput,
                        onValueChange = { notesInput = it },
                        label = { Text("Hizmet / Çözüm Detayı Notu") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showNotesDialog = false
                    viewModel.updateServiceRecord(record.copy(status = statusSelect, technicianNotes = notesInput))
                }) {
                    Text("Kaydet")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNotesDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(record.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text("Tarih: ${formatDate(record.reportedDate)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(record.status, color = statusColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            if (record.faultCode.isNotBlank()) {
                Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(MaterialTheme.colorScheme.errorContainer).padding(horizontal = 6.dp, vertical = 2.dp)) {
                    Text("Sistem Kodu: ${record.faultCode}", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }
            Text(record.description, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp))

            if (record.technicianNotes.isNotBlank()) {
                Box(modifier = Modifier.padding(top = 8.dp).fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surfaceVariant).padding(8.dp)) {
                    Text("👨‍🔧 Not: ${record.technicianNotes}", style = MaterialTheme.typography.bodySmall)
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Divider()
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                viewModel.diagnoseServiceRecord(
                                    recordId = record.id,
                                    brand = appliance.brand,
                                    type = appliance.type,
                                    faultCode = record.faultCode,
                                    description = record.description
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("AI Teşhisi Al", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { showNotesDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.secondary),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Not Ekle / Durum", fontSize = 11.sp)
                        }
                    }

                    if (record.aiDiagnostic.isNotBlank()) {
                        Box(modifier = Modifier.padding(top = 10.dp).fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)).padding(12.dp)) {
                            Text(record.aiDiagnostic, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    } else if (diagnosticState is DiagnosticUiState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(top = 8.dp))
                    }
                }
            }
        }
    }
}

// ---------------- ADD APPLIANCE / CLIENT VIEW ----------------
@Composable
fun AddApplianceScreen(
    onApplianceAdded: (Appliance) -> Unit,
    onCancel: () -> Unit
) {
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Kombi") }
    var serialNo by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var clientName by remember { mutableStateOf("") }
    var clientPhone by remember { mutableStateOf("") }

    val types = listOf("Kombi", "Pelet", "Buzdolabı", "Fırın", "Televizyon", "Çamaşır Makinesi", "Bulaşık Makinesi")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Yeni Cihaz & Müşteri Kaydı", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        item {
            Text("Cihaz Türü", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(types) { t ->
                    val isSel = t == type
                    FilterChip(
                        selected = isSel,
                        onClick = { type = t },
                        label = { Text(t) }
                    )
                }
            }
        }
        item {
            OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Marka (Örn: DemirDöküm)") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("Model Adı (Örn: Atron)") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            OutlinedTextField(value = serialNo, onValueChange = { serialNo = it }, label = { Text("Seri Numarası / Plaka") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Kurulum Konumu / Adres") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            OutlinedTextField(value = clientName, onValueChange = { clientName = it }, label = { Text("Müşteri İsim Soyisim") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            OutlinedTextField(value = clientPhone, onValueChange = { clientPhone = it }, label = { Text("Müşteri Telefon Numarası") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                    Text("İptal")
                }
                Button(
                    onClick = {
                        if (brand.isNotBlank() && clientName.isNotBlank()) {
                            onApplianceAdded(
                                Appliance(
                                    type = type,
                                    brand = brand,
                                    model = model,
                                    serialNumber = serialNo,
                                    serviceAddress = address,
                                    contactName = clientName,
                                    contactPhone = clientPhone
                                )
                            )
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Kaydet")
                }
            }
        }
    }
}

// ---------------- ADD SERVICE TICKETS VIEW ----------------
@Composable
fun AddServiceScreen(
    appliance: Appliance,
    onServiceAdded: (ServiceRecord) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var faultCode by remember { mutableStateOf("") }
    var costInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Arıza Bildir: ${appliance.brand} ${appliance.model}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Arıza Kısa Başlığı (Örn: Sıcak su yok)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = faultCode, onValueChange = { faultCode = it }, label = { Text("Varsa Cihaz Hata Kodu (Örn: F01)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Detaylı Sorun Açıklaması") }, modifier = Modifier.fillMaxWidth(), maxLines = 4)
        OutlinedTextField(value = costInput, onValueChange = { costInput = it }, label = { Text("Tahmini Servis Ücreti (TL)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                Text("Vazgeç")
            }
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val cost = costInput.toDoubleOrNull() ?: 0.0
                        onServiceAdded(
                            ServiceRecord(
                                applianceId = appliance.id,
                                title = title,
                                description = desc,
                                faultCode = faultCode,
                                cost = cost,
                                status = "Açık"
                            )
                        )
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Arızayı Kaydet")
            }
        }
    }
}

// ---------------- IMPOSSIBLY POLISHED AI CAMERA & BRAND SCANNER SCREEN ----------------
@Composable
fun AiScannerScreen(
    viewModel: ServiceViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission Flow state
    var showExplanationDialog by remember { mutableStateOf(true) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Kamera izni reddedildi. AI optik simülatörü çalışacak.", Toast.LENGTH_LONG).show()
        }
    }

    // Interactive mock scanning simulation
    var isScanningMode by remember { mutableStateOf(false) }
    var scanProgress by remember { mutableStateOf(0f) }

    // User text inputs describing the appliance physically
    var physicalDescriptionInput by remember { mutableStateOf("") }

    val predictionText by viewModel.brandPredictionState.collectAsStateWithLifecycle()
    val isPredicting by viewModel.isPredictingBrand.collectAsStateWithLifecycle()

    // Quick fill descriptors
    val quickTemplates = listOf(
        "Kare şeklinde, gri renkli, üzerinde mavi kıvrımlı ince şerit logosu var.",
        "Ön tavanında LCD dairesel ekranı olan, turuncu amblemli beyaz bir kombi kapağı.",
        "Alt tarafında yuvarlak kırmızı ikon logosu duran gri No-Frost buzdolabı gövdesi.",
        "Kapağında büyük B harfi yazan gümüş renkli bulaşık makinesi paneli."
    )

    if (showExplanationDialog) {
        AlertDialog(
            onDismissRequest = { /* Force decision */ },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Text("Yapay Zeka Kameraya Erişmek İstiyor", fontWeight = FontWeight.Black, fontSize = 16.sp)
                }
            },
            text = {
                Text(
                    text = "Kombinin Markasını bilmiyorsanız yapay zeka sizin için Kombinin Markasını Tahmin Edicek Ve Size Sorunun ne olduğunu Sorcak Bu sırada bunu doğru işaretlemeniz gerekiyor. Bunun için yapay zeka kameraya erişme izni istiyor",
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExplanationDialog = false
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Kabul Et", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showExplanationDialog = false
                        // Keep going with virtual simulated viewport
                    }
                ) {
                    Text("Kabul Etme")
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Main Scanning Layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0F19)) // Cool cyber dark
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // High fidelity Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.background(Color.White.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Geri", tint = Color.White)
            }
            Text(
                text = "Optik Marka Tarayıcı",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (hasPermission) Color(0xFF16A34A).copy(alpha = 0.2f) else Color(0xFFD97706).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (hasPermission) Icons.Default.Check else Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (hasPermission) Color(0xFF22C55E) else Color(0xFFF59E0B),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Vizör / Scanning Viewport Section
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.DarkGray.copy(alpha = 0.1f))
                .border(2.dp, Color(0xFF3B82F6).copy(alpha = 0.4f), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Retro Tech Scanner lines & HUD overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Scanning HUD borders
                Box(modifier = Modifier.align(Alignment.TopStart).size(24.dp).drawWithContent {
                    drawRect(color = Color(0xFF3B82F6), size = androidx.compose.ui.geometry.Size(24.dp.toPx(), 4.dp.toPx()))
                    drawRect(color = Color(0xFF3B82F6), size = androidx.compose.ui.geometry.Size(4.dp.toPx(), 24.dp.toPx()))
                })
                Box(modifier = Modifier.align(Alignment.TopEnd).size(24.dp).drawWithContent {
                    drawRect(color = Color(0xFF3B82F6), topLeft = Offset(0f, 0f), size = androidx.compose.ui.geometry.Size(24.dp.toPx(), 4.dp.toPx()))
                    drawRect(color = Color(0xFF3B82F6), topLeft = Offset(20.dp.toPx(), 0f), size = androidx.compose.ui.geometry.Size(4.dp.toPx(), 24.dp.toPx()))
                })
                Box(modifier = Modifier.align(Alignment.BottomStart).size(24.dp).drawWithContent {
                    drawRect(color = Color(0xFF3B82F6), topLeft = Offset(0f, 20.dp.toPx()), size = androidx.compose.ui.geometry.Size(24.dp.toPx(), 4.dp.toPx()))
                    drawRect(color = Color(0xFF3B82F6), topLeft = Offset(0f, 0f), size = androidx.compose.ui.geometry.Size(4.dp.toPx(), 24.dp.toPx()))
                })
                Box(modifier = Modifier.align(Alignment.BottomEnd).size(24.dp).drawWithContent {
                    drawRect(color = Color(0xFF3B82F6), topLeft = Offset(0f, 20.dp.toPx()), size = androidx.compose.ui.geometry.Size(24.dp.toPx(), 4.dp.toPx()))
                    drawRect(color = Color(0xFF3B82F6), topLeft = Offset(20.dp.toPx(), 0f), size = androidx.compose.ui.geometry.Size(4.dp.toPx(), 24.dp.toPx()))
                })

                // High-tech overlay descriptors
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 10.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (hasPermission) "KAMERA REHBERİ AKTİF" else "VIRTUAL AI SENSOR ACTIVE",
                        color = Color(0xFF3B82F6),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                // If Scanning: show laser scan pulse animation
                if (isScanningMode) {
                    val infiniteTransition = rememberInfiniteTransition()
                    val scanY by infiniteTransition.animateFloat(
                        initialValue = 0.1f,
                        targetValue = 0.9f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1500, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(scanY)
                            .drawWithContent {
                                drawLine(
                                    color = Color(0xFFEF4444), // glowing red laser
                                    start = Offset(0f, size.height),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = 4.dp.toPx()
                                )
                            }
                    )

                    Text(
                        text = "ANAFİZ ODAKTA... %${(scanProgress * 100).toInt()}",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .background(Color.Red.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    )
                } else {
                    // Center guide icon
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier
                            .size(72.dp)
                            .align(Alignment.Center)
                    )
                }

                Text(
                    text = "Aygıt vizöründen logoyu veya cihazı ortalayın.",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                )
            }
        }

        // Input Field and Suggestions Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF111827), RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Cihazın Fiziksel Görünümü (Yapay Zekaya Detay Verin):",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = physicalDescriptionInput,
                onValueChange = { physicalDescriptionInput = it },
                placeholder = {
                    Text(
                        "Buraya cihazın görünümünü yazın veya aşağıdaki hazır şablonlardan birine dokunarak doldurun...",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(85.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                    focusedContainerColor = Color(0xFF1F2937),
                    unfocusedContainerColor = Color(0xFF1F2937)
                )
            )

            // Horizontal quick templates selector
            Text(
                text = "Hızlı Hazır Şablonlar (Dokun ve Doldur):",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(quickTemplates) { template ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .clickable {
                                physicalDescriptionInput = template
                            }
                            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = template,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(max = 180.dp)
                        )
                    }
                }
            }

            // CTA Scan Button
            Button(
                onClick = {
                    if (physicalDescriptionInput.isBlank()) {
                        Toast.makeText(context, "Lütfen bir fiziksel tarif yazın veya hazır şablon seçin.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    isScanningMode = true
                    scanProgress = 0f
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("simulate_ai_scan"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                shape = RoundedCornerShape(12.dp),
                enabled = !isScanningMode && !isPredicting
            ) {
                if (isPredicting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Değerlendir & Markayı Tahmin Et", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            // Simulating progress
            if (isScanningMode) {
                LaunchedEffect(Unit) {
                    while (scanProgress < 1.0f) {
                        delay(250)
                        scanProgress += 0.2f
                    }
                    isScanningMode = false
                    viewModel.predictBrand(physicalDescriptionInput)
                }
            }

            // Results Container
            if (predictionText.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    border = BorderStroke(1.dp, Color(0xFF3B82F6).copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🤖 Yapay Zeka Sonucu",
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF60A5FA),
                                fontSize = 14.sp
                            )
                            IconButton(onClick = { viewModel.clearBrandPrediction() }) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                            }
                        }

                        Divider(color = Color.White.copy(alpha = 0.1f))

                        Text(
                            text = predictionText,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            lineHeight = 18.sp
                        )

                        // If brand is detected, offer button to copy or directly view guide
                        Button(
                            onClick = {
                                Toast.makeText(context, "Hata katalogları bu markaya göre hazırlandı!", Toast.LENGTH_SHORT).show()
                                onBack() // Navigate back to guide
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                        ) {
                            Text("Arıza Rehberine Git", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

// ---------------- EMOJI / HELPER UTILITY FOR APPLIANCES ----------------
fun getApplianceEmoji(type: String): String {
    return when (type.lowercase()) {
        "kombi" -> "🔥"
        "pelet" -> "🪵"
        "buzdolabı" -> "❄️"
        "fırın" -> "🍳"
        "televizyon" -> "📺"
        "çamaşır makinesi" -> "🧺"
        "bulaşık makinesi" -> "🍽️"
        else -> "🛠️"
    }
}

fun formatDate(timestamp: Long): String {
    if (timestamp == 0L) return "Belirtilmedi"
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
