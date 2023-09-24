package com.nao4j.otus.kask.web

import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.server.testing.testApplication
import kotlin.test.assertEquals
import org.junit.Test

class ApplicationTest {

    @Test
    fun `root endpoint`() = testApplication {
        val response = client.get("/")
        assertEquals(expected = NotFound, actual = response.status)
    }
}
