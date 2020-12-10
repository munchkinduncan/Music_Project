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
    // Locates different elements of the text file
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

    //Declaring all the variables used as 'var' to allow for changes
    var randomnumber: Int
    var failed = false
    var eachArtist: List<String>
    var eachSong: List<String>
    var artistGuess: String
    var songGuess: String
    var score = 0
    val artists = readfromfile("C:\\Users\\08dbi\\Documents\\artistsandusers.txt", 1) // Creates a list of the artists, based on the line of the file
    val songs = readfromfile("C:\\Users\\08dbi\\Documents\\artistsandusers.txt", 2) // Creates a list of the songs, based on the line of the file
    println("Enter your username: ")
    val username = readLine()!!
    println("Enter your password: ")
    val password = readLine()!!  // !! Is used to prevent errors relating to null input
    if (authorisedUser(username, password)) { // Ensures that the game will only occur if validation is correct
        while (!failed) { // Ensures that the program keeps going until the user fails to guess correctly
            randomnumber = (artists.indices).random() 
            eachArtist = artists[randomnumber].split(" ") // Selects the artists
            println("The first letter/s of the artist's names are: ")
            for (x in eachArtist.indices) {
                print(eachArtist[x][0] + ".".repeat(eachArtist[x].length - 1) + " ")
            }
            eachSong = songs[randomnumber].split(" ") // Selects the songs
            print("\nThe first letter/s of the song's names are: \n")
            for (q in eachSong.indices) { 
                print(eachSong[q][0] + ".".repeat(eachSong[q].length - 1) + " ") // Prints the first letter of each song
            }
            print("\nEnter your guess for the artist: \n")
            artistGuess = readLine().toString()
            println("Enter your guess for the song: ")
            songGuess = readLine().toString()
            if (artistGuess.toLowerCase() == artists[randomnumber].toLowerCase() && songGuess.toLowerCase() == songs[randomnumber].toLowerCase()) { { //Checks if the first guess is valid
                score += 3
                println("You scored 3 points")
            }
            else {
                println("Incorrect")
                println("Enter your guess for the artist: ")
                artistGuess = readLine().toString()
                println("Enter your guess for the song: ")
                songGuess = readLine().toString()
                if (artistGuess.toLowerCase() == artists[randomnumber].toLowerCase() && songGuess.toLowerCase() == songs[randomnumber].toLowerCase()) { // Makes all the data lowercase to allow any capitalization and checks if the second guess is valid.
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
        for ((username, score) in Gethighscores()) { // Iterates through the database
            println("$username - $score")

        }
    } else {
        println("Unauthorized user") // Tells the user whether the username and password was validated and if not the program ends

    }
}
