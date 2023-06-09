package com.example.login.helpers

/**
 * The Patterns class defines regular expression patterns.
 */
class Patterns {

    companion object {

        // The regular expression ensures email addresses are valid.
        val emailPattern = "^[^@]{3,}@[^@\\.]{3,}\\.[^@\\.]{2,}$".toRegex()

        // The regular expression ensures passwords are
        // at least 8 characters long and include at least one uppercase letter,
        // one lowercase letter, one digit, and one special character.
        val passwordPattern =
            "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$".toRegex()

        // The userNamePattern regular expression ensures usernames contain
        // only letters, spaces, apostrophes, dashes, and commas.
        val userNamePattern = "^[\\p{L}\\s.’\\-,]+\$".toRegex()
    }

}