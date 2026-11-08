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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NiyaStrings
import com.example.model.CharityProject
import com.example.model.DonationCategory
import com.example.model.Language
import com.example.model.ProjectStatus
import com.example.ui.components.IslamicProgressBar
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPastel
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPastel
import com.example.ui.theme.GoldPrimary

@Composable
fun ProjectsScreen(
  selectedLanguage: Language,
  projects: List<CharityProject>,
  selectedCategoryFilter: DonationCategory?,
  onCategoryFilterSelected: (DonationCategory?) -> Unit,
  onDonateToProject: (CharityProject) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }

  val filteredProjects = projects.filter { project ->
    val matchesCategory = selectedCategoryFilter == null || project.category == selectedCategoryFilter
    val matchesSearch = searchQuery.isBlank() ||
      project.titleAm.contains(searchQuery, ignoreCase = true) ||
      project.titleEn.contains(searchQuery, ignoreCase = true) ||
      project.locationAm.contains(searchQuery, ignoreCase = true)
    matchesCategory && matchesSearch
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .testTag("projects_screen")
  ) {
    // Search Bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .testTag("projects_search_input"),
      placeholder = {
        Text(
          text = if (selectedLanguage == Language.AMHARIC) "የፕሮጀክት ስም ወይም ቦታ ይፈልጉ..." else "Search projects or locations..."
        )
      },
      leadingIcon = {
        Icon(Icons.Default.Search, contentDescription = null, tint = EmeraldPrimary)
      },
      shape = RoundedCornerShape(14.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = EmeraldPrimary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline
      ),
      singleLine = true
    )

    // Category Filter Chips
    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      item {
        FilterChip(
          selected = selectedCategoryFilter == null,
          onClick = { onCategoryFilterSelected(null) },
          label = {
            Text(if (selectedLanguage == Language.AMHARIC) "ሁሉም (All)" else "All")
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = EmeraldPrimary,
            selectedLabelColor = androidx.compose.ui.graphics.Color.White
          )
        )
      }

      items(DonationCategory.values()) { category ->
        FilterChip(
          selected = selectedCategoryFilter == category,
          onClick = { onCategoryFilterSelected(category) },
          label = {
            Text(NiyaStrings.getCategoryName(category, selectedLanguage))
          },
          leadingIcon = {
            Icon(
              imageVector = getCategoryIcon(category),
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = EmeraldPrimary,
            selectedLabelColor = androidx.compose.ui.graphics.Color.White
          )
        )
      }
    }

    // Projects List
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(filteredProjects) { project ->
        ProjectCard(
          project = project,
          selectedLanguage = selectedLanguage,
          onDonateClick = { onDonateToProject(project) }
        )
      }
    }
  }
}

@Composable
fun ProjectCard(
  project: CharityProject,
  selectedLanguage: Language,
  onDonateClick: () -> Unit
) {
  var isExpanded by remember { mutableStateOf(false) }

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Top status & category tag row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(EmeraldPastel)
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Icon(
            imageVector = getCategoryIcon(project.category),
            contentDescription = null,
            tint = EmeraldPrimary,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = NiyaStrings.getCategoryName(project.category, selectedLanguage),
            style = MaterialTheme.typography.labelSmall.copy(
              color = EmeraldDark,
              fontWeight = FontWeight.Bold
            )
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
              when (project.status) {
                ProjectStatus.COMPLETED -> EmeraldPastel
                ProjectStatus.IN_PROGRESS -> GoldPastel
                ProjectStatus.ACTIVE -> MaterialTheme.colorScheme.surfaceVariant
              }
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = when (project.status) {
              ProjectStatus.COMPLETED -> if (selectedLanguage == Language.AMHARIC) "የተጠናቀቀ ✓" else "Completed ✓"
              ProjectStatus.IN_PROGRESS -> if (selectedLanguage == Language.AMHARIC) "በሂደት ላይ" else "In Progress"
              ProjectStatus.ACTIVE -> if (selectedLanguage == Language.AMHARIC) "ንቁ ፕሮጀክት" else "Active"
            },
            style = MaterialTheme.typography.labelSmall.copy(
              color = when (project.status) {
                ProjectStatus.COMPLETED -> EmeraldDark
                ProjectStatus.IN_PROGRESS -> GoldDark
                ProjectStatus.ACTIVE -> EmeraldPrimary
              },
              fontWeight = FontWeight.Bold
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Title & Location
      Text(
        text = if (selectedLanguage == Language.ENGLISH) project.titleEn else project.titleAm,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp
        )
      )

      Spacer(modifier = Modifier.height(4.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = if (selectedLanguage == Language.ENGLISH) project.locationEn else project.locationAm,
          style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
          imageVector = Icons.Default.People,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "${"%,d".format(project.beneficiaryCount)} ተጠቃሚዎች",
          style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = if (selectedLanguage == Language.ENGLISH) project.descriptionEn else project.descriptionAm,
        style = MaterialTheme.typography.bodySmall.copy(
          color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
          lineHeight = 18.sp
        ),
        maxLines = if (isExpanded) 10 else 2,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Funding Progress Bar
      IslamicProgressBar(
        progress = (project.raisedAmount / project.targetAmount).toFloat(),
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = if (selectedLanguage == Language.AMHARIC) "የተሰበሰበ" else "Raised",
            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )
          Text(
            text = "${"%,d".format(project.raisedAmount.toLong())} ETB",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Bold,
              color = EmeraldPrimary
            )
          )
        }
        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = if (selectedLanguage == Language.AMHARIC) "ዒላማ" else "Target Goal",
            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )
          Text(
            text = "${"%,d".format(project.targetAmount.toLong())} ETB (${project.completionPercentage}%)",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Action Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        androidx.compose.material3.OutlinedButton(
          onClick = { isExpanded = !isExpanded },
          modifier = Modifier.weight(0.9f),
          shape = RoundedCornerShape(10.dp)
        ) {
          Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (isExpanded) (if (selectedLanguage == Language.AMHARIC) "ቀንስ" else "Less")
                   else (if (selectedLanguage == Language.AMHARIC) "ዝርዝር" else "Details"),
            fontSize = 12.sp
          )
        }

        Button(
          onClick = onDonateClick,
          modifier = Modifier
            .weight(1.3f)
            .testTag("donate_to_project_button_${project.id}"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = EmeraldPrimary
          )
        ) {
          Icon(Icons.Default.VolunteerActivism, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (selectedLanguage == Language.AMHARIC) "ለዚህ ለግስ" else "Donate Now",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      }
    }
  }
}
