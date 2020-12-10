package com.zetcode

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

fun <T> serializableTransaction(block: Transaction.() -> T): T {
    return transaction(Connection.TRANSACTION_SERIALIZABLE, 1, null, block)
}

fun authorisedUser(username: String, password: String): Boolean {
    return serializableTransaction {
        val y = UwUElephant.select {
            UwUElephant.username eq username
        }.firstOrNull()
        if (y == null) {
            false
        } else {
            y[UwUElephant.password] == password
        }
    }

}

fun updateHighScore(username: String, score: Int) {
    serializableTransaction {
        UwUElephant.update({ (UwUElephant.username eq username) and (UwUElephant.scores less score) }) {
            it[scores] = score
        }
    }
}

fun Gethighscores(): List<Pair<String, Int>> {

    return serializableTransaction {
        UwUElephant.selectAll().orderBy(UwUElephant.scores, order = SortOrder.DESC).limit(5).map {
            it[UwUElephant.username] to it[UwUElephant.scores]
        }
    }
}



