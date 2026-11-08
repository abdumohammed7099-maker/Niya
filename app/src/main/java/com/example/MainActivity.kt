package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.NiyaStrings
import com.example.model.DonationStatus
import com.example.ui.NiyaViewModel
import com.example.ui.ScreenTab
import com.example.ui.components.DonationReceiptDialog
import com.example.ui.components.NiyaTopAppBar
import com.example.ui.screens.AdminDashboardView
import com.example.ui.screens.AdminLoginDialog
import com.example.ui.screens.CreateProjectDialog
import com.example.ui.screens.DonateScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.RecordExpenseDialog
import com.example.ui.screens.TransparencyScreen
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPastel
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPastel
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NiyaTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      NiyaTheme {
        NiyaApp()
      }
    }
  }
}

@Composable
fun NiyaApp(viewModel: NiyaViewModel = viewModel()) {
  val context = LocalContext.current
  val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
  val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
  val projects by viewModel.projects.collectAsStateWithLifecycle()
  val donations by viewModel.donations.collectAsStateWithLifecycle()
  val expenses by viewModel.expenses.collectAsStateWithLifecycle()
  val auditLogs by viewModel.auditLogs.collectAsStateWithLifecycle()
  val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsStateWithLifecycle()
  val targetProject by viewModel.targetDonationProject.collectAsStateWithLifecycle()
  val categoryFilter by viewModel.selectedCategoryFilter.collectAsStateWithLifecycle()
  val activeReceipt by viewModel.activeReceipt.collectAsStateWithLifecycle()
  val showAdminLogin by viewModel.showAdminLogin.collectAsStateWithLifecycle()
  val showNewProjectDialog by viewModel.showNewProjectDialog.collectAsStateWithLifecycle()
  val showNewExpenseDialog by viewModel.showNewExpenseDialog.collectAsStateWithLifecycle()
  val transparencySummary by viewModel.transparencySummary.collectAsStateWithLifecycle()
  val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

  var viewingAdminDashboard by remember { mutableStateOf(false) }

  // Handle Toast feedback messages
  LaunchedEffect(userMessage) {
    userMessage?.let {
      Toast.makeText(context, it, Toast.LENGTH_LONG).show()
      viewModel.clearUserMessage()
    }
  }

  // BackHandler navigation behavior
  BackHandler(enabled = viewingAdminDashboard || currentTab != ScreenTab.HOME) {
    if (viewingAdminDashboard) {
      viewingAdminDashboard = false
    } else {
      viewModel.selectTab(ScreenTab.HOME)
    }
  }

  if (viewingAdminDashboard && isAdminLoggedIn) {
    AdminDashboardView(
      selectedLanguage = selectedLanguage,
      projects = projects,
      donations = donations,
      expenses = expenses,
      auditLogs = auditLogs,
      onClose = { viewingAdminDashboard = false },
      onVerifyDonation = { viewModel.verifyPendingDonation(it) },
      onOpenNewProject = { viewModel.openNewProjectDialog() },
      onOpenNewExpense = { viewModel.openNewExpenseDialog() },
      onLogout = {
        viewModel.logoutAdmin()
        viewingAdminDashboard = false
      }
    )
  } else {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
        NiyaTopAppBar(
          selectedLanguage = selectedLanguage,
          onLanguageSelected = { viewModel.setLanguage(it) },
          isAdmin = isAdminLoggedIn,
          onAdminClick = {
            if (isAdminLoggedIn) {
              viewingAdminDashboard = true
            } else {
              viewModel.openAdminLogin()
            }
          }
        )
      },
      bottomBar = {
        NavigationBar(
          containerColor = MaterialTheme.colorScheme.surface,
          tonalElevation = 6.dp,
          modifier = Modifier.testTag("niya_bottom_navigation")
        ) {
          val pendingCount = donations.count { it.status == DonationStatus.PENDING_VERIFICATION }

          // 1. መነሻ (Home)
          NavigationBarItem(
            selected = currentTab == ScreenTab.HOME,
            onClick = { viewModel.selectTab(ScreenTab.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = {
              Text(
                text = NiyaStrings.navHome(selectedLanguage),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = EmeraldPrimary,
              selectedTextColor = EmeraldPrimary,
              indicatorColor = EmeraldPastel
            ),
            modifier = Modifier.testTag("nav_tab_home")
          )

          // 2. ፕሮጀክቶች (Projects)
          NavigationBarItem(
            selected = currentTab == ScreenTab.PROJECTS,
            onClick = { viewModel.selectTab(ScreenTab.PROJECTS) },
            icon = { Icon(Icons.Default.GridView, contentDescription = "Projects") },
            label = {
              Text(
                text = NiyaStrings.navProjects(selectedLanguage),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = EmeraldPrimary,
              selectedTextColor = EmeraldPrimary,
              indicatorColor = EmeraldPastel
            ),
            modifier = Modifier.testTag("nav_tab_projects")
          )

          // 3. ሰደቃ አድርግ (Donate)
          NavigationBarItem(
            selected = currentTab == ScreenTab.DONATE,
            onClick = { viewModel.selectTab(ScreenTab.DONATE) },
            icon = {
              Icon(
                Icons.Default.VolunteerActivism,
                contentDescription = "Donate",
                tint = if (currentTab == ScreenTab.DONATE) GoldDark else GoldPrimary
              )
            },
            label = {
              Text(
                text = NiyaStrings.navDonate(selectedLanguage),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = GoldDark,
              selectedTextColor = GoldDark,
              indicatorColor = GoldPastel
            ),
            modifier = Modifier.testTag("nav_tab_donate")
          )

          // 4. ሪፖርት (Transparency & Reports)
          NavigationBarItem(
            selected = currentTab == ScreenTab.TRANSPARENCY,
            onClick = { viewModel.selectTab(ScreenTab.TRANSPARENCY) },
            icon = { Icon(Icons.Default.Assessment, contentDescription = "Transparency") },
            label = {
              Text(
                text = NiyaStrings.navTransparency(selectedLanguage),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = EmeraldPrimary,
              selectedTextColor = EmeraldPrimary,
              indicatorColor = EmeraldPastel
            ),
            modifier = Modifier.testTag("nav_tab_transparency")
          )

          // 5. መገለጫ (Profile & Guide)
          NavigationBarItem(
            selected = currentTab == ScreenTab.PROFILE,
            onClick = { viewModel.selectTab(ScreenTab.PROFILE) },
            icon = {
              if (isAdminLoggedIn && pendingCount > 0) {
                BadgedBox(
                  badge = {
                    Badge(containerColor = GoldPrimary) {
                      Text("$pendingCount", color = Color.White)
                    }
                  }
                ) {
                  Icon(Icons.Default.Person, contentDescription = "Profile")
                }
              } else {
                Icon(Icons.Default.Person, contentDescription = "Profile")
              }
            },
            label = {
              Text(
                text = NiyaStrings.navProfile(selectedLanguage),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = EmeraldPrimary,
              selectedTextColor = EmeraldPrimary,
              indicatorColor = EmeraldPastel
            ),
            modifier = Modifier.testTag("nav_tab_profile")
          )
        }
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        when (currentTab) {
          ScreenTab.HOME -> {
            HomeScreen(
              selectedLanguage = selectedLanguage,
              projects = projects,
              donations = donations,
              transparencySummary = transparencySummary,
              islamicQuotes = viewModel.islamicQuotes,
              onDonateClick = { viewModel.selectTab(ScreenTab.DONATE) },
              onCategoryClick = { category ->
                viewModel.startDonationForCategory(category)
              },
              onViewAllProjectsClick = { viewModel.selectTab(ScreenTab.PROJECTS) },
              onDonationClick = { donation -> viewModel.viewReceipt(donation) }
            )
          }

          ScreenTab.PROJECTS -> {
            ProjectsScreen(
              selectedLanguage = selectedLanguage,
              projects = projects,
              selectedCategoryFilter = categoryFilter,
              onCategoryFilterSelected = { viewModel.setCategoryFilter(it) },
              onDonateToProject = { project -> viewModel.startDonationForProject(project) }
            )
          }

          ScreenTab.DONATE -> {
            DonateScreen(
              selectedLanguage = selectedLanguage,
              projects = projects,
              targetProject = targetProject,
              onSelectProject = { viewModel.startDonationForProject(it) },
              onSubmitDonation = { amt, name, anon, phone, niyyah, note, provider, ref ->
                viewModel.submitDonation(amt, name, anon, phone, niyyah, note, provider, ref)
              }
            )
          }

          ScreenTab.TRANSPARENCY -> {
            TransparencyScreen(
              selectedLanguage = selectedLanguage,
              summary = transparencySummary,
              projects = projects,
              expenses = expenses
            )
          }

          ScreenTab.PROFILE -> {
            ProfileScreen(
              selectedLanguage = selectedLanguage,
              donations = donations,
              isAdmin = isAdminLoggedIn,
              onLanguageSelected = { viewModel.setLanguage(it) },
              onDonationClick = { donation -> viewModel.viewReceipt(donation) },
              onAdminClick = {
                if (isAdminLoggedIn) {
                  viewingAdminDashboard = true
                } else {
                  viewModel.openAdminLogin()
                }
              }
            )
          }
        }
      }
    }
  }

  // Receipts and Admin Dialogs
  activeReceipt?.let { donation ->
    DonationReceiptDialog(
      donation = donation,
      selectedLanguage = selectedLanguage,
      onDismiss = { viewModel.dismissReceipt() }
    )
  }

  if (showAdminLogin) {
    AdminLoginDialog(
      onDismiss = { viewModel.closeAdminLogin() },
      onLogin = { pin ->
        val success = viewModel.loginAdmin(pin)
        if (success) {
          viewingAdminDashboard = true
        }
        success
      }
    )
  }

  if (showNewProjectDialog) {
    CreateProjectDialog(
      onDismiss = { viewModel.closeNewProjectDialog() },
      onCreate = { tAm, tEn, cat, dAm, dEn, lAm, lEn, target, ben ->
        viewModel.createProject(tAm, tEn, cat, dAm, dEn, lAm, lEn, target, ben)
      }
    )
  }

  if (showNewExpenseDialog) {
    RecordExpenseDialog(
      projects = projects,
      onDismiss = { viewModel.closeNewExpenseDialog() },
      onRecord = { pId, tAm, tEn, amt, vendor, ref ->
        viewModel.recordExpense(pId, tAm, tEn, amt, vendor, ref)
      }
    )
  }
}
