package com.matildaerenius.bookmap

import org.junit.Test
import java.net.URL

class BookScraper {
    fun fetchBookId(isbn: String) {

        val apiURL = "https://api.bookbeat.com/api/search/books?query=$isbn"

        try {
            val responseJson = URL(apiURL).readText()
            val idRegex = """"id"\s*:\s*([0-9]+)""".toRegex()
            val match = idRegex.find(responseJson)

            if (match != null) {
                val bookId = match.groupValues[1]
                println("✅ Hittade boken ($isbn). Bok-ID är: $bookId")
            } else {
                println("❌ Kunde inte hitta nån bok för: $isbn")
            }
        } catch (e: Exception) {
            println("⚠️ Något gick fel med $isbn: ${e.message}")
        }
    }

    @Test
    fun runScraper() {
        val isbnLista = listOf(
            "9789173489515",
            "9789100114060",
            "9789174332216",
            "9789173486330",
            "9789173486323",
            "9789173482707",
            "9789173482028",
            "9789173483421",
            "9789176517178",
            "9789173484251",
            "9789173484435",
            "9789173484732",
            "9789176518175",
            "9789178270514",
            "9789127144415",
            "9789100153830",
            "9789127137295",
            "9789173481281",
            "9789173484237"
        )

        println("Startar sökning för ${isbnLista.size} böcker...")
        println("************************************************")

        for (isbn in isbnLista) {
            fetchBookId(isbn)
            Thread.sleep(1000)
        }

        println("************************************************")
        println("Klar med hela listan")
    }
}