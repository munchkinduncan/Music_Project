package com.zetcode

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import java.io.File

fun readfromfile(filename: String, lineNum: Int): List<String> {
    val allLines = mutableListOf<String>()

    val fileName = filename

    val lines: List<String> = File(fileName).readLines()

    lines.forEach { line -> allLines.add(line) }

    return allLines[lineNum - 1].split(", ")

}

fun writetofile(fileName: String, listappendableusernames: List<String>, listappendablenumbers: List<Int>) {
    val myfile = File(fileName)

    myfile.printWriter().use { out ->


        for (y in 0..listappendableusernames.size - 1) {
            out.print(listappendableusernames[y])
            out.print(" ")
        }
        out.println("")
        for (element in listappendablenumbers) {
            out.print(element)
            out.print(" ")
        }
    }

}

fun locate(x: String?, y: String?, z: List<String>, a: List<String>): Boolean {
    var location = 0
    for (i in z.indices) {
        if (z[i] == x) {
            location = i

        }
    }
    if (a[location] == y) {
        return true
    }
    return false
}


fun main() {

    Database.connect(
            "jdbc:sqlite:database/users.db",
            "org.sqlite.JDBC"
    )

    serializableTransaction {
        SchemaUtils.create(UwUElephant)
    }


    var randomnumber: Int
    var failed = false
    var eachArtist: List<String>
    var eachSong: List<String>
    var artistGuess: String
    var songGuess: String
    var score = 0
    val artists = readfromfile("C:\\Users\\08dbi\\Documents\\artistsandusers.txt", 1)
    val songs = readfromfile("C:\\Users\\08dbi\\Documents\\artistsandusers.txt", 2)
    println("Enter your username: ")
    val username = readLine()!!
    println("Enter your password: ")
    val password = readLine()!!
    if (authorisedUser(username, password)) {
        while (!failed) {
            randomnumber = (artists.indices).random()
            eachArtist = artists[randomnumber].split(" ")
            println("The first letter/s of the artist's names are: ")
            for (x in eachArtist.indices) {
                print(eachArtist[x][0] + ".".repeat(eachArtist[x].length - 1) + " ")
            }
            eachSong = songs[randomnumber].split(" ")
            print("\nThe first letter/s of the song's names are: \n")
            for (q in eachSong.indices) {
                print(eachSong[q][0] + ".".repeat(eachSong[q].length - 1) + " ")
            }
            print("\nEnter your guess for the artist: \n")
            artistGuess = readLine().toString()
            println("Enter your guess for the song: ")
            songGuess = readLine().toString()
            if (artistGuess.toLowerCase() == artists[randomnumber].toLowerCase() && songGuess.toLowerCase() == songs[randomnumber].toLowerCase()) { {
                score += 3
                println("You scored 3 points")
            }
            else {
                println("Incorrect")
                println("Enter your guess for the artist: ")
                artistGuess = readLine().toString()
                println("Enter your guess for the song: ")
                songGuess = readLine().toString()
                if (artistGuess.toLowerCase() == artists[randomnumber].toLowerCase() && songGuess.toLowerCase() == songs[randomnumber].toLowerCase()) {
                    score += 1
                    println("You scored 1 point")
                }
                else {
                    failed = true
                    println("The correct artist was " + artists[randomnumber])
                    println("The correct song was " + songs[randomnumber])
                }

            }
        }

        println("You scored $score points")
        updateHighScore(username, score)
        for ((username, score) in Gethighscores()) {
            println("$username - $score")

        }
    } else {
        println("Unauthorized user")

    }
}
