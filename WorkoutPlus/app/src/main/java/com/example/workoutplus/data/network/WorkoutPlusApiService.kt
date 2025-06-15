package com.example.workoutplus.data.network

import com.example.workoutplus.core.RetrofitHelper
import com.example.workoutplus.data.model.DiaryEntryModel
import com.example.workoutplus.data.model.ExerciseInfoModel
import com.example.workoutplus.data.model.ExerciseModel
import com.example.workoutplus.data.model.LoginRequestModel
import com.example.workoutplus.data.model.LoginResponseModel
import com.example.workoutplus.data.model.MessageResponseModel
import com.example.workoutplus.data.model.SessionModel
import com.example.workoutplus.data.model.UserModel
import com.example.workoutplus.data.model.WorkoutModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.Date

class WorkoutPlusApiService {

    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getExercises(): Result<List<ExerciseInfoModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).getExercises()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getExercisesByCategory(category: String): Result<List<ExerciseInfoModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).getExercisesByCategory(category)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getExercisesByName(name: String): Result<List<ExerciseInfoModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).getExercisesByName(name)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun login(login: LoginRequestModel): Result<LoginResponseModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).login(login)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorCode = response.code()
                    val errorBody = response.errorBody()?.string()

                    val message = when (errorCode) {
                        400 -> "Solicitud incorrecta (400). Verifica los datos ingresados."
                        401 -> "Usuario o contraseña incorrecto (401)."
                        403 -> "Acceso prohibido (403)."
                        404 -> "Servicio no encontrado (404)."
                        500 -> "Error del servidor (500). Intenta más tarde."
                        else -> "Error desconocido ($errorCode): $errorBody"
                    }
                    Result.failure(Exception("Error: $message"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getUserById(token: String, id: Int): Result<UserModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).getUserById(token, id)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun delUserById(token: String, id: Int): Result<MessageResponseModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).delUserById(token, id)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun postUser(user: UserModel): Result<UserModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).postUser(user)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun updateUser(token: String, user: UserModel): Result<UserModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).updateUser(token, user)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getWorkouts(): Result<List<WorkoutModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).getWorkouts()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getWorkoutsByUser(userId: Int): Result<List<WorkoutModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).getWorkoutsByUser(userId)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getWorkoutById(id: Int): Result<WorkoutModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).getWorkoutById(id)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun postWorkout(workout: WorkoutModel): Result<WorkoutModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).postWorkout(workout)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun suscribeWorkout(userId: Int, workoutId: Int): Result<MessageResponseModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).suscribeWorkout(userId, workoutId)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun updateWorkout(workout: WorkoutModel): Result<WorkoutModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).updateWorkout(workout)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun delWorkout(id: Int): Result<MessageResponseModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).delWorkout(id)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun postSession(session: SessionModel): Result<SessionModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).postSession(session)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun updateSession(session: SessionModel): Result<SessionModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).updateSession(session)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun delSession(id: Int): Result<MessageResponseModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).delSession(id)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun postExercise(session: ExerciseModel): Result<ExerciseModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).postExercise(session)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun updateExercise(exercise: ExerciseModel): Result<ExerciseModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).updateExercise(exercise)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun delExercise(id: Int): Result<MessageResponseModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).delExercise(id)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getEntriesByUser(id: Int): Result<List<DiaryEntryModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).getEntriesByUser(id)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getEntriesByDate(id: Int, date: LocalDate): Result<List<DiaryEntryModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).getEntriesByDate(id, date)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun postEntry(entry: DiaryEntryModel): Result<MessageResponseModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(WorkoutPlusApiClient::class.java).postEntry(entry)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Cuerpo de respuesta vacío"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Result.failure(Exception("Error ${response.code()}: $errorBody"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}