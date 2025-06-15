package com.example.workoutplus.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutplus.data.WorkoutPlusRepository
import com.example.workoutplus.data.model.DiaryEntryModel
import com.example.workoutplus.data.model.ExerciseInfoModel
import com.example.workoutplus.data.model.ExerciseModel
import com.example.workoutplus.data.model.LoginRequestModel
import com.example.workoutplus.data.model.LoginResponseModel
import com.example.workoutplus.data.model.MessageResponseModel
import com.example.workoutplus.data.model.SessionModel
import com.example.workoutplus.data.model.UserModel
import com.example.workoutplus.data.model.WorkoutModel
import com.example.workoutplus.domain.DelWorkoutUseCase
import com.example.workoutplus.domain.GetPublicWorkoutsUseCase
import com.example.workoutplus.domain.GetWorkoutsByUserUseCase
import com.example.workoutplus.domain.LoginUseCase
import com.example.workoutplus.domain.PostWorkoutUseCase
import com.example.workoutplus.domain.RegisterUseCase
import com.example.workoutplus.domain.SuscribeWorkoutUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date

class WorkoutViewModel: ViewModel() {

    //MutableLiveData
    private val repository = WorkoutPlusRepository()
    val isLoading = MutableLiveData<Boolean>()
    val workoutList = MutableLiveData<List<WorkoutModel>>()
    val workout = MutableLiveData<WorkoutModel>()
    val publicWorkoutList = MutableLiveData<List<WorkoutModel>>()
    val user = MutableLiveData<UserModel>()
    val loginResponseModel = MutableLiveData<LoginResponseModel>()
    val diaryEntryList = MutableLiveData<List<DiaryEntryModel>>()
    val errorMessage = MutableLiveData<String>()
    val responseMessage = MutableLiveData<MessageResponseModel>()
    val exerciseInfoList = MutableLiveData<List<ExerciseInfoModel>>()
    val newSession = MutableLiveData<SessionModel>()
    val updatedSession = MutableLiveData<SessionModel>()
    val newExercise = MutableLiveData<ExerciseModel>()
    val updatedExercise = MutableLiveData<ExerciseModel>()
    val allActivity = MutableLiveData<List<DiaryEntryModel>>()
    val dailyActivity = MutableLiveData<List<DiaryEntryModel>>()

    //casos de uso
    var loginUseCase = LoginUseCase()
    var registerUseCase = RegisterUseCase()
    var getWorkoutsByUserUseCase = GetWorkoutsByUserUseCase()
    var getPublicWorkoutsUseCase = GetPublicWorkoutsUseCase()
    var suscribeWorkoutUseCase = SuscribeWorkoutUseCase()
    var delWorkoutUseCase = DelWorkoutUseCase()
    var postWorkoutUseCase = PostWorkoutUseCase()

    fun login(login: LoginRequestModel){
        isLoading.postValue(true)

        viewModelScope.launch {
            var result = loginUseCase(login)

            result.onSuccess {
                loginResponseModel.postValue(it)
                isLoading.postValue(false)
            }.onFailure {
                errorMessage.postValue(it.message)
                isLoading.postValue(false)
            }
        }
    }

    fun register(registerUser: UserModel){
        isLoading.postValue(true)

        viewModelScope.launch {
            var result = registerUseCase(registerUser)

            result.onSuccess {
                user.postValue(it)
                isLoading.postValue(false)
            }.onFailure {
                errorMessage.postValue(it.message)
                isLoading.postValue(false)
            }
        }
    }

