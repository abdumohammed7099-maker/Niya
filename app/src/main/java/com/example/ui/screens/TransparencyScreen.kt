package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NiyaStrings
import com.example.model.CharityProject
import com.example.model.ExpenseItem
import com.example.model.Language
import com.example.ui.TransparencySummary
import com.example.ui.components.IslamicProgressBar
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPastel
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPastel
import com.example.ui.theme.GoldPrimary

@Composable
fun TransparencyScreen(
  selectedLanguage: Language,
  summary: TransparencySummary,
  projects: List<CharityProject>,
  expenses: List<ExpenseItem>
) {
  val context = LocalContext.current

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .testTag("transparency_screen"),
    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Header Banner: Public Financial Transparency
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.Shield, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(22.dp))
            Text(
              text = if (selectedLanguage == Language.AMHARIC) "የህዝብ የፋይናንስ ግልጽነት መረጃ" else "Public Financial Transparency Ledger",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary
              )
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = if (selectedLanguage == Language.AMHARIC)
              "በNIYA መድረክ የሚሰበሰብ እያንዳንዱ የሰደቃ ሳንቲም በአላህ ፊት እና በህዝብ ፊት በታማኝነት ለታለመለት ፕሮጀክት ብቻ ይውላል። ወጪዎች በደረሰኝ የተረጋገጡ ናቸው።"
            else
              "Every single cent collected through NIYA is directly audited, safeguarded, and disbursed strictly with verified vendor receipts and board approvals.",
            style = MaterialTheme.typography.bodySmall.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 18.sp
            )
          )
        }
      }
    }

    // 2. High-Level Summary Cards: Collected, Spent, Remaining
    item {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        FinancialMetricRow(
          title = if (selectedLanguage == Language.AMHARIC) "ድምር የተሰበሰበ ሰደቃ (Total Raised)" else "Total Collected",
          amount = summary.totalCollected,
          icon = Icons.Default.MonetizationOn,
          accentColor = EmeraldPrimary,
          bgColor = EmeraldPastel
        )
        FinancialMetricRow(
          title = if (selectedLanguage == Language.AMHARIC) "ለፕሮጀክቶች ወጪ የተደረገ (Total Disbursed)" else "Total Disbursed / Spent",
          amount = summary.totalSpent,
          icon = Icons.Default.Payments,
          accentColor = GoldDark,
          bgColor = GoldPastel
        )
        FinancialMetricRow(
          title = if (selectedLanguage == Language.AMHARIC) "በሒሳብ ላይ ያለ ቀሪ (Remaining Balance)" else "Active Treasury Balance",
          amount = summary.remainingBalance,
          icon = Icons.Default.AccountBalance,
          accentColor = EmeraldDark,
          bgColor = EmeraldPastel
        )
      }
    }

    // 3. Administrative Expenses Disclose
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (selectedLanguage == Language.AMHARIC) "የአስተዳደር ወጪ መጣን (Admin Ratio)" else "Operational Expense Ratio",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(EmeraldPastel)
                .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Text(
                text = "${summary.administrativeExpensePercent}% ብቻ",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = EmeraldDark,
                  fontWeight = FontWeight.Bold
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          IslamicProgressBar(
            progress = (summary.administrativeExpensePercent / 100).toFloat(),
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = if (selectedLanguage == Language.AMHARIC)
              "96.8% የሚሆነው ልገሳ በቀጥታ ለፕሮጀክቶችና ለተቸገሩት ይውላል። 3.2% የሚሆነው ለአገልጋዮች የባንክ ዝውውርና የቴሌኮም ክፍያ ብቻ ይውላል።"
            else
              "96.8% directly funds grassroots projects. Only 3.2% covers technical infrastructure and banking gateway operational fees.",
            style = MaterialTheme.typography.bodySmall.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontSize = 11.sp
            )
          )
        }
      }
    }

    // 4. Project-by-Project Budget & Progress Breakdown
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (selectedLanguage == Language.AMHARIC) "የፕሮጀክቶች በጀት እና አፈጻጸም" else "Project Budgets & Execution",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
      }
    }

    items(projects) { project ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(
                imageVector = getCategoryIcon(project.category),
                contentDescription = null,
                tint = EmeraldPrimary,
                modifier = Modifier.size(18.dp)
              )
              Text(
                text = project.titleAm,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1
              )
            }
            Text(
              text = "${project.completionPercentage}%",
              style = MaterialTheme.typography.labelMedium.copy(
                color = EmeraldPrimary,
                fontWeight = FontWeight.Bold
              )
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          IslamicProgressBar(
            progress = (project.raisedAmount / project.targetAmount).toFloat(),
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "የተሰበሰበ: ${"%,d".format(project.raisedAmount.toLong())} ብር",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
            Text(
              text = "የተረጋገጠ ወጪ: ${"%,d".format(project.verifiedExpenses.toLong())} ብር",
              style = MaterialTheme.typography.bodySmall.copy(color = GoldDark, fontWeight = FontWeight.SemiBold)
            )
          }
        }
      }
    }

    // 5. Verified Expenditures Ledger (የተረጋገጡ ወጪዎች)
    item {
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = if (selectedLanguage == Language.AMHARIC) "የተረጋገጡ የግንባታና የእርዳታ ወጪዎች (Audit Ledger)" else "Verified Expense Audit Trail",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
    }

    items(expenses) { exp ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = exp.titleAm,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = "${exp.vendorOrContractor} • ደረሰኝ: ${exp.receiptNumber}",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
              )
            }
            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "-${"%,d".format(exp.amount.toLong())} ETB",
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.error
                )
              )
              Text(
                text = exp.dateFormatted,
                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "የጸደቀው በ: ${exp.approvedBy}",
              style = MaterialTheme.typography.labelSmall.copy(color = EmeraldDark, fontSize = 10.sp)
            )
          }
        }
      }
    }

    // 6. Share / Export Report Button
    item {
      Spacer(modifier = Modifier.height(8.dp))
      OutlinedButton(
        onClick = {
          val shareText = "NIYA Islamic Charity Public Transparency Report\nTotal Collected: ${summary.totalCollected.toLong()} ETB\nTotal Disbursed: ${summary.totalSpent.toLong()} ETB\nRemaining Balance: ${summary.remainingBalance.toLong()} ETB\nActive Projects: ${summary.activeProjectsCount}\nCompleted Projects: ${summary.completedProjectsCount}\nAdmin Expense: ${summary.administrativeExpensePercent}%\nAudited for the Ummah in Ethiopia."
          val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
          }
          val shareIntent = Intent.createChooser(sendIntent, "Share Financial Transparency Report")
          context.startActivity(shareIntent)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
      ) {
        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(if (selectedLanguage == Language.AMHARIC) "የግልጽነት ሪፖርቱን አጋራ (Share Report)" else "Share Transparency Report")
      }
    }
  }
}

@Composable
private fun FinancialMetricRow(
  title: String,
  amount: Double,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  accentColor: Color,
  bgColor: Color
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(bgColor),
          contentAlignment = Alignment.Center
        ) {
          Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(22.dp))
        }
        Column {
          Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )
          Text(
            text = "${"%,d".format(amount.toLong())} ETB",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = accentColor
            )
          )
        }
      }
    }
  }
}
