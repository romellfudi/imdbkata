package com.example.data.models

data class NoteModel(
    val title: String,
    val comment: String,
    val count: Int
)

val noteModelList = listOf(
    NoteModel(
        title = "Grades",
        comment = "Rate and get recommendations",
        count = 0
    ),
    NoteModel(
        title = "Lists",
        comment = "Add to lists",
        count = 4
    ),
    NoteModel(
        title = "Comments",
        comment = "No reviews yet",
        count = 0
    )
)
