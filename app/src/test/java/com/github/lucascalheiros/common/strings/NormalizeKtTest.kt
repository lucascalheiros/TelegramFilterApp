package com.github.lucascalheiros.common.strings

import org.junit.Assert.assertEquals
import org.junit.Test

class NormalizeKtTest {

    @Test
    fun `string should be normalized`() {
        assertEquals("Coracao", "Coração".normalizeString())
    }

    @Test
    fun `string should be normalized 2`() {
        assertEquals("aeEiUc", "àéÈìÙç".normalizeString())
    }

}