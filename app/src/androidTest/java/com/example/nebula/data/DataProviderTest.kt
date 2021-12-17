package com.example.nebula.data

import androidx.test.platform.app.InstrumentationRegistry
import com.example.nebula.data.repository.ImageRepository
import org.junit.Test

import org.junit.Assert.*

class DataProviderTest {

    @Test
    fun fetchImages_shouldParseJson() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val images = ImageRepository().fetchImages(appContext)
        assertNotEquals("Failed to parse images",0,images.size)
    }
}