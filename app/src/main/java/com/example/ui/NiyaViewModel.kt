package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.NiyaRepository
import com.example.model.AdminAuditLog
import com.example.model.CharityProject
import com.example.model.Donation
import com.example.model.DonationCategory
import com.example.model.ExpenseItem
import com.example.model.IslamicQuote
import com.example.model.Language
import com.example.model.NiyyahType
import com.example.model.PaymentProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
  HOME,
  PROJECTS,
  DONATE,
  TRANSPARENCY,
  PROFILE
}

data class TransparencySummary(
  val totalCollected: Double,
  val totalSpent: Double,
  val remainingBalance: Double,
  val activeProjectsCount: Int,
  val completedProjectsCount: Int,
  val totalBeneficiaries: Int,
  val administrativeExpensePercent: Double = 3.2
)

class NiyaViewModel(
  private val repository: NiyaRepository = NiyaRepository()
) : ViewModel() {

  private val _currentTab = MutableStateFlow(ScreenTab.HOME)
  val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

  val selectedLanguage: StateFlow<Language> = repository.selectedLanguage
  val projects: StateFlow<List<CharityProject>> = repository.projects
  val donations: StateFlow<List<Donation>> = repository.donations
  val expenses: StateFlow<List<ExpenseItem>> = repository.expenses
  val auditLogs: StateFlow<List<AdminAuditLog>> = repository.auditLogs
  val isAdminLoggedIn: StateFlow<Boolean> = repository.isAdminLoggedIn
  val islamicQuotes: List<IslamicQuote> = repository.islamicQuotes

  // Category filter on Projects tab (null means ALL)
  private val _selectedCategoryFilter = MutableStateFlow<DonationCategory?>(null)
  val selectedCategoryFilter: StateFlow<DonationCategory?> = _selectedCategoryFilter.asStateFlow()

  // Project selected for donation
  private val _targetDonationProject = MutableStateFlow<CharityProject?>(null)
  val targetDonationProject: StateFlow<CharityProject?> = _targetDonationProject.asStateFlow()

  // Active receipt to display in modal/dialog
  private val _activeReceipt = MutableStateFlow<Donation?>(null)
  val activeReceipt: StateFlow<Donation?> = _activeReceipt.asStateFlow()

  // Admin login dialog state
  private val _showAdminLogin = MutableStateFlow(false)
  val showAdminLogin: StateFlow<Boolean> = _showAdminLogin.asStateFlow()

  // Admin action dialogs
  private val _showNewProjectDialog = MutableStateFlow(false)
  val showNewProjectDialog: StateFlow<Boolean> = _showNewProjectDialog.asStateFlow()

  private val _showNewExpenseDialog = MutableStateFlow(false)
  val showNewExpenseDialog: StateFlow<Boolean> = _showNewExpenseDialog.asStateFlow()

  // Toast / feedback message
  private val _userMessage = MutableStateFlow<String?>(null)
  val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

  // Combined Financial Transparency Dashboard calculations
  val transparencySummary: StateFlow<TransparencySummary> = combine(
    projects, donations, expenses
  ) { projList, _, expList ->
    val totalColl = projList.sumOf { it.raisedAmount }
    val totalExp = expList.sumOf { it.amount }
    val balance = (totalColl - totalExp).coerceAtLeast(0.0)
    val active = projList.count { it.status != com.example.model.ProjectStatus.COMPLETED }
    val completed = projList.count { it.status == com.example.model.ProjectStatus.COMPLETED }
    val beneficiaries = projList.sumOf { it.beneficiaryCount }

    TransparencySummary(
      totalCollected = totalColl,
      totalSpent = totalExp,
      remainingBalance = balance,
      activeProjectsCount = active,
      completedProjectsCount = completed,
      totalBeneficiaries = beneficiaries
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = TransparencySummary(0.0, 0.0, 0.0, 0, 0, 0)
  )

  fun selectTab(tab: ScreenTab) {
    _currentTab.value = tab
  }

  fun setLanguage(language: Language) {
    repository.setLanguage(language)
  }

  fun setCategoryFilter(category: DonationCategory?) {
    _selectedCategoryFilter.value = category
  }

  fun startDonationForProject(project: CharityProject?) {
    _targetDonationProject.value = project
    _currentTab.value = ScreenTab.DONATE
  }

  fun startDonationForCategory(category: DonationCategory) {
    val project = projects.value.firstOrNull { it.category == category }
    _targetDonationProject.value = project
    _currentTab.value = ScreenTab.DONATE
  }

  fun submitDonation(
    amount: Double,
    donorName: String,
    isAnonymous: Boolean,
    donorPhone: String,
    niyyah: NiyyahType,
    intentionNote: String,
    paymentProvider: PaymentProvider,
    paymentReference: String
  ) {
    viewModelScope.launch {
      val project = _targetDonationProject.value
      val projId = project?.id
      val projNameAm = project?.titleAm ?: "አጠቃላይ የሰደቃ ፈንድ (General Sadaqah Pool)"
      val projNameEn = project?.titleEn ?: "General Sadaqah & Relief Fund"
      val cat = project?.category ?: DonationCategory.POOR_RELIEF

      val donation = repository.addDonation(
        projectId = projId,
        projectNameAm = projNameAm,
        projectNameEn = projNameEn,
        category = cat,
        amount = amount,
        donorName = donorName,
        isAnonymous = isAnonymous,
        donorPhone = donorPhone,
        niyyah = niyyah,
        intentionNote = intentionNote,
        paymentProvider = paymentProvider,
        paymentReference = paymentReference
      )

      _activeReceipt.value = donation
      val msg = when (selectedLanguage.value) {
        Language.AMHARIC -> "ሰደቃዎ በስኬት ተቀብለናል! ጀዛከላሁ ኸይረን። አላህ መልካም ስራዎን ይቀበልዎ።"
        Language.ENGLISH -> "Donation recorded successfully! JazakAllah Khair. May Allah accept your Sadaqah."
        Language.ARABIC -> "جزاكم الله خيراً، تم تسجيل صدقتكم بنجاح. تقبل الله منا ومنكم صالح الأعمال."
      }
      _userMessage.value = msg
    }
  }

  fun dismissReceipt() {
    _activeReceipt.value = null
  }

  fun viewReceipt(donation: Donation) {
    _activeReceipt.value = donation
  }

  fun clearUserMessage() {
    _userMessage.value = null
  }

  // Admin controls
  fun openAdminLogin() {
    _showAdminLogin.value = true
  }

  fun closeAdminLogin() {
    _showAdminLogin.value = false
  }

  fun loginAdmin(pin: String): Boolean {
    // Standard secure admin PIN
    if (pin == "1994" || pin == "7860") {
      repository.setAdminLoggedIn(true)
      _showAdminLogin.value = false
      _userMessage.value = "እንኳን ወደ አስተዳዳሪ ክፍል በደህና መጡ (Admin Authenticated)"
      return true
    }
    return false
  }

  fun logoutAdmin() {
    repository.setAdminLoggedIn(false)
    _userMessage.value = "ከአስተዳዳሪ ክፍል ወጥተዋል (Logged out)"
  }

  fun verifyPendingDonation(donationId: String) {
    repository.verifyDonation(donationId)
    _userMessage.value = "ልገሳው ተረጋግጦ ጸድቋል (Donation Confirmed)"
  }

  fun openNewProjectDialog() {
    _showNewProjectDialog.value = true
  }

  fun closeNewProjectDialog() {
    _showNewProjectDialog.value = false
  }

  fun createProject(
    titleAm: String,
    titleEn: String,
    category: DonationCategory,
    descriptionAm: String,
    descriptionEn: String,
    locationAm: String,
    locationEn: String,
    targetAmount: Double,
    beneficiaries: Int
  ) {
    repository.createProject(
      titleAm = titleAm,
      titleEn = titleEn,
      category = category,
      descriptionAm = descriptionAm,
      descriptionEn = descriptionEn,
      locationAm = locationAm,
      locationEn = locationEn,
      targetAmount = targetAmount,
      beneficiaries = beneficiaries
    )
    _showNewProjectDialog.value = false
    _userMessage.value = "አዲስ ፕሮጀክት በተሳካ ሁኔታ ተመዝግቧል"
  }

  fun openNewExpenseDialog() {
    _showNewExpenseDialog.value = true
  }

  fun closeNewExpenseDialog() {
    _showNewExpenseDialog.value = false
  }

  fun recordExpense(
    projectId: String,
    titleAm: String,
    titleEn: String,
    amount: Double,
    vendor: String,
    receiptRef: String
  ) {
    repository.addExpense(
      projectId = projectId,
      titleAm = titleAm,
      titleEn = titleEn,
      amount = amount,
      vendor = vendor,
      receiptRef = receiptRef
    )
    _showNewExpenseDialog.value = false
    _userMessage.value = "ህጋዊ ወጪው በስኬት ተመዝግቧል"
  }
}
