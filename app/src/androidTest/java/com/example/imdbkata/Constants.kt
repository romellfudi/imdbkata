package com.example.imdbkata

const val userName = "Freddy"
val randomString = (1..5).map { ('a'..'z').random() }.joinToString("")
val badEmail = "${userName.toLowerCase()}.$randomString@example"
val goodEmail = "$badEmail.com"
const val goodPassword = "123qweASD!"
const val badPasswordNotEspecialCharacters = "123qweASD"
const val badPasswordNotCapitalizeLetters = "123qwe!@"
const val badPasswordOnlyNumbers = "12345678910"