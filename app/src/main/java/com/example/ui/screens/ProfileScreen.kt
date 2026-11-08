package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NiyaStrings
import com.example.model.Donation
import com.example.model.Language
import com.example.model.NiyyahType
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPastel
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPastel
import com.example.ui.theme.GoldPrimary

@Composable
fun ProfileScreen(
  selectedLanguage: Language,
  donations: List<Donation>,
  isAdmin: Boolean,
  onLanguageSelected: (Language) -> Unit,
  onDonationClick: (Donation) -> Unit,
  onAdminClick: () -> Unit
) {
  var showIslamicPrinciples by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .testTag("profile_screen"),
    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Profile Header
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(EmeraldPrimary),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
          }
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (selectedLanguage == Language.AMHARIC) "የአላህ ባሪያ (ውድ ለጋሽ)" else "Faithful Donor",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
              text = "NIYA ኢትዮጵያ • የበጎ አድራጊዎች አባል",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
            Text(
              text = "ጠቅላላ የሰደቃ ብዛት: ${donations.size}",
              style = MaterialTheme.typography.labelSmall.copy(color = EmeraldDark, fontWeight = FontWeight.SemiBold)
            )
          }

          if (isAdmin) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(GoldPastel)
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "አስተዳዳሪ",
                color = GoldDark,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
              )
            }
          }
        }
      }
    }

    // 2. Islamic Principles & Fiqh of Sadaqah Guide
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { showIslamicPrinciples = !showIslamicPrinciples },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(GoldPastel),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Mosque, contentDescription = null, tint = GoldDark, modifier = Modifier.size(20.dp))
              }
              Text(
                text = if (selectedLanguage == Language.AMHARIC) "የኢስላማዊ ሰደቃ እና ዘካህ መመሪያ" else "Islamic Principles: Sadaqah & Zakat",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
              )
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
          }

          if (showIslamicPrinciples) {
            Spacer(modifier = Modifier.height(12.dp))

            PrincipleItem(
              title = "1. ሰደቃ (Sadaqah - የፍቃደኝነት ልገሳ)",
              detail = "በማንኛውም ጊዜ፣ በማንኛውም የገንዘብ መጠን ለአላህ ውዴታ ብቻ ተብሎ ለተቸገሩ ሰዎች የሚሰጥ ልገሳ ነው። አላህ በደስታና በፍቅር የሚሰጡትን ይወዳል።"
            )
            Spacer(modifier = Modifier.height(8.dp))
            PrincipleItem(
              title = "2. ሰደቃ ጃሪያ (Sadaqah Jariyah - ቀጣይነት ያለው ምንዳ)",
              detail = "መስጊድ መገንባት፣ የቁርኣን መድረሳ ማቋቋም፣ የውሃ ጉድጓድ መቆፈር እና የመቃብር ስፍራ ማዘጋጀት የመሳሰሉ ሰው ከሞተ በኋላም ምንዳቸው የማይቋረጥ ዘላቂ የበጎ ሥራዎች ናቸው።"
            )
            Spacer(modifier = Modifier.height(8.dp))
            PrincipleItem(
              title = "3. ዘካህ (Zakat - የግዴታ ምጽዋት)",
              detail = "ኒሳብ የደረሰ ሀብት ላይ በአመት አንድ ጊዜ 2.5% የሚከፈል የእስልምና ማዕዘን ነው። በNIYA የሚደረጉ ልገሳዎች በለጋሹ ካልተገለጹ በስተቀር በነባሪነት እንደ ዘካህ አይወሰዱም።"
            )
            Spacer(modifier = Modifier.height(8.dp))
            PrincipleItem(
              title = "4. የኒያ (የውስጥ ፍላጎት) ጥራት",
              detail = "የአላህ መልእክተኛ (ሰ.ዐ.ወ)፡ 'ሥራዎች የሚመዘኑት በኒያ (በሐሳብ) ነው' ብለዋል። ሰደቃ ይፋ ከመሆን በሚስጥር መሆኑ የበለጠ ምንዳ አለው።"
            )
          }
        }
      }
    }

    // 3. User Donation History & Receipts
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (selectedLanguage == Language.AMHARIC) "የእኔ የሰደቃ ታሪክ እና ደረሰኞች" else "My Donation Receipts & History",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
      }
    }

    if (donations.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = if (selectedLanguage == Language.AMHARIC) "እስካሁን ምንም ሰደቃ አልተመዘገበም።" else "No donations recorded yet.",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
          }
        }
      }
    } else {
      items(donations) { don ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onDonationClick(don) },
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.Receipt, contentDescription = null, tint = EmeraldPrimary)
              Column {
                Text(
                  text = "${"%,d".format(don.amount.toLong())} ETB",
                  style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                  )
                )
                Text(
                  text = don.projectNameAm,
                  style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface),
                  maxLines = 1
                )
                Text(
                  text = "${don.id} • ${don.timestampFormatted}",
                  style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
                )
              }
            }

            Text(
              text = if (selectedLanguage == Language.AMHARIC) "ደረሰኝ እይ ›" else "Receipt ›",
              style = MaterialTheme.typography.labelMedium.copy(
                color = EmeraldPrimary,
                fontWeight = FontWeight.Bold
              )
            )
          }
        }
      }
    }

    // 4. Language Selection Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(Icons.Default.Language, contentDescription = null, tint = EmeraldPrimary)
            Text(
              text = if (selectedLanguage == Language.AMHARIC) "ቋንቋ ይምረጡ (Select Language)" else "App Language",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Language.values().forEach { lang ->
              val isSelected = lang == selectedLanguage
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant)
                  .clickable { onLanguageSelected(lang) }
                  .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = lang.displayName,
                  style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                  )
                )
              }
            }
          }
        }
      }
    }

    // 5. Admin Portal Login Button
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable(onClick = onAdminClick)
          .testTag("admin_portal_button"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(EmeraldPastel),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Security, contentDescription = null, tint = EmeraldDark, modifier = Modifier.size(20.dp))
            }
            Column {
              Text(
                text = if (isAdmin) "የአስተዳዳሪ ዳሽቦርድ ክፈት (Open Admin Panel)" else "የአስተዳዳሪ ክፍል (Admin Login)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = if (isAdmin) "ክፍያዎችን ማረጋገጥ፣ ፕሮጀክቶችንና ወጪዎችን ማስተዳደር" else "የተፈቀደላቸው የቦርድ አባላት መግቢያ (PIN)",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
              )
            }
          }
          Icon(Icons.Default.ChevronRight, contentDescription = null, tint = EmeraldPrimary)
        }
      }
    }

    // 6. About App & Contact Footer
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "NIYA (ኒያ) • Islamic Charity Ethiopia",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldPrimary)
        )
        Text(
          text = "ስሪት 1.0.0 • Addis Ababa, Ethiopia",
          style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Text(
          text = "ለአላህ ውዴታ ብቻ የተዘጋጀ የበጎ አድራጎት መተግበሪያ",
          style = MaterialTheme.typography.labelSmall.copy(color = GoldDark, fontWeight = FontWeight.Medium)
        )
      }
    }
  }
}

@Composable
private fun PrincipleItem(title: String, detail: String) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant)
      .padding(10.dp)
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldDark)
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = detail,
      style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface, lineHeight = 16.sp)
    )
  }
}
