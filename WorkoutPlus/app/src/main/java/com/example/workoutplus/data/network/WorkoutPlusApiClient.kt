package com.example.workoutplus.data.network

import com.example.workoutplus.data.model.DiaryEntryModel
import com.example.workoutplus.data.model.ExerciseInfoModel
import com.example.workoutplus.data.model.ExerciseModel
import com.example.workoutplus.data.model.LoginRequestModel
import com.example.workoutplus.data.model.LoginResponseModel
import com.example.workoutplus.data.model.MessageResponseModel
import com.example.workoutplus.data.model.SessionModel
import com.example.workoutplus.data.model.UserModel
import com.example.workoutplus.data.model.WorkoutModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.time.LocalDate
import java.util.Date

interface WorkoutPlusApiClient {

    //ExerciseInfo endpoints
    @GET("ExerciseInfo")
    suspend fun getExercises(): Response<List<ExerciseInfoModel>>

    @GET("ExerciseInfo/byCategory/{category}")
    suspend fun getExercisesByCategory(
        @Path("category") category: String
    ): Response<List<ExerciseInfoModel>>

    @GET("ExerciseInfo/byName/{name}")
    suspend fun getExercisesByName(
        @Path("name") name: String
    ): Response<List<ExerciseInfoModel>>

    //Login
    @POST("Login")
    suspend fun login(
        @Body login: LoginRequestModel
    ): Response<LoginResponseModel>

    //User
    @GET("User/{id}")
    suspend fun getUserById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<UserModel>

    @DELETE("User")
    suspend fun delUserById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<MessageResponseModel>

    @POST("User")
    suspend fun postUser(
        @Body user: UserModel
    ): Response<UserModel>

    @PUT("User")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Body user: UserModel
    ): Response<UserModel>

    //Workout
    @GET("Workout")
    suspend fun getWorkouts(
    ): Response<List<WorkoutModel>>

    @GET("Workout/ByUser/{userId}")
    suspend fun getWorkoutsByUser(
        @Path("userId") userId: Int
    ): Response<List<WorkoutModel>>

    @GET("Workout/ById/{id}")
    suspend fun getWorkoutById(
        @Path("id") id: Int
    ): Response<WorkoutModel>

    @POST("Workout")
    suspend fun postWorkout(
        @Body workout: WorkoutModel
    ): Response<WorkoutModel>

    @POST("Workout/Suscrive/{userId}/{workoutId}")
    suspend fun suscribeWorkout(
        @Path("userId") userId: Int,
        @Path("workoutId") workoutId: Int
    ): Response<MessageResponseModel>

    @PUT("Workout")
    suspend fun updateWorkout(
        @Body workout: WorkoutModel
    ): Response<WorkoutModel>

    @DELETE("Workout/{id}")
    suspend fun delWorkout(
        @Path("id") id: Int
    ): Response<MessageResponseModel>

    //Diary
    @GET("Diary/byUser/{id}")
    suspend fun getEntriesByUser(
        @Path("id") id: Int
    ): Response<List<DiaryEntryModel>>

    @GET("Diary/byDate/{id}/{date}")
    suspend fun getEntriesByDate(
        @Path("id") id: Int,
        @Path("date") date: LocalDate
    ): Response<List<DiaryEntryModel>>

    @POST("Diary")
    suspend fun postEntry(
        @Body entry: DiaryEntryModel
    ): Response<MessageResponseModel>

    //Session
    @POST("Session")
    suspend fun postSession(
        @Body session: SessionModel
    ): Response<SessionModel>

    @PUT("Session")
    suspend fun updateSession(
        @Body session: SessionModel
    ): Response<SessionModel>

    @DELETE("Session/{id}")
    suspend fun delSession(
        @Path("id") id: Int
    ): Response<MessageResponseModel>

    //Exercise
    @POST("Exercise")
    suspend fun postExercise(
        @Body exercise: ExerciseModel
    ): Response<ExerciseModel>

    @PUT("Exercise")
    suspend fun updateExercise(
        @Body exercise: ExerciseModel
    ): Response<ExerciseModel>

    @DELETE("Exercise/{id}")
    suspend fun delExercise(
        @Path("id") id: Int
    ): Response<MessageResponseModel>
}