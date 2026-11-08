package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NiyaStrings
import com.example.model.CharityProject
import com.example.model.Language
import com.example.model.NiyyahType
import com.example.model.PaymentProvider
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPastel
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPastel
import com.example.ui.theme.GoldPrimary

@Composable
fun DonateScreen(
  selectedLanguage: Language,
  projects: List<CharityProject>,
  targetProject: CharityProject?,
  onSelectProject: (CharityProject?) -> Unit,
  onSubmitDonation: (
    amount: Double,
    donorName: String,
    isAnonymous: Boolean,
    donorPhone: String,
    niyyah: NiyyahType,
    intentionNote: String,
    paymentProvider: PaymentProvider,
    paymentReference: String
  ) -> Unit
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current

  // Form State
  var amountText by remember { mutableStateOf("500") }
  val suggestedAmounts = listOf(50, 100, 200, 500, 1000, 5000)

  var selectedNiyyah by remember { mutableStateOf(NiyyahType.SADAQAH) }
  var isAnonymous by remember { mutableStateOf(false) }
  var donorName by remember { mutableStateOf("") }
  var donorPhone by remember { mutableStateOf("") }
  var intentionNote by remember { mutableStateOf("") }

  var selectedProvider by remember { mutableStateOf(PaymentProvider.TELEBIRR) }
  var paymentRefInput by remember { mutableStateOf("") }

  var errorMessage by remember { mutableStateOf<String?>(null) }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .testTag("donate_screen_scroll"),
    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Header Card: Selected Project or General Sadaqah
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
            Icon(
              imageVector = if (targetProject != null) getCategoryIcon(targetProject.category) else Icons.Default.VolunteerActivism,
              contentDescription = null,
              tint = EmeraldPrimary,
              modifier = Modifier.size(20.dp)
            )
            Text(
              text = if (selectedLanguage == Language.AMHARIC) "የተመረጠ የበጎ አድራጎት ፕሮጀክት" else "Selected Cause / Project",
              style = MaterialTheme.typography.labelMedium.copy(
                color = EmeraldPrimary,
                fontWeight = FontWeight.Bold
              )
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = targetProject?.titleAm ?: (if (selectedLanguage == Language.AMHARIC) "አጠቃላይ የሰደቃ ፈንድ (General Sadaqah)" else "General Sadaqah & Community Development Pool"),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )

          if (targetProject != null) {
            Text(
              text = "${targetProject.locationAm} • ${"%,d".format(targetProject.targetAmount.toLong())} ETB ዒላማ",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
          } else {
            Text(
              text = if (selectedLanguage == Language.AMHARIC) "ልገሳዎ በአስቸኳይ ቅድሚያ ለሚሰጣቸው የመስጊድና የድሆች ፕሮጀክቶች ይውላል።" else "Your contribution directly assists urgent priority Islamic relief works.",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
          }
        }
      }
    }

    // 2. Donation Amount Selector (Suggested & Custom ETB)
    item {
      Text(
        text = if (selectedLanguage == Language.AMHARIC) "የሰደቃ መጠን በኢትዮጵያ ብር (ETB Amount) *" else "Donation Amount in ETB *",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Preset pills: 50, 100, 200, 500, 1000, 5000 ETB
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(suggestedAmounts) { amount ->
          val isSelected = amountText == amount.toString()
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.surface)
              .border(
                1.dp,
                if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
              )
              .clickable { amountText = amount.toString() }
              .padding(horizontal = 14.dp, vertical = 10.dp)
          ) {
            Text(
              text = "$amount ብር",
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = amountText,
        onValueChange = { input ->
          if (input.all { it.isDigit() || it == '.' }) {
            amountText = input
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("donation_amount_input"),
        label = { Text(if (selectedLanguage == Language.AMHARIC) "የሚለግሱትን የብር መጠን ያስገቡ" else "Enter custom amount in ETB") },
        trailingIcon = {
          Text(
            text = "ETB",
            fontWeight = FontWeight.Bold,
            color = EmeraldPrimary,
            modifier = Modifier.padding(end = 12.dp)
          )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = EmeraldPrimary
        ),
        singleLine = true
      )
    }

    // 3. Intention / Niyyah Selector
    item {
      Text(
        text = if (selectedLanguage == Language.AMHARIC) "የልገሳው ኒያ (Intention) *" else "Intention of Sadaqah (Niyyah) *",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )

      Spacer(modifier = Modifier.height(6.dp))

      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        NiyyahType.values().forEach { niyyah ->
          val isSelected = selectedNiyyah == niyyah
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSelected) EmeraldPastel else MaterialTheme.colorScheme.surface)
              .border(
                1.dp,
                if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                RoundedCornerShape(12.dp)
              )
              .clickable { selectedNiyyah = niyyah }
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(if (isSelected) EmeraldPrimary else Color.Transparent)
                .border(2.dp, if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.outline, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              if (isSelected) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
              }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = NiyaStrings.getNiyyahTitle(niyyah, selectedLanguage),
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) EmeraldDark else MaterialTheme.colorScheme.onSurface
                )
              )
              Text(
                text = NiyaStrings.getNiyyahExplanation(niyyah, selectedLanguage),
                style = MaterialTheme.typography.bodySmall.copy(
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  fontSize = 11.sp
                )
              )
            }
          }
        }
      }
    }

    // 4. Donor Information & Anonymous Sadaqah Toggle
    item {
      Text(
        text = if (selectedLanguage == Language.AMHARIC) "የለጋሽ መረጃ" else "Donor Information",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant)
          .padding(8.dp)
      ) {
        Checkbox(
          checked = isAnonymous,
          onCheckedChange = { isAnonymous = it },
          colors = CheckboxDefaults.colors(checkedColor = EmeraldPrimary)
        )
        Column {
          Text(
            text = if (selectedLanguage == Language.AMHARIC) "ስውር ሰደቃ (በሚስጥር ለግስ)" else "Donate Anonymously (Secret Sadaqah)",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
          )
          Text(
            text = if (selectedLanguage == Language.AMHARIC) "ስምዎ በይፋ አይገለጽም፤ ለአላህ ብቻ የሚደረግ ሰደቃ" else "Your identity remains hidden from public reports",
            style = MaterialTheme.typography.bodySmall.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontSize = 11.sp
            )
          )
        }
      }

      if (!isAnonymous) {
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
          value = donorName,
          onValueChange = { donorName = it },
          modifier = Modifier.fillMaxWidth(),
          label = { Text(if (selectedLanguage == Language.AMHARIC) "ሙሉ ስም (አማራጭ)" else "Full Name (Optional)") },
          leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = EmeraldPrimary) },
          shape = RoundedCornerShape(12.dp),
          singleLine = true
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = donorPhone,
        onValueChange = { donorPhone = it },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(if (selectedLanguage == Language.AMHARIC) "ስልክ ቁጥር (የደረሰኝ ኤስኤምኤስ ለመላክ)" else "Phone Number (for SMS Receipt)") },
        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = EmeraldPrimary) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = intentionNote,
        onValueChange = { intentionNote = it },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(if (selectedLanguage == Language.AMHARIC) "መልእክት ወይም ዱዓ (Dua / Note)" else "Intention note / Dua request") },
        shape = RoundedCornerShape(12.dp),
        maxLines = 3
      )
    }

    // 5. Ethiopian Payment Provider Selection & Official Account Details
    item {
      Text(
        text = if (selectedLanguage == Language.AMHARIC) "የኢትዮጵያ የክፍያ መንገድ ምረጥ *" else "Ethiopian Payment Provider *",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )

      Spacer(modifier = Modifier.height(6.dp))

      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(PaymentProvider.values()) { provider ->
          val isSelected = selectedProvider == provider
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSelected) Color(provider.colorHex) else MaterialTheme.colorScheme.surface)
              .border(
                1.dp,
                if (isSelected) Color(provider.colorHex) else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
              )
              .clickable { selectedProvider = provider }
              .padding(horizontal = 14.dp, vertical = 10.dp)
          ) {
            Text(
              text = if (selectedLanguage == Language.ENGLISH) provider.nameEn else provider.nameAm,
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Payment Account Details Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = selectedProvider.nameAm,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = selectedProvider.accountHolder,
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
              )
            }
            IconButton(
              onClick = {
                clipboardManager.setText(AnnotatedString(selectedProvider.accountNumber))
                Toast.makeText(context, "ቁጥሩ ተገልብጧል (Copied)", Toast.LENGTH_SHORT).show()
              }
            ) {
              Icon(Icons.Default.ContentCopy, contentDescription = "Copy Account Number", tint = EmeraldPrimary)
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(EmeraldPastel)
              .padding(horizontal = 12.dp, vertical = 8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = selectedProvider.accountNumber,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = EmeraldDark,
                  letterSpacing = 1.sp
                )
              )
              Text(
                text = if (selectedLanguage == Language.AMHARIC) "ኮፒ አድርግ" else "Copy",
                style = MaterialTheme.typography.labelSmall.copy(color = EmeraldPrimary, fontWeight = FontWeight.Bold)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = if (selectedLanguage == Language.ENGLISH) selectedProvider.instructionEn else selectedProvider.instructionAm,
            style = MaterialTheme.typography.bodySmall.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 16.sp
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Transaction Reference Input
      OutlinedTextField(
        value = paymentRefInput,
        onValueChange = { paymentRefInput = it },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("payment_reference_input"),
        label = {
          Text(
            if (selectedLanguage == Language.AMHARIC)
              "የክፍያ ማረጋገጫ ቁጥር (Transaction ID / Ref) *"
            else
              "Transaction ID / Bank Ref Code *"
          )
        },
        placeholder = { Text("ምሳሌ: TB2611... ወይም FT263...") },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = EmeraldPrimary
        ),
        singleLine = true
      )

      if (errorMessage != null) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = errorMessage ?: "",
          color = MaterialTheme.colorScheme.error,
          style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
        )
      }
    }

    // 6. Submit Button
    item {
      Spacer(modifier = Modifier.height(8.dp))

      Button(
        onClick = {
          val amt = amountText.toDoubleOrNull()
          if (amt == null || amt <= 0) {
            errorMessage = if (selectedLanguage == Language.AMHARIC) "እባክዎ ትክክለኛ የብር መጠን ያስገቡ" else "Please enter a valid amount"
            return@Button
          }
          if (paymentRefInput.isBlank()) {
            errorMessage = if (selectedLanguage == Language.AMHARIC) "እባክዎ ከቴሌብር ወይም ከባንክ የተላከውን የደረሰኝ ቁጥር (Transaction ID) ያስገቡ" else "Please enter your Telebirr or Bank Transaction reference ID"
            return@Button
          }

          errorMessage = null
          onSubmitDonation(
            amt,
            if (isAnonymous) "ስውር ሰደቃ" else donorName,
            isAnonymous,
            donorPhone,
            selectedNiyyah,
            intentionNote,
            selectedProvider,
            paymentRefInput.trim()
          )
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("submit_sadaqah_button"),
        colors = ButtonDefaults.buttonColors(
          containerColor = GoldPrimary,
          contentColor = Color.White
        ),
        shape = RoundedCornerShape(14.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
      ) {
        Icon(Icons.Default.VolunteerActivism, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (selectedLanguage == Language.AMHARIC) "ሰደቃውን አጠናቅቅ (Submit Sadaqah)" else "Confirm & Submit Sadaqah",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          )
        )
      }
    }
  }
}
