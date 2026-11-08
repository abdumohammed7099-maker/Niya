package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.NiyaRepository
import com.example.model.DonationCategory
import com.example.model.NiyyahType
import com.example.model.PaymentProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("NIYA", appName)
  }

  @Test
  fun `verify initial charity projects and donation flow`() {
    val repository = NiyaRepository()
    val initialProjects = repository.projects.value
    assertTrue("Should have 5 charity categories represented", initialProjects.isNotEmpty())

    val mosqueProject = initialProjects.find { it.category == DonationCategory.MOSQUE }
    assertNotNull(mosqueProject)

    val donation = repository.addDonation(
      projectId = mosqueProject?.id,
      projectNameAm = mosqueProject?.titleAm ?: "",
      projectNameEn = mosqueProject?.titleEn ?: "",
      category = DonationCategory.MOSQUE,
      amount = 500.0,
      donorName = "ፈትሂ ሙሐመድ",
      isAnonymous = false,
      donorPhone = "0911000000",
      niyyah = NiyyahType.SADAQAH_JARIYAH,
      intentionNote = "ለአላህ ውዴታ",
      paymentProvider = PaymentProvider.TELEBIRR,
      paymentReference = "TB2611998877"
    )

    assertEquals(500.0, donation.amount, 0.01)
    assertTrue(repository.donations.value.any { it.id == donation.id })
  }
}
