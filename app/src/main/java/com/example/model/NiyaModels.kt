package com.example.model

enum class Language(val code: String, val displayName: String) {
  AMHARIC("am", "አማርኛ"),
  ENGLISH("en", "English"),
  ARABIC("ar", "العربية")
}

enum class DonationCategory(val id: String) {
  MOSQUE("mosque"),
  POOR_RELIEF("poor_relief"),
  GRAVEYARD("graveyard"),
  ISLAMIC_SCHOOL("islamic_school"),
  HEALTH_CENTER("health_center")
}

enum class NiyyahType(val id: String) {
  SADAQAH("sadaqah"),
  SADAQAH_JARIYAH("sadaqah_jariyah"),
  ZAKAT("zakat"),
  GENERAL_CHARITY("general")
}

enum class PaymentProvider(
  val id: String,
  val nameAm: String,
  val nameEn: String,
  val accountNumber: String,
  val accountHolder: String,
  val instructionAm: String,
  val instructionEn: String,
  val colorHex: Long
) {
  TELEBIRR(
    id = "telebirr",
    nameAm = "ቴሌብር (Telebirr)",
    nameEn = "Telebirr",
    accountNumber = "829104",
    accountHolder = "NIYA Charity Ethiopia (የነጋዴ ቁጥር)",
    instructionAm = "በቴሌብር መተግበሪያ ወደ 'ክፍያ' (Pay Merchant) በመሄድ የነጋዴ ቁጥር 829104 ያስገቡ። ክፍያውን ከፈጸሙ በኋላ የደረሰኝ ቁጥር (Transaction ID) እዚህ ያስገቡ።",
    instructionEn = "Open Telebirr app, select Pay Merchant, enter Merchant Code 829104. Enter your payment transaction code below.",
    colorHex = 0xFF007ECC
  ),
  CBE(
    id = "cbe",
    nameAm = "የኢትዮጵያ ንግድ ባንክ (CBE)",
    nameEn = "Commercial Bank of Ethiopia (CBE)",
    accountNumber = "1000456789123",
    accountHolder = "NIYA Islamic Charity Foundation",
    instructionAm = "በ CBE Birr ወይም በሞባይል ባንኪንግ ወደ ሒሳብ ቁጥር 1000456789123 ያስተላልፉ። ከዛም የዝውውር ማረጋገጫ ቁጥሩን እዚህ ያስገቡ።",
    instructionEn = "Transfer to CBE Account 1000456789123 via CBE Birr or CBE Mobile Banking. Enter transfer reference number.",
    colorHex = 0xFF6D28D9
  ),
  AWASH_BANK(
    id = "awash",
    nameAm = "አዋሽ ባንክ (Awash Bank)",
    nameEn = "Awash Bank",
    accountNumber = "01320491823000",
    accountHolder = "NIYA Sadaqah Fund",
    instructionAm = "በአዋሽ ባንክ ሞባይል መተግበሪያ ወደ ሒሳብ 01320491823000 ያስተላልፉ። የማረጋገጫ መልእክቱን ቁጥር ያስገቡ።",
    instructionEn = "Transfer to Awash Bank account 01320491823000 via mobile app. Submit confirmation reference.",
    colorHex = 0xFF1E40AF
  ),
  DASHEN_BANK(
    id = "dashen",
    nameAm = "ዳሸን ባንክ / አሞሌ (Dashen Bank)",
    nameEn = "Dashen Bank / Amole",
    accountNumber = "5241098231011",
    accountHolder = "NIYA Charity Organization",
    instructionAm = "በዳሸን ባንክ ወይም በአሞሌ ወደ ሒሳብ ቁጥር 5241098231011 ያስተላልፉ እና የማረጋገጫ ቁጥሩን ይመዝግቡ።",
    instructionEn = "Transfer to Dashen account 5241098231011 via Amole or Dashen Mobile. Submit reference.",
    colorHex = 0xFF047857
  ),
  BANK_TRANSFER(
    id = "bank_transfer",
    nameAm = "ሌላ ባንክ ዝውውር (Bank Transfer)",
    nameEn = "Direct Bank Transfer",
    accountNumber = "1000456789123 (CBE) / 01320491823000 (Awash)",
    accountHolder = "NIYA Islamic Charity",
    instructionAm = "በማንኛውም የኢትዮጵያ ባንክ ሒሳብ ዝውውር ማድረግ ይችላሉ። የደረሰኝ ፎቶ ወይም የዝውውር ቁጥር ያስገቡ።",
    instructionEn = "Direct bank transfer from any Ethiopian commercial bank. Record transfer reference.",
    colorHex = 0xFFB45309
  )
}

enum class ProjectStatus {
  ACTIVE,
  IN_PROGRESS,
  COMPLETED
}

enum class DonationStatus {
  CONFIRMED,
  PENDING_VERIFICATION
}

data class CharityProject(
  val id: String,
  val titleAm: String,
  val titleEn: String,
  val titleAr: String,
  val category: DonationCategory,
  val descriptionAm: String,
  val descriptionEn: String,
  val locationAm: String,
  val locationEn: String,
  val targetAmount: Double,
  val raisedAmount: Double,
  val status: ProjectStatus,
  val beneficiaryCount: Int,
  val completionPercentage: Int = ((raisedAmount / targetAmount.coerceAtLeast(1.0)) * 100).toInt().coerceIn(0, 100),
  val verifiedExpenses: Double = raisedAmount * 0.72
)

data class Donation(
  val id: String,
  val projectId: String?,
  val projectNameAm: String,
  val projectNameEn: String,
  val category: DonationCategory,
  val amount: Double,
  val donorName: String,
  val isAnonymous: Boolean,
  val donorPhone: String = "",
  val niyyah: NiyyahType,
  val intentionNote: String = "",
  val paymentProvider: PaymentProvider,
  val paymentReference: String,
  val status: DonationStatus,
  val timestampFormatted: String,
  val epochMillis: Long = System.currentTimeMillis()
)

data class ExpenseItem(
  val id: String,
  val projectId: String,
  val projectNameAm: String,
  val titleAm: String,
  val titleEn: String,
  val amount: Double,
  val dateFormatted: String,
  val receiptNumber: String,
  val vendorOrContractor: String,
  val approvedBy: String = "የአስተዳደር ቦርድ (Board of Trustees)"
)

data class AdminAuditLog(
  val id: String,
  val actionAm: String,
  val actionEn: String,
  val adminName: String,
  val timestampFormatted: String
)

data class IslamicQuote(
  val arabic: String,
  val amharic: String,
  val english: String,
  val source: String
)
