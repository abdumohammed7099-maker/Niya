package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.NiyaStrings
import com.example.model.CharityProject
import com.example.model.Donation
import com.example.model.DonationCategory
import com.example.model.IslamicQuote
import com.example.model.Language
import com.example.ui.TransparencySummary
import com.example.ui.components.CategoryCard
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPastel
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPastel
import com.example.ui.theme.GoldPrimary

@Composable
fun HomeScreen(
  selectedLanguage: Language,
  projects: List<CharityProject>,
  donations: List<Donation>,
  transparencySummary: TransparencySummary,
  islamicQuotes: List<IslamicQuote>,
  onDonateClick: () -> Unit,
  onCategoryClick: (DonationCategory) -> Unit,
  onViewAllProjectsClick: () -> Unit,
  onDonationClick: (Donation) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .testTag("home_screen_scroll"),
    contentPadding = PaddingValues(bottom = 24.dp)
  ) {
    // 1. Hero Banner with Mosque Imagery and Quick CTA
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(260.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_islamic_hero),
          contentDescription = "Majestic Mosque Hero Banner",
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        // Gradient overlay for high legibility
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                listOf(
                  Color.Black.copy(alpha = 0.3f),
                  DarkBackground.copy(alpha = 0.85f),
                  DarkBackground
                )
              )
            )
        )

        // Hero Content
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 18.dp),
          verticalArrangement = Arrangement.Bottom
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(GoldPrimary.copy(alpha = 0.85f))
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(
              text = "NIYA ኒያ • በጎ አድራጎት",
              style = MaterialTheme.typography.labelSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
              )
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = NiyaStrings.welcomeTitle(selectedLanguage),
            style = MaterialTheme.typography.headlineMedium.copy(
              color = Color.White,
              fontWeight = FontWeight.Bold
            )
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = NiyaStrings.welcomeSubtitle(selectedLanguage),
            style = MaterialTheme.typography.bodyMedium.copy(
              color = Color.White.copy(alpha = 0.9f)
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Prominent "ሰደቃ አድርግ" Button
          Button(
            onClick = onDonateClick,
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("home_hero_donate_button"),
            colors = ButtonDefaults.buttonColors(
              containerColor = GoldPrimary,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.VolunteerActivism,
              contentDescription = null,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = NiyaStrings.donateNow(selectedLanguage),
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
            )
          }
        }
      }
    }

    // 2. Transparency Metrics Quick Bar
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        StatCard(
          title = if (selectedLanguage == Language.AMHARIC) "የተሰበሰበ ሰደቃ" else "Total Collected",
          value = "${"%,d".format(transparencySummary.totalCollected.toLong())} ብር",
          icon = Icons.Default.Payments,
          color = EmeraldPrimary,
          bgColor = EmeraldPastel,
          modifier = Modifier.weight(1f)
        )
        StatCard(
          title = if (selectedLanguage == Language.AMHARIC) "ያሉ ፕሮጀክቶች" else "Active Projects",
          value = "${transparencySummary.activeProjectsCount}",
          icon = Icons.Default.HourglassTop,
          color = GoldDark,
          bgColor = GoldPastel,
          modifier = Modifier.weight(1f)
        )
        StatCard(
          title = if (selectedLanguage == Language.AMHARIC) "የተጠናቀቁ" else "Completed",
          value = "${transparencySummary.completedProjectsCount}",
          icon = Icons.Default.CheckCircle,
          color = EmeraldDark,
          bgColor = EmeraldPastel,
          modifier = Modifier.weight(1f)
        )
      }
    }

    // 3. Main 5 Donation Categories Section Header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (selectedLanguage == Language.AMHARIC) "የሰደቃ ዘርፎች (Categories)" else "Donation Categories",
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
          text = if (selectedLanguage == Language.AMHARIC) "ሁሉንም እይ" else "View All",
          style = MaterialTheme.typography.bodyMedium.copy(
            color = EmeraldPrimary,
            fontWeight = FontWeight.SemiBold
          ),
          modifier = Modifier.clickable(onClick = onViewAllProjectsClick)
        )
      }
    }

    // 5 Categories Horizontal / Grid Scroll
    item {
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(DonationCategory.values()) { category ->
          val catProjects = projects.filter { it.category == category }
          val catRaised = catProjects.sumOf { it.raisedAmount }
          CategoryCard(
            category = category,
            projectCount = catProjects.size,
            raisedAmount = catRaised,
            selectedLanguage = selectedLanguage,
            onClick = { onCategoryClick(category) },
            modifier = Modifier.width(170.dp)
          )
        }
      }
    }

    // 4. Islamic Inspirational Quote Card
    item {
      val quote = islamicQuotes.firstOrNull()
      if (quote != null) {
        Spacer(modifier = Modifier.height(16.dp))
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
          )
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(
                imageVector = Icons.Default.FormatQuote,
                contentDescription = null,
                tint = GoldDark,
                modifier = Modifier.size(24.dp)
              )
              Text(
                text = quote.source,
                style = MaterialTheme.typography.labelSmall.copy(
                  color = GoldDark,
                  fontWeight = FontWeight.Bold
                )
              )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = quote.arabic,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = EmeraldDark,
                lineHeight = 24.sp
              ),
              textAlign = TextAlign.End,
              modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = if (selectedLanguage == Language.ENGLISH) quote.english else quote.amharic,
              style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            )
          }
        }
      }
    }

    // 5. Recent Verified Donations Feed
    item {
      Spacer(modifier = Modifier.height(16.dp))
      Text(
        text = if (selectedLanguage == Language.AMHARIC) "የቅርብ ጊዜ ሰደቃዎች (Recent Donations)" else "Recent Sadaqah Donations",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
      )
    }

    items(donations.take(5)) { donation ->
      RecentDonationRow(
        donation = donation,
        selectedLanguage = selectedLanguage,
        onClick = { onDonationClick(donation) }
      )
    }
  }
}

@Composable
private fun StatCard(
  title: String,
  value: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color,
  bgColor: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
    ) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(bgColor),
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
      }
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Text(
        text = title,
        style = MaterialTheme.typography.bodySmall.copy(
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 11.sp
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
}

@Composable
private fun RecentDonationRow(
  donation: Donation,
  selectedLanguage: Language,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 4.dp)
      .clickable(onClick = onClick),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    )
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
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.weight(1f)
      ) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(EmeraldPastel),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = EmeraldPrimary,
            modifier = Modifier.size(20.dp)
          )
        }
        Column {
          Text(
            text = donation.donorName,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Text(
            text = donation.projectNameAm,
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Text(
            text = "${donation.paymentProvider.nameEn} • ${donation.timestampFormatted}",
            style = MaterialTheme.typography.labelSmall.copy(color = EmeraldDark, fontSize = 10.sp)
          )
        }
      }

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = "+${"%,d".format(donation.amount.toLong())} ETB",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = EmeraldPrimary
          )
        )
        Text(
          text = NiyaStrings.getNiyyahTitle(donation.niyyah, selectedLanguage),
          style = MaterialTheme.typography.labelSmall.copy(
            color = GoldDark,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp
          )
        )
      }
    }
  }
}
