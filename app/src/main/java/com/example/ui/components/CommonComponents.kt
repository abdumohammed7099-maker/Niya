package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.NiyaStrings
import com.example.model.Donation
import com.example.model.DonationCategory
import com.example.model.DonationStatus
import com.example.model.Language
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPastel
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPastel
import com.example.ui.theme.GoldPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NiyaTopAppBar(
  selectedLanguage: Language,
  onLanguageSelected: (Language) -> Unit,
  isAdmin: Boolean,
  onAdminClick: () -> Unit
) {
  var showLangMenu by remember { mutableStateOf(false) }

  TopAppBar(
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.ic_niya_logo),
          contentDescription = "NIYA Logo",
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp)),
          contentScale = ContentScale.Crop
        )
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "NIYA",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                letterSpacing = 1.sp
              )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "• ኒያ",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            )
          }
          Text(
            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            style = MaterialTheme.typography.labelSmall.copy(
              color = EmeraldPrimary,
              fontWeight = FontWeight.Medium,
              fontSize = 11.sp
            )
          )
        }
      }
    },
    actions = {
      // Admin shield indicator button
      IconButton(onClick = onAdminClick) {
        Icon(
          imageVector = Icons.Default.Shield,
          contentDescription = "Admin Portal",
          tint = if (isAdmin) GoldPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
      }

      // Language Switcher dropdown
      Box {
        IconButton(onClick = { showLangMenu = true }) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(16.dp))
              .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Language,
              contentDescription = "Language",
              modifier = Modifier.size(16.dp),
              tint = EmeraldPrimary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = when (selectedLanguage) {
                Language.AMHARIC -> "አማ"
                Language.ENGLISH -> "EN"
                Language.ARABIC -> "ع"
              },
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = EmeraldPrimary
            )
          }
        }

        DropdownMenu(
          expanded = showLangMenu,
          onDismissRequest = { showLangMenu = false }
        ) {
          Language.values().forEach { lang ->
            DropdownMenuItem(
              text = {
                Text(
                  text = lang.displayName,
                  fontWeight = if (lang == selectedLanguage) FontWeight.Bold else FontWeight.Normal,
                  color = if (lang == selectedLanguage) EmeraldPrimary else MaterialTheme.colorScheme.onSurface
                )
              },
              onClick = {
                onLanguageSelected(lang)
                showLangMenu = false
              }
            )
          }
        }
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.surface
    )
  )
}

fun getCategoryIcon(category: DonationCategory): ImageVector {
  return when (category) {
    DonationCategory.MOSQUE -> Icons.Default.Mosque
    DonationCategory.POOR_RELIEF -> Icons.Default.VolunteerActivism
    DonationCategory.GRAVEYARD -> Icons.Default.LocationCity
    DonationCategory.ISLAMIC_SCHOOL -> Icons.Default.School
    DonationCategory.HEALTH_CENTER -> Icons.Default.Healing
  }
}

@Composable
fun CategoryCard(
  category: DonationCategory,
  projectCount: Int,
  raisedAmount: Double,
  selectedLanguage: Language,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .clickable(onClick = onClick),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(EmeraldPastel),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = getCategoryIcon(category),
            contentDescription = null,
            tint = EmeraldPrimary,
            modifier = Modifier.size(24.dp)
          )
        }
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(GoldPastel)
            .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Text(
            text = "$projectCount " + if (selectedLanguage == Language.AMHARIC) "ፕሮጀክቶች" else "Projects",
            style = MaterialTheme.typography.labelSmall.copy(
              color = GoldDark,
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = NiyaStrings.getCategoryName(category, selectedLanguage),
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "${"%,d".format(raisedAmount.toLong())} ETB",
        style = MaterialTheme.typography.bodyMedium.copy(
          color = EmeraldPrimary,
          fontWeight = FontWeight.SemiBold
        )
      )
    }
  }
}

@Composable
fun IslamicProgressBar(
  progress: Float,
  modifier: Modifier = Modifier
) {
  val safeProgress = progress.coerceIn(0f, 1f)
  Column(modifier = modifier) {
    LinearProgressIndicator(
      progress = { safeProgress },
      modifier = Modifier
        .fillMaxWidth()
        .height(8.dp)
        .clip(RoundedCornerShape(4.dp)),
      color = EmeraldPrimary,
      trackColor = EmeraldPastel
    )
  }
}

