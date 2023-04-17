package com.example.data.models

data class NoteView(
    val title: String,
    val comment: String,
    val count: Int
)

val noteViewMockList = listOf(
    NoteView(
        title = "Grades",
        comment = "Rate and get recommendations",
        count = 0
    ),
    NoteView(
        title = "Lists",
        comment = "Add to lists",
        count = 4
    ),
    NoteView(
        title = "Comments",
        comment = "No reviews yet",
        count = 0
    )
)