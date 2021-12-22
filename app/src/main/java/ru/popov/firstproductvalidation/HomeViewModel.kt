package ru.popov.firstproductvalidation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.popov.firstproductvalidation.Utils.haveQ
import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = Repository(application)

//    private val stringJsonSettingsLiveData = SingleLiveEvent<String>()
//    private val downloadStringJsonSettingsLiveData =
//        SingleLiveEvent<CheckingSettingsCustomAdapter.CustomCheckingSettings?>()
//    private val isSendingLiveData = SingleLiveEvent<Boolean>()
//    private val isSendingImagesLiveData = SingleLiveEvent<Boolean>()
//    private val isErrorSettingsLiveData = SingleLiveEvent<String>()
//    private val imagesMutableLiveData = SingleLiveEvent<List<Image>>()
//    private val isSendingPhotoLiveData = SingleLiveEvent<Boolean>()
//    private val isDeletePhotoLiveData = SingleLiveEvent<Boolean>()
//    private val isFileSettingsExistLiveData = SingleLiveEvent<Boolean>()
    private val permissionsGrantedMutableLiveData = MutableLiveData(true)
//    private val listStatisticsMutableLiveData = SingleLiveEvent<HashMap<Date, ValueAndCalibration>>()

//    val stringJsonSettings: LiveData<String>
//        get() = stringJsonSettingsLiveData
//    val downloadStringJsonSettings: LiveData<CheckingSettingsCustomAdapter.CustomCheckingSettings?>
//        get() = downloadStringJsonSettingsLiveData
//
//    val isError: LiveData<String>
//        get() = isErrorSettingsLiveData
//    val isSending: LiveData<Boolean>
//        get() = isSendingLiveData
//    val isSendingImages: LiveData<Boolean>
//        get() = isSendingImagesLiveData
//    val isSendingPhoto: LiveData<Boolean>
//        get() = isSendingPhotoLiveData
//    val isDeletePhoto: LiveData<Boolean>
//        get() = isDeletePhotoLiveData
//    val isFileSettingsExist: LiveData<Boolean>
//        get() = isFileSettingsExistLiveData
//    val imagesLiveData: LiveData<List<Image>>
//        get() = imagesMutableLiveData
//    val permissionsGrantedLiveData: LiveData<Boolean>
//        get() = permissionsGrantedMutableLiveData
//    val listStatistics: LiveData<HashMap<Date, ValueAndCalibration>>
//        get() = listStatisticsMutableLiveData

//    fun generateJson() {
//        viewModelScope.launch {
//            val settingsJsonString = repository.generateJsonString()
//            stringJsonSettingsLiveData.postValue(settingsJsonString)
//        }
//    }

//    fun convertPackageResultToJson(
//        wp29: Boolean?,
//        textNote: String
//    ) {
//        viewModelScope.launch {
//            repository.convertPackageResultToJson(wp29, textNote)
//        }
//    }
//
//    fun convertPackageResultToJson(
//        wp22Mod: Boolean?,
//        wp23Mod: Boolean?,
//        wp24Mod: Boolean?,
//        wp25Mod: Boolean?,
//        wp26Mod: Boolean?,
//        wp27Mod: Boolean?,
//        textNote: String,
//        wp22Version: Boolean?,
//        wp23Version: Boolean?,
//        wp24Version: Boolean?,
//        wp25Version: Boolean?,
//        wp26Version: Boolean?,
//        wp27Version: Boolean?,
//        textNoteVersion: String
//    ) {
//        viewModelScope.launch {
//            repository.convertProgramResultToJson(
//                wp22Mod,
//                wp23Mod,
//                wp24Mod,
//                wp25Mod,
//                wp26Mod,
//                wp27Mod,
//                textNote,
//                wp22Version,
//                wp23Version,
//                wp24Version,
//                wp25Version,
//                wp26Version,
//                wp27Version,
//                textNoteVersion
//            )
//        }
//    }

    fun updatePermissionState(isGranted: Boolean, date: String) {
        if (isGranted) {
            permissionsGranted(date)
        } else {
            permissionsDenied()
        }
    }

    fun permissionsGranted(date: String) {
        if (haveQ()) {
//            loadImages(date)
        }

        permissionsGrantedMutableLiveData.postValue(true)
    }

    fun permissionsDenied() {
        permissionsGrantedMutableLiveData.postValue(false)
    }

//    fun saveFileToServer(strJson: String, date: String) {
//        viewModelScope.launch {
////            isLoadingLiveData.postValue(true)
//            try {
//                val result = repository.saveFileToServer(strJson, date)
//                if (result) {
//                    isSendingLiveData.postValue(true)
//                }
//            } catch (t: Throwable) {
//                Timber.e(t)
//                isErrorSettingsLiveData.postValue(t.message)
////                isErrorLiveData.postValue("")
//            } finally {
////                isLoadingLiveData.postValue(false)
//            }
//        }
//    }
//
//    fun downloadFileToServer(year: String, month: String, day: String) {
//        viewModelScope.launch {
////            isLoadingLiveData.postValue(true)
//            try {
////                val result = repository.downloadFileToServer(year, month, day)
////                val adapter = stringJsonToCustomAdapter(result)
//                val adapter = repository.download(year, month, day)
//                downloadStringJsonSettingsLiveData.postValue(adapter)
//            } catch (t: Throwable) {
//                Timber.e(t)
//                isErrorSettingsLiveData.postValue(t.message)
//            } finally {
////                isLoadingLiveData.postValue(false)
//            }
//        }
//    }
//
//    fun isExistFile(year: String, month: String, day: String) {
//        viewModelScope.launch {
//            try {
//                val result = repository.isExistFile(year, month, day)
//                isFileSettingsExistLiveData.postValue(result)
//            } catch (e: Exception) {
//                Timber.e(e)
//                isErrorSettingsLiveData.postValue(e.message)
//            }
//        }
//    }
}