@Composable
fun DonationReceiptDialog(
  donation: Donation,
  selectedLanguage: Language,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 8.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Islamic Crest Header
        Box(
          modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(
              Brush.linearGradient(
                listOf(EmeraldPrimary, EmeraldDark)
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            tint = GoldLight,
            modifier = Modifier.size(36.dp)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "جَزَاكَ ٱللَّٰهُ خَيْرًا",
          style = MaterialTheme.typography.titleMedium.copy(
            color = GoldDark,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
        )

        Text(
          text = if (selectedLanguage == Language.AMHARIC) "የሰደቃ ዲጂታል ደረሰኝ"
                 else if (selectedLanguage == Language.ARABIC) "إيصال التبرع الرقمي"
                 else "Digital Donation Receipt",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        )

        Text(
          text = "NIYA Islamic Charity Foundation • ኢትዮጵያ",
          style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Receipt Card Box
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(14.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ReceiptRow(
              label = if (selectedLanguage == Language.AMHARIC) "የደረሰኝ ቁጥር" else "Receipt Ref",
              value = donation.id,
              isBold = true
            )
            ReceiptRow(
              label = if (selectedLanguage == Language.AMHARIC) "ቀንና ሰዓት" else "Date & Time",
              value = donation.timestampFormatted
            )
            ReceiptRow(
              label = if (selectedLanguage == Language.AMHARIC) "የለጋሽ ስም" else "Donor Name",
              value = donation.donorName
            )
            ReceiptRow(
              label = if (selectedLanguage == Language.AMHARIC) "የሰደቃ ዓላማ (ኒያ)" else "Intention (Niyyah)",
              value = NiyaStrings.getNiyyahTitle(donation.niyyah, selectedLanguage)
            )
            ReceiptRow(
              label = if (selectedLanguage == Language.AMHARIC) "ፕሮጀክት" else "Project",
              value = donation.projectNameAm
            )
            ReceiptRow(
              label = if (selectedLanguage == Language.AMHARIC) "የክፍያ ዘዴ" else "Payment Method",
              value = donation.paymentProvider.nameAm
            )
            ReceiptRow(
              label = if (selectedLanguage == Language.AMHARIC) "የባንክ ማረጋገጫ" else "Bank Ref / Code",
              value = donation.paymentReference
            )

            HorizontalDivider(
              modifier = Modifier.padding(vertical = 4.dp),
              color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = if (selectedLanguage == Language.AMHARIC) "የተለገሰው መጠን:" else "Amount Donated:",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = "${"%,d".format(donation.amount.toLong())} ETB",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = EmeraldPrimary
                )
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Status Badge
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (donation.status == DonationStatus.CONFIRMED) EmeraldPastel else GoldPastel)
            .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Icon(
            imageVector = if (donation.status == DonationStatus.CONFIRMED) Icons.Default.CheckCircle else Icons.Default.Shield,
            contentDescription = null,
            tint = if (donation.status == DonationStatus.CONFIRMED) EmeraldDark else GoldDark,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (donation.status == DonationStatus.CONFIRMED)
              (if (selectedLanguage == Language.AMHARIC) "ክፍያው ተረጋግጧል (Confirmed)" else "Payment Confirmed")
            else
              (if (selectedLanguage == Language.AMHARIC) "በማረጋገጥ ላይ (Pending Verification)" else "Pending Verification"),
            color = if (donation.status == DonationStatus.CONFIRMED) EmeraldDark else GoldDark,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = {
              val shareText = "NIYA Islamic Charity Ethiopia Receipt\nReceipt ID: ${donation.id}\nAmount: ${donation.amount.toInt()} ETB\nProject: ${donation.projectNameAm}\nNiyyah: ${donation.niyyah}\nRef: ${donation.paymentReference}\nMay Allah accept your Sadaqah!"
              val sendIntent = android.content.Intent().apply {
                action = android.content.Intent.ACTION_SEND
                putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
              }
              val shareIntent = android.content.Intent.createChooser(sendIntent, "Share Sadaqah Receipt")
              context.startActivity(shareIntent)
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (selectedLanguage == Language.AMHARIC) "አጋራ" else "Share")
          }

          androidx.compose.material3.Button(
            onClick = onDismiss,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
              containerColor = EmeraldPrimary
            )
          ) {
            Text(if (selectedLanguage == Language.AMHARIC) "እሺ (ጨርስ)" else "Done")
          }
        }
      }
    }
  }
}

@Composable
private fun ReceiptRow(label: String, value: String, isBold: Boolean = false) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
    )
    Text(
      text = value,
      style = MaterialTheme.typography.bodySmall.copy(
        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface
      ),
      textAlign = TextAlign.End
    )
  }
}
