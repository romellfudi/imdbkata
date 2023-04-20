package com.example.home.ui.dataview

import com.example.data.models.Movie


/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
data class MovieView(
    val adult: Boolean,
    val backdropPath: String?,
    val genreIds: List<Int>,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    val posterUrl: String,
    val tileUrl: String,
    val isFav: Boolean = false
)

fun Movie.toMovieView(): MovieView {
    return MovieView(
        adult = adult,
        backdropPath = backdropPath,
        genreIds = genreIds,
        id = id,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        posterUrl = posterUrl,
        tileUrl = tileUrl
    )
}

val movieViewFList = listOf(
    MovieView(
        adult = false,
        backdropPath = "/2Eewgp7o5AU1xCataDmiIL2nYxd.jpg",
        genreIds = listOf(1, 2, 3),
        id = 943822,
        originalLanguage = "en",
        originalTitle = "Prizefighter: The Life of Jem Belcher",
        overview = "At the turn of the 19th century, Pugilism was the sport of kings and a gifted young boxer fought his way to becoming champion of England.",
        popularity = 1077.864,
        posterPath = "/x3PIk93PTbxT88ohfeb26L1VpZw.jpg",
        posterUrl = "https://image.tmdb.org/t/p/w500/x3PIk93PTbxT88ohfeb26L1VpZw.jpg",
        releaseDate = "2022-06-30",
        tileUrl = "https://image.tmdb.org/t/p/w500/2Eewgp7o5AU1xCataDmiIL2nYxd.jpg",
        title = "Prizefighter: The Life of Jem Belcher",
        video = false,
        voteAverage = 6.2,
        voteCount = 115
    ),
    MovieView(
        adult = false,
        backdropPath = "/7eccX0xay9pDj6ZQvU4cu3whw18.jpg",
        genreIds = listOf(1, 2, 3),
        id = 1003579,
        originalLanguage = "en",
        originalTitle = "Batman: The Doom That Came to Gotham",
        overview = "Explorer Bruce Wayne accidentally unleashes an ancient evil, and returns to Gotham after being away for two decades. There, Batman battles Lovecraftian supernatural forces and encounters allies and enemies such as Green Arrow, Ra's al Ghul, Mr. Freeze, Killer Croc, Two-Face and James Gordon.",
        popularity = 837.958,
        posterPath = "/hrATQE8ScQceohwInaMluluNEaf.jpg",
        posterUrl = "https://image.tmdb.org/t/p/w500/hrATQE8ScQceohwInaMluluNEaf.jpg",
        releaseDate = "2023-03-10",
        tileUrl = "https://image.tmdb.org/t/p/w500/7eccX0xay9pDj6ZQvU4cu3whw18.jpg",
        title = "Batman: The Doom That Came to Gotham",
        video = false,
        voteAverage = 6.464,
        voteCount = 69
    )
)

val movieViewRVList = listOf(
    MovieView(
        adult = false,
        backdropPath = "/8QMWSEeotCy7dhB8EAZgCuNaQtb.jpg",
        genreIds = listOf(1, 2, 3),
        id = 850871,
        originalLanguage = "es",
        originalTitle = "Sayen",
        overview = "Sayen is hunting down the men who murdered her grandmother. Using her training and knowledge of nature, she is able to turn the tables on them, learning of a conspiracy from a corporation that threatens her people's ancestral lands.",
        popularity = 680.149,
        posterPath = "/aCy61aU7BMG7SfhkaAaasS0KzUO.jpg",
        posterUrl = "https://image.tmdb.org/t/p/w500/aCy61aU7BMG7SfhkaAaasS0KzUO.jpg",
        releaseDate = "2023-03-03",
        tileUrl = "https://image.tmdb.org/t/p/w500/8QMWSEeotCy7dhB8EAZgCuNaQtb.jpg",
        title = "Sayen",
        video = false,
        voteAverage = 6.24,
        voteCount = 73
    ),
    MovieView(
        adult = false,
        backdropPath = "/wD2kUCX1Bb6oeIb2uz7kbdfLP6k.jpg",
        genreIds = listOf(1, 2, 3),
        id = 980078,
        originalLanguage = "en",
        originalTitle = "Winnie the Pooh: Blood and Honey",
        overview = "Christopher Robin is headed off to college and he has abandoned his old friends, Pooh and Piglet, which then leads to the duo embracing their inner monsters.",
        popularity = 1520.221,
        posterPath = "/fNTtVbqI92abEKAgz2ynurCUne.jpg",
        posterUrl = "https://image.tmdb.org/t/p/w500/fNTtVbqI92abEKAgz2ynurCUne.jpg",
        releaseDate = "2023-01-27",
        tileUrl = "https://image.tmdb.org/t/p/w500/wD2kUCX1Bb6oeIb2uz7kbdfLP6k.jpg",
        title = "Winnie the Pooh: Blood and Honey",
        video = false,
        voteAverage = 5.777,
        voteCount = 448
    ),
    MovieView(
        adult = false,
        backdropPath = "/xjixln7CCUOgAJNX8RrGxYRwLvI.jpg",
        genreIds = listOf(1, 2, 3),
        id = 948050,
        originalLanguage = "fr",
        originalTitle = "Le Roi des Ombres",
        overview = "After the death of their father, two half-brothers find themselves on opposite sides of an escalating conflict with tragic consequences.",
        popularity = 131.299,
        posterPath = "/ooyH5mGWvN3ZJOSqTBntlQ8Iail.jpg",
        posterUrl = "https://image.tmdb.org/t/p/w500/ooyH5mGWvN3ZJOSqTBntlQ8Iail.jpg",
        releaseDate = "2023-03-17",
        tileUrl = "https://image.tmdb.org/t/p/w500/xjixln7CCUOgAJNX8RrGxYRwLvI.jpg",
        title = "In His Shadow",
        video = false,
        voteAverage = 6.3,
        voteCount = 62
    )
)