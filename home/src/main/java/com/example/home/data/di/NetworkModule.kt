package com.example.home.data.di

import com.example.home.data.api.Api
import com.example.home.data.api.IMDBState
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Singleton

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRavenApi(moshi: Moshi): Api {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(FlowResourceCallAdapterFactory())
            .build()
            .create(Api::class.java)
    }

}

/**
 * A class that implements the CallAdapter.Factory interface for Retrofit
 * and allows the use of Kotlin Flow with a specific IMDBState type
 */
class FlowResourceCallAdapterFactory(private val isSelfExceptionHandling: Boolean = true) :
    CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit) =
        if (getRawType(returnType) == Flow::class.java) {
            val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
            val rawObservableType = getRawType(observableType)
            require(rawObservableType == IMDBState::class.java) { "type must be a resource" }
            require(observableType is ParameterizedType) { "resource must be parameterized" }
            val bodyType = getParameterUpperBound(0, observableType)
            FlowResourceCallAdapter<Any>(bodyType, isSelfExceptionHandling)
        } else null
}

/**
 * A class that adapts a Retrofit call to a Flow of IMDBState,
 * allowing to handle responses as states (Loading, Success, and Error) and handle exceptions.
 */
class FlowResourceCallAdapter<R>(
    private val responseType: Type,
    private val isSelfExceptionHandling: Boolean
) : CallAdapter<R, Flow<IMDBState<R>>> {

    override fun responseType() = responseType

    @ExperimentalCoroutinesApi
    override fun adapt(call: Call<R>) = flow<IMDBState<R>> {
        emit(IMDBState.Loading())
        val resp = call.awaitResponse()
        when {
            resp.isSuccessful -> resp.body()?.let { data -> emit(IMDBState.Success(null, data)) }
                ?: emit(IMDBState.Error("Response can't be null"))
            else -> emit(IMDBState.Error(resp.message()))
        }
    }.catch { error ->
        if (isSelfExceptionHandling) emit(IMDBState.Error(error.message ?: "Something went wrong"))
        else throw error
    }

}