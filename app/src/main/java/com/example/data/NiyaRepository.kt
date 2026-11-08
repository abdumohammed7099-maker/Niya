package com.example.data

import com.example.model.AdminAuditLog
import com.example.model.CharityProject
import com.example.model.Donation
import com.example.model.DonationCategory
import com.example.model.DonationStatus
import com.example.model.ExpenseItem
import com.example.model.IslamicQuote
import com.example.model.Language
import com.example.model.NiyyahType
import com.example.model.PaymentProvider
import com.example.model.ProjectStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class NiyaRepository {

  private val _projects = MutableStateFlow<List<CharityProject>>(emptyList())
  val projects: StateFlow<List<CharityProject>> = _projects.asStateFlow()

  private val _donations = MutableStateFlow<List<Donation>>(emptyList())
  val donations: StateFlow<List<Donation>> = _donations.asStateFlow()

  private val _expenses = MutableStateFlow<List<ExpenseItem>>(emptyList())
  val expenses: StateFlow<List<ExpenseItem>> = _expenses.asStateFlow()

  private val _auditLogs = MutableStateFlow<List<AdminAuditLog>>(emptyList())
  val auditLogs: StateFlow<List<AdminAuditLog>> = _auditLogs.asStateFlow()

  private val _selectedLanguage = MutableStateFlow(Language.AMHARIC)
  val selectedLanguage: StateFlow<Language> = _selectedLanguage.asStateFlow()

  private val _isAdminLoggedIn = MutableStateFlow(false)
  val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

  val islamicQuotes = listOf(
    IslamicQuote(
      arabic = "مَّثَلُ الَّذِينَ يُنفِقُونَ أَمْوَالَهُمْ فِي سَبِيلِ اللَّهِ كَمَثَلِ حَبَّةٍ أَنبَتَتْ سَبْعَ سَنَابِلَ فِي كُلِّ سُنبُلَةٍ مِّائَةُ حَبَّةٍ",
      amharic = "የእነዚያ ገንዘቦቻቸውን በአላህ መንገድ የሚለግሱ ሰዎች ምሳሌ እንደ አንዲት ቅንጣት ናት፤ ሰባት ዘለላዎችን ያበቀለች በየዘለላውም ሁሉ መቶ ቅንጣት እንዳለባት። (አል-በቀራህ: 261)",
      english = "The example of those who spend their wealth in the way of Allah is like a seed of grain which grows seven spikes; in each spike is a hundred grains. (Al-Baqarah: 261)",
      source = "ሱረቱል አል-በቀራህ: 261"
    ),
    IslamicQuote(
      arabic = "مَا نَقَصَتْ صَدَقَةٌ مِنْ مَالٍ",
      amharic = "የአላህ መልእክተኛ (ሰ.ዐ.ወ) እንዲህ ብለዋል፡ 'ሰደቃ ሀብትን አታጎድልም፤ ይልቁንም አላህ ይባርከዋል።'",
      english = "The Messenger of Allah (ﷺ) said: 'Charity does not decrease wealth.'",
      source = "ሶሒሕ ሙስሊም 2588"
    ),
    IslamicQuote(
      arabic = "إِذَا مَاتَ ابْنُ آدَمَ انْقَطَعَ عَمَلُهُ إِلا مِنْ ثَلاثٍ: صَدَقَةٍ جَارِيَةٍ، أَوْ عِلْمٍ يُنْتَفَعُ بِهِ، أَوْ وَلَدٍ صَالِحٍ يَدْعُو لَهُ",
      amharic = "የሰው ልጅ ሲሞት ሥራው ሁሉ ይቋረጣል ከሦስት በስተቀር፡ ቀጣይነት ያለው ሰደቃ (ሰደቃ ጃሪያ)፣ የሚጠቅም እውቀት፣ ወይም ዱዓ የሚያደርግለት መልካም ልጅ።",
      english = "When a person dies, all their deeds end except three: continuous charity (Sadaqah Jariyah), beneficial knowledge, or a righteous child who prays for them.",
      source = "ሶሒሕ ሙስሊም 1631"
    )
  )

  init {
    seedInitialData()
  }

  fun setLanguage(language: Language) {
    _selectedLanguage.value = language
  }

  fun setAdminLoggedIn(loggedIn: Boolean) {
    _isAdminLoggedIn.value = loggedIn
  }

  fun addDonation(
    projectId: String?,
    projectNameAm: String,
    projectNameEn: String,
    category: DonationCategory,
    amount: Double,
    donorName: String,
    isAnonymous: Boolean,
    donorPhone: String,
    niyyah: NiyyahType,
    intentionNote: String,
    paymentProvider: PaymentProvider,
    paymentReference: String
  ): Donation {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val refCode = if (paymentReference.isNotBlank()) paymentReference.trim()
                  else "NY-${(100000..999999).random()}"

    val newDonation = Donation(
      id = "TXN-${UUID.randomUUID().toString().take(8).uppercase()}",
      projectId = projectId,
      projectNameAm = projectNameAm,
      projectNameEn = projectNameEn,
      category = category,
      amount = amount,
      donorName = if (isAnonymous || donorName.isBlank()) "ስውር ሰደቃ (ሚስጥራዊ)" else donorName.trim(),
      isAnonymous = isAnonymous,
      donorPhone = donorPhone,
      niyyah = niyyah,
      intentionNote = intentionNote,
      paymentProvider = paymentProvider,
      paymentReference = refCode,
      status = if (paymentReference.isNotBlank()) DonationStatus.CONFIRMED else DonationStatus.PENDING_VERIFICATION,
      timestampFormatted = dateFormat.format(Date()),
      epochMillis = System.currentTimeMillis()
    )

    _donations.value = listOf(newDonation) + _donations.value

    // If donation was for a specific project, update the raised amount
    if (projectId != null && newDonation.status == DonationStatus.CONFIRMED) {
      _projects.value = _projects.value.map { project ->
        if (project.id == projectId) {
          val newRaised = project.raisedAmount + amount
          val newPercentage = ((newRaised / project.targetAmount) * 100).toInt().coerceIn(0, 100)
          project.copy(
            raisedAmount = newRaised,
            completionPercentage = newPercentage,
            status = if (newRaised >= project.targetAmount) ProjectStatus.COMPLETED else ProjectStatus.IN_PROGRESS
          )
        } else {
          project
        }
      }
    }

    addAuditLog(
      actionAm = "አዲስ ሰደቃ ተመዝግቧል: ${amount.toInt()} ብር ለ${projectNameAm} በ${paymentProvider.nameEn}",
      actionEn = "New donation recorded: ${amount.toInt()} ETB for $projectNameEn via ${paymentProvider.nameEn}",
      admin = "ስርዓተ-ክፍያ (Payment Gateway)"
    )

    return newDonation
  }

  fun verifyDonation(donationId: String) {
    _donations.value = _donations.value.map { donation ->
      if (donation.id == donationId) {
        // Also update project if not already added
        if (donation.projectId != null && donation.status != DonationStatus.CONFIRMED) {
          _projects.value = _projects.value.map { p ->
            if (p.id == donation.projectId) {
              val newRaised = p.raisedAmount + donation.amount
              p.copy(
                raisedAmount = newRaised,
                completionPercentage = ((newRaised / p.targetAmount) * 100).toInt().coerceIn(0, 100)
              )
            } else p
          }
        }
        donation.copy(status = DonationStatus.CONFIRMED)
      } else donation
    }

    addAuditLog(
      actionAm = "የባንክ ዝውውር ተረጋግጦ ጸድቋል: $donationId",
      actionEn = "Payment transfer verified and approved: $donationId",
      admin = "ኦዲተር አህመድ"
    )
  }

  fun addExpense(
    projectId: String,
    titleAm: String,
    titleEn: String,
    amount: Double,
    vendor: String,
    receiptRef: String
  ) {
    val project = _projects.value.find { it.id == projectId }
    val projNameAm = project?.titleAm ?: "አጠቃላይ የበጎ አድራጎት ፈንድ"

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val expense = ExpenseItem(
      id = "EXP-${UUID.randomUUID().toString().take(6).uppercase()}",
      projectId = projectId,
      projectNameAm = projNameAm,
      titleAm = titleAm,
      titleEn = titleEn,
      amount = amount,
      dateFormatted = dateFormat.format(Date()),
      receiptNumber = receiptRef,
      vendorOrContractor = vendor,
      approvedBy = "የግምጃ ቤትና ኦዲት ኮሚቴ (Audit Committee)"
    )

    _expenses.value = listOf(expense) + _expenses.value

    // Update verified expenses on project
    if (project != null) {
      _projects.value = _projects.value.map { p ->
        if (p.id == projectId) {
          p.copy(verifiedExpenses = p.verifiedExpenses + amount)
        } else p
      }
    }

    addAuditLog(
      actionAm = "ህጋዊ ወጪ ተመዝግቧል: ${amount.toInt()} ብር ለ${titleAm} (ደረሰኝ: $receiptRef)",
      actionEn = "Approved expenditure: ${amount.toInt()} ETB for $titleEn (Ref: $receiptRef)",
      admin = "የፋይናንስ አስተዳዳሪ"
    )
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
    val newProject = CharityProject(
      id = "PRJ-${UUID.randomUUID().toString().take(6).uppercase()}",
      titleAm = titleAm,
      titleEn = titleEn,
      titleAr = titleEn,
      category = category,
      descriptionAm = descriptionAm,
      descriptionEn = descriptionEn,
      locationAm = locationAm,
      locationEn = locationEn,
      targetAmount = targetAmount,
      raisedAmount = 0.0,
      status = ProjectStatus.ACTIVE,
      beneficiaryCount = beneficiaries,
      completionPercentage = 0,
      verifiedExpenses = 0.0
    )

    _projects.value = listOf(newProject) + _projects.value

    addAuditLog(
      actionAm = "አዲስ ፕሮጀክት ተከፍቷል: $titleAm (ዒላማ: ${targetAmount.toInt()} ብር)",
      actionEn = "New project created: $titleEn (Target: ${targetAmount.toInt()} ETB)",
      admin = "የቦርድ ሰብሳቢ"
    )
  }

  private fun addAuditLog(actionAm: String, actionEn: String, admin: String) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val log = AdminAuditLog(
      id = "LOG-${(1000..9999).random()}",
      actionAm = actionAm,
      actionEn = actionEn,
      adminName = admin,
      timestampFormatted = dateFormat.format(Date())
    )
    _auditLogs.value = listOf(log) + _auditLogs.value
  }

  private fun seedInitialData() {
    _projects.value = listOf(
      // 1. የመስጊድ ግንባታ
      CharityProject(
        id = "PRJ-MSQ-01",
        titleAm = "የአንዋር ታላቁ መስጊድ ተጓዳኝ ማዕከል ማስፋፊያ",
        titleEn = "Grand Anwar Mosque Annex & Ablution Facility Expansion",
        titleAr = "توسعة وتطوير مرافق المسجد الكبير (أنور)",
        category = DonationCategory.MOSQUE,
        descriptionAm = "በአዲስ አበባ መርካቶ የሚገኘውን ጥንታዊውንና ታላቁን አንዋር መስጊድ የውዱእ ቦታዎችን፣ የወጣቶች ማዕከልንና የመጽሐፍት ቤቱን ማደስና ማስፋፋት።",
        descriptionEn = "Expansion of ablution centers, Islamic library, and community youth halls for the historic Grand Anwar Mosque in Addis Ababa.",
        locationAm = "አዲስ አበባ፣ መርካቶ",
        locationEn = "Addis Ababa, Merkato",
        targetAmount = 4500000.0,
        raisedAmount = 3650000.0,
        status = ProjectStatus.IN_PROGRESS,
        beneficiaryCount = 25000,
        verifiedExpenses = 2890000.0
      ),
      CharityProject(
        id = "PRJ-MSQ-02",
        titleAm = "የቢላል መስጊድ አዲስ ግንባታ - ጎንደር",
        titleEn = "Bilal Mosque New Construction - Gondar",
        titleAr = "بناء مسجد بلال بن رباح الجديد في غوندر",
        category = DonationCategory.MOSQUE,
        descriptionAm = "በጎንደር ከተማ አቅራቢያ የሚኖሩ 4,000 ሙስሊሞች የሚሰግዱበት አዲስ የመስጊድ ህንጻ፣ ሚናራና የውሃ ጉድጓድ ቁፋሮ ግንባታ።",
        descriptionEn = "New mosque construction, minaret, and deep water well serving over 4,000 Muslims in Gondar region.",
        locationAm = "ጎንደር፣ አማራ ክልል",
        locationEn = "Gondar, Amhara Region",
        targetAmount = 3200000.0,
        raisedAmount = 2100000.0,
        status = ProjectStatus.ACTIVE,
        beneficiaryCount = 4200,
        verifiedExpenses = 1450000.0
      ),

      // 2. የድሆች ድጋፍ
      CharityProject(
        id = "PRJ-POR-01",
        titleAm = "የችግረኛ ቤተሰቦችና የየቲሞች ወርሃዊ የምግብ ስንቅ",
        titleEn = "Monthly Food & Sustenance Baskets for 500 Needy Families",
        titleAr = "سلال غذائية وكفالات شهرية لـ 500 عائلة متعففة وأيتام",
        category = DonationCategory.POOR_RELIEF,
        descriptionAm = "ለአቅመ ደካማ እናቶች፣ ወላጅ አልባ ሕፃናት (የቲሞች) እና አረጋውያን ጤፍ፣ ዘይት፣ ዱቄት፣ ስኳር እና አልሚ ምግቦች ድጋፍ መስጠት።",
        descriptionEn = "Providing teff, cooking oil, flour, and urgent nutritional food supplies to vulnerable widows, orphans, and indigent households.",
        locationAm = "አዲስ አበባ እና ዙሪያዋ",
        locationEn = "Addis Ababa & Surrounding Areas",
        targetAmount = 1800000.0,
        raisedAmount = 1520000.0,
        status = ProjectStatus.ACTIVE,
        beneficiaryCount = 2100,
        verifiedExpenses = 1200000.0
      ),
      CharityProject(
        id = "PRJ-POR-02",
        titleAm = "የአደጋ ጊዜ ድንገተኛ ድጋፍ - አፋር እና ሶማሌ ክልል",
        titleEn = "Emergency Humanitarian Aid - Afar & Somali Regions",
        titleAr = "إغاثة إنسانية عاجلة لمتضرري الجفاف في إقليمي عفار والصومال",
        category = DonationCategory.POOR_RELIEF,
        descriptionAm = "በድርቅና በተፈጥሮ አደጋ ለተጎዱ ወገኖች ንጹህ የመጠጥ ውሃ ቦቴ ማቅረብ፣ የህክምና ድጋፍና አስቸኳይ የምግብ አቅርቦት።",
        descriptionEn = "Emergency clean water trucking, urgent medical relief, and critical food supplies for communities impacted by severe climate drought.",
        locationAm = "አፋር እና ሶማሌ ክልል",
        locationEn = "Afar and Somali Regions",
        targetAmount = 2500000.0,
        raisedAmount = 2500000.0,
        status = ProjectStatus.COMPLETED,
        beneficiaryCount = 8500,
        verifiedExpenses = 2480000.0
      ),

      // 3. የመቃብር ስፍራ ግንባታ
      CharityProject(
        id = "PRJ-GRV-01",
        titleAm = "የሱሉልታ ሙስሊሞች መቃብር ስፍራ ማጠርና መንገድ ዝርጋታ",
        titleEn = "Sululta Muslim Cemetery Fencing & Perimeter Protection",
        titleAr = "تسوير وتأهيل مقبرة المسلمين في سولوتا ورصف الطرق",
        category = DonationCategory.GRAVEYARD,
        descriptionAm = "የሙስሊም መቃብር ቦታውን ክብር ለመጠበቅ ዙሪያውን በድንጋይና ብረት ማጠር፣ የውስጥ መንገድ ማዘጋጀትና የክብር ቀብር መገልገያ ማዕከል ማቋቋም።",
        descriptionEn = "Securing perimeter fencing, cobblestone internal pathways, and dignified burial infrastructure for the Muslim cemetery in Sululta.",
        locationAm = "ሱሉልታ፣ ኦሮሚያ ክልል",
        locationEn = "Sululta, Oromia Region",
        targetAmount = 2100000.0,
        raisedAmount = 1420000.0,
        status = ProjectStatus.IN_PROGRESS,
        beneficiaryCount = 50000,
        verifiedExpenses = 980000.0
      ),

      // 4. የእስላማዊ ትምህርት ቤት
      CharityProject(
        id = "PRJ-SCH-01",
        titleAm = "የነጃሺ መድረሳ እና የቁርኣን ሂፍዝ ማዕከል ግንባታ",
        titleEn = "Al-Negashi Madrasa & Quran Memorization Institute",
        titleAr = "بناء معهد النجاشي لتحفيظ القرآن الكريم والعلوم الشرعية",
        category = DonationCategory.ISLAMIC_SCHOOL,
        descriptionAm = "በታሪካዊው ነጃሺ 400 ተማሪዎችን የሚያስተናግድ የመማሪያ ክፍሎች፣ የመኝታ ህንጻ፣ የመጽሐፍት ቤትና የኮምፒውተር ላብራቶሪ ግንባታ።",
        descriptionEn = "Building 8 classrooms, dormitories, library, and modern educational facilities at the historic site of Negash for 400 students.",
        locationAm = "ውቅሮ፣ ነጃሺ",
        locationEn = "Wukro, Negash",
        targetAmount = 3800000.0,
        raisedAmount = 2950000.0,
        status = ProjectStatus.IN_PROGRESS,
        beneficiaryCount = 400,
        verifiedExpenses = 2100000.0
      ),

      // 5. የጤና ጣቢያ ግንባታ
      CharityProject(
        id = "PRJ-HLT-01",
        titleAm = "የአል-ሺፋ ማህበረሰብ ጤና ጣቢያ እና የእናቶች ማቆያ",
        titleEn = "Al-Shifa Maternal & Community Health Clinic - Jimma",
        titleAr = "إنشاء مركز الشفاء الصحي لرعاية الأمومة والطفولة في جيما",
        category = DonationCategory.HEALTH_CENTER,
        descriptionAm = "ለገጠር እናቶችና ሕፃናት የወሊድ ክትትል፣ የአልትራሳውንድ ምርመራና ነፃ የድንገተኛ ህክምና የሚሰጥ ማዕከል መገንባትና የህክምና መሣሪያዎችን ማሟላት።",
        descriptionEn = "Community health clinic with ultrasound diagnostics, maternity ward, and free medicines for low-income mothers and children in Jimma.",
        locationAm = "ጅማ፣ ኦሮሚያ ክልል",
        locationEn = "Jimma, Oromia Region",
        targetAmount = 4200000.0,
        raisedAmount = 3100000.0,
        status = ProjectStatus.IN_PROGRESS,
        beneficiaryCount = 12000,
        verifiedExpenses = 2300000.0
      )
    )

    _donations.value = listOf(
      Donation(
        id = "TXN-8291041",
        projectId = "PRJ-MSQ-01",
        projectNameAm = "የአንዋር ታላቁ መስጊድ ተጓዳኝ ማዕከል ማስፋፊያ",
        projectNameEn = "Grand Anwar Mosque Annex",
        category = DonationCategory.MOSQUE,
        amount = 5000.0,
        donorName = "ስውር ሰደቃ (ሚስጥራዊ)",
        isAnonymous = true,
        donorPhone = "",
        niyyah = NiyyahType.SADAQAH_JARIYAH,
        intentionNote = "ለሞቱት ወላጆቼ ሰደቃ ጃሪያ ይሁንልኝ",
        paymentProvider = PaymentProvider.TELEBIRR,
        paymentReference = "TB2611089201",
        status = DonationStatus.CONFIRMED,
        timestampFormatted = "08/11/2026 14:20"
      ),
      Donation(
        id = "TXN-8291042",
        projectId = "PRJ-POR-01",
        projectNameAm = "የችግረኛ ቤተሰቦችና የየቲሞች ወርሃዊ የምግብ ስንቅ",
        projectNameEn = "Monthly Food & Sustenance Baskets",
        category = DonationCategory.POOR_RELIEF,
        amount = 1000.0,
        donorName = "ኡስታዝ ጀማል ኑሩ",
        isAnonymous = false,
        donorPhone = "0911****84",
        niyyah = NiyyahType.SADAQAH,
        intentionNote = "አላህ ለተቸገሩት ወገኖች በረካ ያድርግላቸው",
        paymentProvider = PaymentProvider.CBE,
        paymentReference = "FT2631298410",
        status = DonationStatus.CONFIRMED,
        timestampFormatted = "08/11/2026 13:45"
      ),
      Donation(
        id = "TXN-8291043",
        projectId = "PRJ-SCH-01",
        projectNameAm = "የነጃሺ መድረሳ እና የቁርኣን ሂፍዝ ማዕከል ግንባታ",
        projectNameEn = "Al-Negashi Madrasa & Quran Institute",
        category = DonationCategory.ISLAMIC_SCHOOL,
        amount = 2500.0,
        donorName = "ፋጢማ ሙሐመድ",
        isAnonymous = false,
        donorPhone = "0923****12",
        niyyah = NiyyahType.SADAQAH_JARIYAH,
        intentionNote = "ለቁርኣን ተማሪዎች ማበረታቻ",
        paymentProvider = PaymentProvider.AWASH_BANK,
        paymentReference = "AW99104812",
        status = DonationStatus.CONFIRMED,
        timestampFormatted = "08/11/2026 12:10"
      ),
      Donation(
        id = "TXN-8291044",
        projectId = "PRJ-GRV-01",
        projectNameAm = "የሱሉልታ ሙስሊሞች መቃብር ስፍራ ማጠርና መንገድ ዝርጋታ",
        projectNameEn = "Sululta Muslim Cemetery Fencing",
        category = DonationCategory.GRAVEYARD,
        amount = 500.0,
        donorName = "ስውር ሰደቃ",
        isAnonymous = true,
        donorPhone = "",
        niyyah = NiyyahType.SADAQAH_JARIYAH,
        intentionNote = "ለአላህ ውዴታ ሲባል",
        paymentProvider = PaymentProvider.TELEBIRR,
        paymentReference = "TB2611019488",
        status = DonationStatus.CONFIRMED,
        timestampFormatted = "07/11/2026 18:30"
      )
    )

    _expenses.value = listOf(
      ExpenseItem(
        id = "EXP-901",
        projectId = "PRJ-MSQ-01",
        projectNameAm = "የአንዋር ታላቁ መስጊድ ተጓዳኝ ማዕከል ማስፋፊያ",
        titleAm = "የሲሚንቶ፣ ብረትና የኮንክሪት ግዢ (500 ኩንታል)",
        titleEn = "Purchase of Structural Cement & Rebar",
        amount = 850000.0,
        dateFormatted = "01/11/2026",
        receiptNumber = "INV-MUG-8821",
        vendorOrContractor = "ሙገር ሲሚንቶ ፋብሪካ ወኪል"
      ),
      ExpenseItem(
        id = "EXP-902",
        projectId = "PRJ-POR-01",
        projectNameAm = "የችግረኛ ቤተሰቦችና የየቲሞች ወርሃዊ የምግብ ስንቅ",
        titleAm = "የማግኛ ጤፍና የአልሚ ዘይት ክፍፍል ግዢ",
        titleEn = "Direct Procurement of Magna Teff & Sunflower Oil",
        amount = 620000.0,
        dateFormatted = "28/10/2026",
        receiptNumber = "INV-FOD-1049",
        vendorOrContractor = "የሸማቾች ኅብረት ሥራ ማህበር"
      ),
      ExpenseItem(
        id = "EXP-903",
        projectId = "PRJ-SCH-01",
        projectNameAm = "የነጃሺ መድረሳ እና የቁርኣን ሂፍዝ ማዕከል ግንባታ",
        titleAm = "የጣሪያ ቆርቆሮና የእንጨት አጣና ግዢ",
        titleEn = "Roofing sheets and structural lumber",
        amount = 430000.0,
        dateFormatted = "22/10/2026",
        receiptNumber = "INV-NEG-0041",
        vendorOrContractor = "መቀሌ የብረታ ብረት ማዕከል"
      ),
      ExpenseItem(
        id = "EXP-904",
        projectId = "PRJ-HLT-01",
        projectNameAm = "የአል-ሺፋ ማህበረሰብ ጤና ጣቢያ እና የእናቶች ማቆያ",
        titleAm = "የአልትራሳውንድ መመርመሪያ ማሽን ግዢ ክፍያ",
        titleEn = "Ultrasound diagnostic system installation",
        amount = 740000.0,
        dateFormatted = "15/10/2026",
        receiptNumber = "INV-MED-7712",
        vendorOrContractor = "ሜድቴክ ኢትዮጵያ አ.ማ"
      )
    )

    _auditLogs.value = listOf(
      AdminAuditLog(
        id = "LOG-101",
        actionAm = "የሩብ ዓመት የፋይናንስ ኦዲት ሪፖርት ጸድቋል",
        actionEn = "Quarterly financial transparency audit approved",
        adminName = "የቦርድ ኦዲት ኮሚቴ",
        timestampFormatted = "05/11/2026 10:00"
      ),
      AdminAuditLog(
        id = "LOG-102",
        actionAm = "ለአል-ሺፋ ጤና ጣቢያ የህክምና መሣሪያ ወጪ ተረጋግጦ ተከፍሏል",
        actionEn = "Ultrasound equipment expense verified and released",
        adminName = "የሂሳብ ሹም ዑስማን",
        timestampFormatted = "01/11/2026 15:30"
      )
    )
  }
}
