package com.example.data

import com.example.model.DonationCategory
import com.example.model.Language
import com.example.model.NiyyahType

object NiyaStrings {

  fun getCategoryName(category: DonationCategory, lang: Language): String = when (lang) {
    Language.AMHARIC -> when (category) {
      DonationCategory.MOSQUE -> "የመስጊድ ግንባታ"
      DonationCategory.POOR_RELIEF -> "የድሆች ድጋፍ"
      DonationCategory.GRAVEYARD -> "የመቃብር ስፍራ ግንባታ"
      DonationCategory.ISLAMIC_SCHOOL -> "የእስላማዊ ትምህርት ቤት"
      DonationCategory.HEALTH_CENTER -> "የጤና ጣቢያ ግንባታ"
    }
    Language.ENGLISH -> when (category) {
      DonationCategory.MOSQUE -> "Mosque Construction"
      DonationCategory.POOR_RELIEF -> "Support Poor People"
      DonationCategory.GRAVEYARD -> "Graveyard Development"
      DonationCategory.ISLAMIC_SCHOOL -> "Islamic School Construction"
      DonationCategory.HEALTH_CENTER -> "Health Center Construction"
    }
    Language.ARABIC -> when (category) {
      DonationCategory.MOSQUE -> "بناء وتعمير المساجد"
      DonationCategory.POOR_RELIEF -> "إغاثة ودعم الفقراء"
      DonationCategory.GRAVEYARD -> "تطوير مقابر المسلمين"
      DonationCategory.ISLAMIC_SCHOOL -> "بناء المدارس الإسلامية"
      DonationCategory.HEALTH_CENTER -> "إنشاء المراكز الصحية"
    }
  }

  fun getCategoryDesc(category: DonationCategory, lang: Language): String = when (lang) {
    Language.AMHARIC -> when (category) {
      DonationCategory.MOSQUE -> "አዳዲስ መስጊዶችን መገንባት፣ የነበሩትን ማደስ እና የግንባታ ቁሳቁሶችን ማሟላት።"
      DonationCategory.POOR_RELIEF -> "የምግብ ድጋፍ፣ ለችግረኛ ቤተሰቦችና የቲሞች የገንዘብ ድጋፍ፣ ለአረጋውያንና አቅመ ደካሞች ድጋፍ።"
      DonationCategory.GRAVEYARD -> "የሙስሊም መቃብር ቦታዎችን ማዘጋጀት፣ አጥር ማጠር፣ መገልገያዎችንና የክብር ቀብር አገልግሎት ማሟላት።"
      DonationCategory.ISLAMIC_SCHOOL -> "የመድረሳ እና የእስላማዊ ትምህርት ቤቶች ግንባታ፣ የቁርኣን መጻሕፍትና የትምህርት ቁሳቁሶች ማቅረብ።"
      DonationCategory.HEALTH_CENTER -> "የማህበረሰብ ጤና ጣቢያዎችን መገንባት፣ የህክምና መሣሪያዎችን ማቅረብና ለህክምና አቅም የሌላቸውን መርዳት።"
    }
    Language.ENGLISH -> when (category) {
      DonationCategory.MOSQUE -> "Build new mosques, renovate historic houses of worship, and fund durable construction materials."
      DonationCategory.POOR_RELIEF -> "Emergency food baskets, financial aid for vulnerable families, orphan care and widow support."
      DonationCategory.GRAVEYARD -> "Secure and fence Muslim cemeteries, establish dignified washing and burial facilities."
      DonationCategory.ISLAMIC_SCHOOL -> "Construct Islamic schools and madrasas, provide Qurans and fund scholarships for needy students."
      DonationCategory.HEALTH_CENTER -> "Build community clinics, purchase medical equipment, and sponsor medical treatments for patients."
    }
    Language.ARABIC -> when (category) {
      DonationCategory.MOSQUE -> "بناء وتشييد المساجد وترميم بيوت الله وتوفير مواد البناء اللازمة."
      DonationCategory.POOR_RELIEF -> "السلال الغذائية، كفالة الأيتام والأرامل، ومساعدة العائلات المحتاجة في إثيوبيا."
      DonationCategory.GRAVEYARD -> "تهيئة مقابر المسلمين وتسويرها وتوفير مرافق الدفن الشرعي الكريم."
      DonationCategory.ISLAMIC_SCHOOL -> "بناء وتطوير المدارس الشرعية والمراكز القرآنية ودعم الطلبة المحتاجين."
      DonationCategory.HEALTH_CENTER -> "إنشاء العيادات والمراكز الصحية وتوفير الأجهزة والمعدات الطبية ورعاية المرضى."
    }
  }

