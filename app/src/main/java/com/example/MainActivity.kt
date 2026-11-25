package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.MeadowViewModel
import com.example.ui.screens.DailyDiscoveriesScreen
import com.example.ui.screens.DailyResponseScreen
import com.example.ui.screens.EmotionCheckInScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoodCalendarScreen
import com.example.ui.screens.RelaxTab
import com.example.ui.screens.RelaxZoneScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SupportResourcesDialog
import com.example.ui.theme.PocketMeadowTheme

object AppRoutes {
    const val HOME = "home"
    const val CHECK_IN = "check_in"
    const val RESPONSE = "daily_response"
    const val RELAX = "relax_zone"
    const val DISCOVER = "daily_discoveries"
    const val CALENDAR = "mood_calendar"
    const val SETTINGS = "settings"
}

private data class TopNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val topNavItems = listOf(
    TopNavItem(AppRoutes.HOME, "Home", Icons.Rounded.Home, Icons.Outlined.Home),
    TopNavItem(AppRoutes.CHECK_IN, "Check-In", Icons.Rounded.Favorite, Icons.Outlined.FavoriteBorder),
    TopNavItem(AppRoutes.RELAX, "Relax", Icons.Rounded.Spa, Icons.Outlined.Spa),
    TopNavItem(AppRoutes.DISCOVER, "Discover", Icons.Rounded.AutoAwesome, Icons.Outlined.AutoAwesome),
    TopNavItem(AppRoutes.CALENDAR, "Calendar", Icons.Rounded.CalendarMonth, Icons.Outlined.CalendarMonth)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MeadowViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            PocketMeadowTheme(
                highContrast = uiState.settings.highContrast,
                largerText = uiState.settings.largerText
            ) {
                PocketMeadowRoot(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun PocketMeadowRoot(viewModel: MeadowViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: AppRoutes.HOME

    var relaxInitialTab by remember { mutableStateOf(RelaxTab.TINY_RESET) }
    var showSupportDialog by remember { mutableStateOf(false) }

    if (showSupportDialog) {
        SupportResourcesDialog(onDismiss = { showSupportDialog = false })
    }

    fun navigateTopLevel(route: String) {
        if (route == AppRoutes.CHECK_IN) {
            viewModel.startCheckInForDate(uiState.todayIso)
        }
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val showBottomOrRail = currentRoute != AppRoutes.RESPONSE && currentRoute != AppRoutes.SETTINGS

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useNavigationRail = maxWidth >= 600.dp

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                if (showBottomOrRail && !useNavigationRail) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        topNavItems.forEach { item ->
                            val selected = currentRoute == item.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = { navigateTopLevel(item.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label
                                    )
                                },
                                label = { Text(item.label) },
                                modifier = Modifier.testTag("nav_${item.route}")
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (showBottomOrRail && useNavigationRail) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        topNavItems.forEach { item ->
                            val selected = currentRoute == item.route
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateTopLevel(item.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label
                                    )
                                },
                                label = { Text(item.label) },
                                modifier = Modifier.testTag("rail_${item.route}")
                            )
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = AppRoutes.HOME,
                    modifier = Modifier.weight(1f)
                ) {
                    composable(AppRoutes.HOME) {
                        HomeScreen(
                            uiState = uiState,
                            onOpenCheckIn = { navigateTopLevel(AppRoutes.CHECK_IN) },
                            onOpenDailyResponse = { navController.navigate(AppRoutes.RESPONSE) },
                            onOpenRelaxTinyReset = {
                                relaxInitialTab = RelaxTab.TINY_RESET
                                navigateTopLevel(AppRoutes.RELAX)
                            },
                            onOpenDiscoveries = { navigateTopLevel(AppRoutes.DISCOVER) },
                            onOpenSettings = { navController.navigate(AppRoutes.SETTINGS) },
                            onShowSupportSheet = { showSupportDialog = true }
                        )
                    }

                    composable(AppRoutes.CHECK_IN) {
                        EmotionCheckInScreen(
                            uiState = uiState,
                            onToggleEmotion = viewModel::toggleEmotionSelection,
                            onNoteChanged = viewModel::updateDraftNote,
                            onSaveCheckIn = {
                                viewModel.saveCheckIn {
                                    navController.navigate(AppRoutes.RESPONSE)
                                }
                            }
                        )
                    }

                    composable(AppRoutes.RESPONSE) {
                        DailyResponseScreen(
                            uiState = uiState,
                            soundEngine = viewModel.soundEngine,
                            voiceNarrator = viewModel.voiceNarrator,
                            onToggleVoice = viewModel::setVoiceNarrationEnabled,
                            onToggleSubtitles = viewModel::setSubtitlesEnabled,
                            onOpenRelaxTinyReset = {
                                relaxInitialTab = RelaxTab.TINY_RESET
                                navigateTopLevel(AppRoutes.RELAX)
                            },
                            onShowSupportSheet = { showSupportDialog = true },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(AppRoutes.RELAX) {
                        RelaxZoneScreen(
                            uiState = uiState,
                            soundEngine = viewModel.soundEngine,
                            selectedTab = relaxInitialTab,
                            onSelectTab = { relaxInitialTab = it },
                            onCompletedTinyReset = viewModel::markTinyResetCompleted,
                            onPlantFlower = viewModel::plantThoughtFlower
                        )
                    }

                    composable(AppRoutes.DISCOVER) {
                        DailyDiscoveriesScreen(
                            uiState = uiState,
                            soundEngine = viewModel.soundEngine,
                            voiceNarrator = viewModel.voiceNarrator,
                            onToggleVoice = viewModel::setVoiceNarrationEnabled,
                            onToggleSubtitles = viewModel::setSubtitlesEnabled,
                            onMarkChallengeDone = viewModel::markDailyChallengeDone,
                            onOpenSurprise = viewModel::openDailySurprise,
                            onOpenDoodle = {
                                relaxInitialTab = RelaxTab.MINI_DOODLE
                                navigateTopLevel(AppRoutes.RELAX)
                            }
                        )
                    }

                    composable(AppRoutes.CALENDAR) {
                        MoodCalendarScreen(
                            uiState = uiState,
                            onEditDate = { dateIso ->
                                viewModel.startCheckInForDate(dateIso)
                                navController.navigate(AppRoutes.CHECK_IN)
                            },
                            onDeleteDate = viewModel::deleteEntryForDate
                        )
                    }

                    composable(AppRoutes.SETTINGS) {
                        SettingsScreen(
                            uiState = uiState,
                            onSoundEnabledChange = viewModel::setSoundEnabled,
                            onVoiceNarrationChange = viewModel::setVoiceNarrationEnabled,
                            onVolumeChange = viewModel::setMusicVolume,
                            onSubtitlesChange = viewModel::setSubtitlesEnabled,
                            onReducedMotionChange = viewModel::setReducedMotion,
                            onHighContrastChange = viewModel::setHighContrast,
                            onLargerTextChange = viewModel::setLargerText,
                            onClearMoodEntries = viewModel::clearAllMoodEntries,
                            onClearGarden = viewModel::clearThoughtGarden,
                            onResetAllData = viewModel::resetAllAppData,
                            onShowSupportSheet = { showSupportDialog = true },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
