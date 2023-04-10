package com.example.home.data.api

/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
/**
 * Represents the possible states of a JSON request in the IMDB application.
 *
 * The possible states are:
 * - Init: represents the initial state of the request, before it has started.
 * - Loading: represents the state of the request when it's in progress.
 * - Success: represents the state of the request when it has successfully completed,
 *   and contains the result of the request.
 * - Error: represents the state of the request when it has failed, and contains the error message.
 *
 * @param T the type of data returned by the JSON request in the Success state.
 *
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
sealed class IMDBState<T> {

    /**
     * Represents the initial state of the JSON request, before it has started.
     */
    class Init<T> : IMDBState<T>()

    /**
     * Represents the state of the JSON request when it's in progress.
     */
    class Loading<T> : IMDBState<T>()

    /**
     * Represents the state of the JSON request when it has successfully completed,
     * and contains the result of the request.
     *
     * @param message an optional message.
     * @param data the result of the request.
     */
    data class Success<T>(
        val message: String?,
        val data: T
    ) : IMDBState<T>()

    /**
     * Represents the state of the JSON request when it has failed, and contains the error message.
     *
     * @param errorMessage the error message returned by the request.
     */
    data class Error<T>(
        val errorMessage: String
    ) : IMDBState<T>()
}