    fun getWorkoutsByUser(userId: Int){
        viewModelScope.launch {
            var result = getWorkoutsByUserUseCase(userId)

            result.onSuccess {
                workoutList.postValue(it)
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun getPublicWorkouts(){
        viewModelScope.launch {
            var result = getPublicWorkoutsUseCase()

            result.onSuccess {
                publicWorkoutList.postValue(it)
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun getWorkoutById(id: Int){
        viewModelScope.launch {
            var result = repository.getWorkoutById(id)

            result.onSuccess {
                workout.postValue(it)
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun suscribeWorkouts(userId: Int, workoutId: Int){
        viewModelScope.launch {
            var result = suscribeWorkoutUseCase(userId, workoutId)

            result.onSuccess {
                responseMessage.postValue(it)
                getWorkoutsByUser(userId)
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun delWorkouts(userId: Int, workoutId: Int){
        viewModelScope.launch {
            var result = delWorkoutUseCase(workoutId)

            result.onSuccess {
                responseMessage.postValue(it)
                getWorkoutsByUser(userId)
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun postWorkout(workout: WorkoutModel){
        viewModelScope.launch {
            var result = postWorkoutUseCase(workout)

            result.onSuccess {
                val currentList = workoutList.value.toMutableList()
                currentList.add(it)
                workoutList.postValue(currentList)
                responseMessage.postValue(MessageResponseModel(true, "Rutina creada con exito"))
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun updateWorkout(workout: WorkoutModel){
        viewModelScope.launch {
            var result = repository.updateWorkout(workout)

            result.onSuccess {
                val currentList = workoutList.value.toMutableList()
                currentList.removeIf { it.id == workout.id }
                currentList.add(it)
                workoutList.postValue(currentList)
                responseMessage.postValue(MessageResponseModel(true, "Rutina actualizada con exito"))
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun postSession(newSession: SessionModel){
        viewModelScope.launch {
            var result = repository.postSession(newSession)

            result.onSuccess {
                this@WorkoutViewModel.newSession.postValue(it)
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun updateSession(updatedSession: SessionModel){
        viewModelScope.launch {
            var result = repository.updateSession(updatedSession)

            result.onSuccess {
                this@WorkoutViewModel.updatedSession.postValue(it)
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun delSession(id: Int){
        viewModelScope.launch {
            val result = repository.delSession(id)

            result.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun postExercise(newExercise: ExerciseModel){
        viewModelScope.launch {
            var result = repository.postExercise(newExercise)

            result.onSuccess {
                this@WorkoutViewModel.newExercise.postValue(it)
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun updateExercise(updstedExercise: ExerciseModel){
        viewModelScope.launch {
            var result = repository.updateExercise(updstedExercise)

            result.onSuccess {
                this@WorkoutViewModel.updatedExercise.postValue(it)
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun delExercise(id: Int){
        viewModelScope.launch {
            val result = repository.delExercise(id)

            result.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun getExerciseInfo(){
        viewModelScope.launch {
            var result = repository.getExercises()

            result.onSuccess {
                exerciseInfoList.postValue(it)
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun getExerciseInfoByName(name: String){
        viewModelScope.launch {
            var result = repository.getExercisesByName(name)

            result.onSuccess {
                exerciseInfoList.postValue(it)
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun getExerciseInfoByCategory(category: String){
        viewModelScope.launch {
            var result = repository.getExercisesByCategory(category)

            result.onSuccess {
                exerciseInfoList.postValue(it)
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun updateUser(token: String, updatedUser: UserModel){
        viewModelScope.launch {
            var result = repository.updateUser(token, updatedUser)

            result.onSuccess {
                user.postValue(it)
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun getAllActivity(userId: Int){
        viewModelScope.launch {
            var result = repository.getEntriesByUser(userId)

            result.onSuccess {
                allActivity.postValue(it)
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun getDailyActivity(userId: Int, date: LocalDate){
        viewModelScope.launch {
            var result = repository.getEntriesByDate(userId, date)

            result.onSuccess {
                dailyActivity.postValue(it)
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }

    fun postDiaryEntry(entry: DiaryEntryModel){
        viewModelScope.launch {
            var result = repository.postEntry(entry)

            result.onSuccess {
                responseMessage.postValue(it)
            }.onFailure {
                errorMessage.postValue(it.message)
            }
        }
    }
}