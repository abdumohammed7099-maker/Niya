package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.NiyaStrings
import com.example.model.AdminAuditLog
import com.example.model.CharityProject
import com.example.model.Donation
import com.example.model.DonationCategory
import com.example.model.DonationStatus
import com.example.model.ExpenseItem
import com.example.model.Language
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPastel
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPastel
import com.example.ui.theme.GoldPrimary

@Composable
fun AdminLoginDialog(
  onDismiss: () -> Unit,
  onLogin: (String) -> Boolean
) {
  var pin by remember { mutableStateOf("") }
  var error by remember { mutableStateOf(false) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
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
        Box(
          modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(GoldPastel),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Security, contentDescription = null, tint = GoldDark, modifier = Modifier.size(28.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "የአስተዳዳሪ ማረጋገጫ (Admin Access)",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Text(
          text = "ይህ ገጽ የተፈቀደላቸው የቦርድ አባላትና የፋይናንስ ኦዲተሮች ብቻ የሚገቡበት ነው።",
          style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
          modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
        )

        OutlinedTextField(
          value = pin,
          onValueChange = {
            if (it.length <= 6) {
              pin = it
              error = false
            }
          },
          label = { Text("የይለፍ ቃል ፒን (PIN)") },
          placeholder = { Text("ነባሪ ፒን: 1994") },
          visualTransformation = PasswordVisualTransformation(),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
          isError = error,
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("admin_pin_input")
        )

        if (error) {
          Text(
            text = "የተሳሳተ ፒን ቁጥር! እባክዎ 1994 ይጠቀሙ።",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f)
          ) {
            Text("ተመለስ")
          }
          Button(
            onClick = {
              val success = onLogin(pin)
              if (!success) {
                error = true
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
            modifier = Modifier.weight(1f).testTag("admin_login_submit")
          ) {
            Text("ግባ (Enter)")
          }
        }
      }
    }
  }
}

@Composable
fun AdminDashboardView(
  selectedLanguage: Language,
  projects: List<CharityProject>,
  donations: List<Donation>,
  expenses: List<ExpenseItem>,
  auditLogs: List<AdminAuditLog>,
  onClose: () -> Unit,
  onVerifyDonation: (String) -> Unit,
  onOpenNewProject: () -> Unit,
  onOpenNewExpense: () -> Unit,
  onLogout: () -> Unit
) {
  var selectedSubTab by remember { mutableStateOf("donations") }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Admin Header
    Surface(
      color = EmeraldDark,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Icon(Icons.Default.Shield, contentDescription = null, tint = GoldLight)
          Column {
            Text(
              text = "የአስተዳዳሪ ክፍል (Admin Dashboard)",
              style = MaterialTheme.typography.titleMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
              )
            )
            Text(
              text = "የተፈቀደ ኦዲት እና ቁጥጥር ማዕከል",
              style = MaterialTheme.typography.labelSmall.copy(color = EmeraldPastel)
            )
          }
        }

        IconButton(onClick = onLogout) {
          Icon(Icons.Default.Close, contentDescription = "Exit Admin", tint = Color.White)
        }
      }
    }

    // Sub Navigation Tabs
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface)
        .padding(horizontal = 12.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      item {
        AdminSubTabPill(
          title = "ልገሳዎች ማረጋገጫ",
          count = donations.count { it.status == DonationStatus.PENDING_VERIFICATION },
          isSelected = selectedSubTab == "donations",
          onClick = { selectedSubTab = "donations" }
        )
      }
      item {
        AdminSubTabPill(
          title = "ፕሮጀክቶች (${projects.size})",
          isSelected = selectedSubTab == "projects",
          onClick = { selectedSubTab = "projects" }
        )
      }
      item {
        AdminSubTabPill(
          title = "ህጋዊ ወጪዎች (${expenses.size})",
          isSelected = selectedSubTab == "expenses",
          onClick = { selectedSubTab = "expenses" }
        )
      }
      item {
        AdminSubTabPill(
          title = "የኦዲት መዝገብ (${auditLogs.size})",
          isSelected = selectedSubTab == "audit",
          onClick = { selectedSubTab = "audit" }
        )
      }
    }

    // Tab Contents
    when (selectedSubTab) {
      "donations" -> {
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          item {
            Text(
              text = "የተጠቃሚዎች የባንክ ዝውውር ማረጋገጫ ዝርዝር",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
              text = "ክፍያው በባንክ ሒሳብ መግባቱ ሲረጋገጥ 'አረጋግጥ' የሚለውን ይጫኑ።",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
            Spacer(modifier = Modifier.height(4.dp))
          }

          items(donations) { don ->
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
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = "${don.donorName} • ${"%,d".format(don.amount.toLong())} ETB",
                      style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                      text = "${don.projectNameAm} • ${don.paymentProvider.nameAm}",
                      style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                      text = "ማረጋገጫ ኮድ: ${don.paymentReference} | ${don.timestampFormatted}",
                      style = MaterialTheme.typography.labelSmall.copy(color = EmeraldDark, fontWeight = FontWeight.Bold)
                    )
                  }

                  if (don.status == DonationStatus.PENDING_VERIFICATION) {
                    Button(
                      onClick = { onVerifyDonation(don.id) },
                      colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                      shape = RoundedCornerShape(8.dp)
                    ) {
                      Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                      Spacer(modifier = Modifier.width(4.dp))
                      Text("አረጋግጥ", fontSize = 12.sp)
                    }
                  } else {
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(EmeraldPastel)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                      Text("ተረጋግጧል ✓", color = EmeraldDark, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    }
                  }
                }
              }
            }
          }
        }
      }

      "projects" -> {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "የበጎ አድራጎት ፕሮጀክቶች አስተዳደር",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Button(
              onClick = onOpenNewProject,
              colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("አዲስ ፕሮጀክት", fontSize = 12.sp)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            items(projects) { p ->
              Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(
                      text = p.titleAm,
                      style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                      modifier = Modifier.weight(1f)
                    )
                    Text(
                      text = "${p.completionPercentage}%",
                      style = MaterialTheme.typography.labelMedium.copy(color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                    )
                  }
                  Text(
                    text = "${p.locationAm} • ዒላማ: ${"%,d".format(p.targetAmount.toLong())} ETB",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                  )
                  Text(
                    text = "የተሰበሰበ: ${"%,d".format(p.raisedAmount.toLong())} ETB | ተጠቃሚዎች: ${p.beneficiaryCount}",
                    style = MaterialTheme.typography.labelSmall.copy(color = EmeraldDark)
                  )
                }
              }
            }
          }
        }
      }

      "expenses" -> {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "የተረጋገጡ የግንባታና የእርዳታ ወጪዎች",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Button(
              onClick = onOpenNewExpense,
              colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("ወጪ መዝግብ", fontSize = 12.sp)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            items(expenses) { exp ->
              Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(
                      text = exp.titleAm,
                      style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                      text = "-${"%,d".format(exp.amount.toLong())} ETB",
                      style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                      )
                    )
                  }
                  Text(
                    text = "አቅራቢ: ${exp.vendorOrContractor} • ደረሰኝ: ${exp.receiptNumber}",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                  )
                  Text(
                    text = "ቀን: ${exp.dateFormatted} • ያጸደቀው: ${exp.approvedBy}",
                    style = MaterialTheme.typography.labelSmall.copy(color = EmeraldDark)
                  )
                }
              }
            }
          }
        }
      }

      "audit" -> {
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          item {
            Text(
              text = "የስርዓቱ የለውጥና የኦዲት መዝገብ (Audit Log)",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
              text = "ማንኛውም የአስተዳዳሪ ድርጊት ተመዝግቦ ይቀመጣል።",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
          }

          items(auditLogs) { log ->
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Text(
                  text = log.actionAm,
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = "ኃላፊ: ${log.adminName}",
                    style = MaterialTheme.typography.labelSmall.copy(color = EmeraldPrimary)
                  )
                  Text(
                    text = log.timestampFormatted,
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun AdminSubTabPill(
  title: String,
  count: Int = 0,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(10.dp))
      .background(if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant)
      .clickable(onClick = onClick)
      .padding(horizontal = 12.dp, vertical = 8.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(
        text = title,
        style = MaterialTheme.typography.labelMedium.copy(
          fontWeight = FontWeight.Bold,
          color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
        )
      )
      if (count > 0) {
        Spacer(modifier = Modifier.width(6.dp))
        Box(
          modifier = Modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(GoldPrimary),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "$count",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectDialog(
  onDismiss: () -> Unit,
  onCreate: (
    titleAm: String,
    titleEn: String,
    category: DonationCategory,
    descriptionAm: String,
    descriptionEn: String,
    locationAm: String,
    locationEn: String,
    targetAmount: Double,
    beneficiaries: Int
  ) -> Unit
) {
  var titleAm by remember { mutableStateOf("") }
  var category by remember { mutableStateOf(DonationCategory.MOSQUE) }
  var locationAm by remember { mutableStateOf("") }
  var targetAmountText by remember { mutableStateOf("") }
  var descriptionAm by remember { mutableStateOf("") }
  var beneficiariesText by remember { mutableStateOf("500") }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.surface,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = "አዲስ የበጎ አድራጎት ፕሮጀክት መዝግብ",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        OutlinedTextField(
          value = titleAm,
          onValueChange = { titleAm = it },
          label = { Text("የፕሮጀክቱ ስም (በአማርኛ)") },
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = locationAm,
          onValueChange = { locationAm = it },
          label = { Text("ቦታ (ምሳሌ: አዲስ አበባ፣ ኮልፌ)") },
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = targetAmountText,
          onValueChange = { targetAmountText = it },
          label = { Text("ዒላማ የገንዘብ መጠን (በኢትዮጵያ ብር)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = descriptionAm,
          onValueChange = { descriptionAm = it },
          label = { Text("የፕሮጀክቱ ዝርዝር ማብራሪያ") },
          modifier = Modifier.fillMaxWidth(),
          maxLines = 3
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
            Text("ይቅር")
          }
          Button(
            onClick = {
              val target = targetAmountText.toDoubleOrNull() ?: 100000.0
              val ben = beneficiariesText.toIntOrNull() ?: 100
              if (titleAm.isNotBlank()) {
                onCreate(
                  titleAm,
                  titleAm,
                  category,
                  descriptionAm,
                  descriptionAm,
                  locationAm,
                  locationAm,
                  target,
                  ben
                )
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
            modifier = Modifier.weight(1f)
          ) {
            Text("ፍጠር")
          }
        }
      }
    }
  }
}

@Composable
fun RecordExpenseDialog(
  projects: List<CharityProject>,
  onDismiss: () -> Unit,
  onRecord: (
    projectId: String,
    titleAm: String,
    titleEn: String,
    amount: Double,
    vendor: String,
    receiptRef: String
  ) -> Unit
) {
  var selectedProject by remember { mutableStateOf(projects.firstOrNull()?.id ?: "") }
  var titleAm by remember { mutableStateOf("") }
  var amountText by remember { mutableStateOf("") }
  var vendor by remember { mutableStateOf("") }
  var receiptRef by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.surface,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = "ህጋዊ የግንባታ/እርዳታ ወጪ መዝግብ",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        OutlinedTextField(
          value = titleAm,
          onValueChange = { titleAm = it },
          label = { Text("የወጪው ርዕስ (ምሳሌ: የሲሚንቶ ግዢ)") },
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = amountText,
          onValueChange = { amountText = it },
          label = { Text("የተከፈለው መጠን (ETB)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = vendor,
          onValueChange = { vendor = it },
          label = { Text("የተከፈለለት ድርጅት / አቅራቢ") },
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = receiptRef,
          onValueChange = { receiptRef = it },
          label = { Text("የደረሰኝ ቁጥር (Invoice / Receipt Ref)") },
          modifier = Modifier.fillMaxWidth()
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
            Text("ይቅር")
          }
          Button(
            onClick = {
              val amt = amountText.toDoubleOrNull() ?: 0.0
              if (titleAm.isNotBlank() && amt > 0) {
                onRecord(
                  selectedProject,
                  titleAm,
                  titleAm,
                  amt,
                  vendor,
                  receiptRef.ifBlank { "REC-${(1000..9999).random()}" }
                )
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
            modifier = Modifier.weight(1f)
          ) {
            Text("መዝግብ")
          }
        }
      }
    }
  }
}
