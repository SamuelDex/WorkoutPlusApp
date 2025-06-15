package com.example.workoutplus.data

import com.example.workoutplus.data.model.DiaryEntryModel
import com.example.workoutplus.data.model.ExerciseInfoModel
import com.example.workoutplus.data.model.ExerciseModel
import com.example.workoutplus.data.model.LoginRequestModel
import com.example.workoutplus.data.model.LoginResponseModel
import com.example.workoutplus.data.model.MessageResponseModel
import com.example.workoutplus.data.model.SessionModel
import com.example.workoutplus.data.model.UserModel
import com.example.workoutplus.data.model.WorkoutModel
import com.example.workoutplus.data.network.WorkoutPlusApiService
import java.time.LocalDate
import java.util.Date

class WorkoutPlusRepository {

    private val service = WorkoutPlusApiService()

    suspend fun getExercises(): Result<List<ExerciseInfoModel>> {
        return service.getExercises()
    }

    suspend fun getExercisesByCategory(category: String): Result<List<ExerciseInfoModel>> {
        return service.getExercisesByCategory(category)
    }

    suspend fun getExercisesByName(name: String): Result<List<ExerciseInfoModel>> {
        return service.getExercisesByName(name)
    }

    suspend fun login(login: LoginRequestModel): Result<LoginResponseModel> {
        return service.login(login)
    }

    suspend fun getUserById(token: String, id: Int): Result<UserModel> {
        return service.getUserById(token, id)
    }

    suspend fun delUserById(token: String, id: Int): Result<MessageResponseModel> {
        return service.delUserById(token, id)
    }

    suspend fun postUser(user: UserModel): Result<UserModel> {
        return service.postUser(user)
    }

    suspend fun updateUser(token: String, user: UserModel): Result<UserModel> {
        return service.updateUser(token, user)
    }

    suspend fun getWorkouts(): Result<List<WorkoutModel>> {
        return service.getWorkouts()
    }

    suspend fun getWorkoutsByUser(userId: Int): Result<List<WorkoutModel>> {
        return service.getWorkoutsByUser(userId)
    }

    suspend fun getWorkoutById(id: Int): Result<WorkoutModel> {
        return service.getWorkoutById(id)
    }

    suspend fun postWorkout(workout: WorkoutModel): Result<WorkoutModel> {
        return service.postWorkout(workout)
    }

    suspend fun suscribeWorkout(userId: Int, workoutId: Int): Result<MessageResponseModel> {
        return service.suscribeWorkout(userId, workoutId)
    }

    suspend fun updateWorkout(workout: WorkoutModel): Result<WorkoutModel> {
        return service.updateWorkout(workout)
    }

    suspend fun delWorkout(id: Int): Result<MessageResponseModel> {
        return service.delWorkout(id)
    }

    suspend fun postSession(session: SessionModel): Result<SessionModel> {
        return service.postSession(session)
    }

    suspend fun updateSession(session: SessionModel): Result<SessionModel> {
        return service.updateSession(session)
    }

    suspend fun delSession(id: Int): Result<MessageResponseModel> {
        return service.delSession(id)
    }

    suspend fun postExercise(exercise: ExerciseModel): Result<ExerciseModel> {
        return service.postExercise(exercise)
    }

    suspend fun updateExercise(exercise: ExerciseModel): Result<ExerciseModel> {
        return service.updateExercise(exercise)
    }

    suspend fun delExercise(id: Int): Result<MessageResponseModel> {
        return service.delExercise(id)
    }

    suspend fun getEntriesByUser(id: Int): Result<List<DiaryEntryModel>> {
        return service.getEntriesByUser(id)
    }

    suspend fun getEntriesByDate(id: Int, date: LocalDate): Result<List<DiaryEntryModel>> {
        return service.getEntriesByDate(id, date)
    }

    suspend fun postEntry(entry: DiaryEntryModel): Result<MessageResponseModel> {
        return service.postEntry(entry)
    }

}