  fun getNiyyahTitle(niyyah: NiyyahType, lang: Language): String = when (lang) {
    Language.AMHARIC -> when (niyyah) {
      NiyyahType.SADAQAH -> "ሰደቃ (የፍቃደኝነት ልገሳ)"
      NiyyahType.SADAQAH_JARIYAH -> "ሰደቃ ጃሪያ (ቀጣይነት ያለው ምንዳ)"
      NiyyahType.ZAKAT -> "ዘካህ (የግዴታ ምጽዋት)"
      NiyyahType.GENERAL_CHARITY -> "አጠቃላይ በጎ አድራጎት"
    }
    Language.ENGLISH -> when (niyyah) {
      NiyyahType.SADAQAH -> "Sadaqah (Voluntary Charity)"
      NiyyahType.SADAQAH_JARIYAH -> "Sadaqah Jariyah (Ongoing Reward)"
      NiyyahType.ZAKAT -> "Zakat (Obligatory Charity)"
      NiyyahType.GENERAL_CHARITY -> "General Good Cause"
    }
    Language.ARABIC -> when (niyyah) {
      NiyyahType.SADAQAH -> "صدقة تطوعية"
      NiyyahType.SADAQAH_JARIYAH -> "صدقة جارية (أجر مستمر)"
      NiyyahType.ZAKAT -> "زكاة المال المفروضة"
      NiyyahType.GENERAL_CHARITY -> "أعمال البر العامة"
    }
  }

  fun getNiyyahExplanation(niyyah: NiyyahType, lang: Language): String = when (lang) {
    Language.AMHARIC -> when (niyyah) {
      NiyyahType.SADAQAH -> "በማንኛውም ጊዜ ለተቸገሩ ሰዎች የሚሰጥ የፍቃደኝነት መልካም ሥራ ነው።"
      NiyyahType.SADAQAH_JARIYAH -> "መስጊድ፣ መድረሳ ወይም መቃብር የመሳሰሉ ከሞት በኋላም ምንዳቸው የማይቋረጥ ዘላቂ የበጎ ሥራዎች ናቸው።"
      NiyyahType.ZAKAT -> "ኒሳብ የደረሰ ሀብት ላይ የሚከፈል ግዴታ ሲሆን ለቁርኣኑ 8 ተጠቃሚዎች ብቻ በጥብቅ ይውላል።"
      NiyyahType.GENERAL_CHARITY -> "ለማህበረሰብ አገልግሎት የሚውል አጠቃላይ የበጎ አድራጎት ልገሳ።"
    }
    Language.ENGLISH -> when (niyyah) {
      NiyyahType.SADAQAH -> "Voluntary charity given out of love and devotion at any time for vulnerable people."
      NiyyahType.SADAQAH_JARIYAH -> "Continuous ongoing charity (mosques, water, education) whose reward continues even after death."
      NiyyahType.ZAKAT -> "Obligatory 2.5% wealth pillar directed strictly to the 8 Quranic categories of beneficiaries."
      NiyyahType.GENERAL_CHARITY -> "Flexible donation dedicated to urgent community welfare needs."
    }
    Language.ARABIC -> when (niyyah) {
      NiyyahType.SADAQAH -> "صدقة تطوعية تدفع ابتغاء وجه الله في أي وقت لتفريج كرب المحتاجين."
      NiyyahType.SADAQAH_JARIYAH -> "صدقة يمتد أجرها وثوابها حتى بعد الممات، كبناء المساجد والمراكز العلمية."
      NiyyahType.ZAKAT -> "فريضة ركن الإسلام للمستحقين الثمانية المنصوص عليهم في كتاب الله."
      NiyyahType.GENERAL_CHARITY -> "مساهمة خيرية عامة لمشاريع النفع العام."
    }
  }

  // Navigation labels
  fun navHome(lang: Language) = when (lang) {
    Language.AMHARIC -> "መነሻ"
    Language.ENGLISH -> "Home"
    Language.ARABIC -> "الرئيسية"
  }
  fun navProjects(lang: Language) = when (lang) {
    Language.AMHARIC -> "ፕሮጀክቶች"
    Language.ENGLISH -> "Projects"
    Language.ARABIC -> "المشاريع"
  }
  fun navDonate(lang: Language) = when (lang) {
    Language.AMHARIC -> "ሰደቃ አድርግ"
    Language.ENGLISH -> "Donate"
    Language.ARABIC -> "تبرع الآن"
  }
  fun navTransparency(lang: Language) = when (lang) {
    Language.AMHARIC -> "ሪፖርት"
    Language.ENGLISH -> "Transparency"
    Language.ARABIC -> "الشفافية"
  }
  fun navProfile(lang: Language) = when (lang) {
    Language.AMHARIC -> "መገለጫ"
    Language.ENGLISH -> "Profile"
    Language.ARABIC -> "حسابي"
  }

  // General texts
  fun welcomeTitle(lang: Language) = when (lang) {
    Language.AMHARIC -> "እንኳን ወደ NIYA በደህና መጡ"
    Language.ENGLISH -> "Welcome to NIYA Charity"
    Language.ARABIC -> "أهلاً بكم في منصة نِية الخيرية"
  }

  fun welcomeSubtitle(lang: Language) = when (lang) {
    Language.AMHARIC -> "የኢትዮጵያ እስላማዊ የበጎ አድራጎት እና የሰደቃ ማሰባሰቢያ መድረክ"
    Language.ENGLISH -> "Islamic Sadaqah & Community Development Platform for Ethiopia"
    Language.ARABIC -> "منصة الصدقات والتنمية الإسلامية للمسلمين في إثيوبيا"
  }

  fun bismillah(lang: Language) = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"

  fun donateNow(lang: Language) = when (lang) {
    Language.AMHARIC -> "ሰደቃ አድርግ"
    Language.ENGLISH -> "Give Sadaqah"
    Language.ARABIC -> "تصدق الآن"
  }

  fun currencySymbol() = "ETB (ብር)"
}
