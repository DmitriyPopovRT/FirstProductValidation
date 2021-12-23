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

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = Repository(application)

    private val stringJsonLiveData = SingleLiveEvent<String>()
    private val downloadStringJsonSettingsLiveData =
        SingleLiveEvent<FirstProductCustomAdapter.CustomFirstProduct?>()
    private val isSendingLiveData = SingleLiveEvent<Boolean>()
    private val isErrorSettingsLiveData = SingleLiveEvent<String>()
    private val isFileSettingsExistLiveData = SingleLiveEvent<Boolean>()
    private val permissionsGrantedMutableLiveData = MutableLiveData(true)

    val stringJson: LiveData<String>
        get() = stringJsonLiveData
    val downloadStringJsonSettings: LiveData<FirstProductCustomAdapter.CustomFirstProduct?>
        get() = downloadStringJsonSettingsLiveData
    val isError: LiveData<String>
        get() = isErrorSettingsLiveData
    val isSending: LiveData<Boolean>
        get() = isSendingLiveData
    val isFileSettingsExist: LiveData<Boolean>
        get() = isFileSettingsExistLiveData
    val permissionsGrantedLiveData: LiveData<Boolean>
        get() = permissionsGrantedMutableLiveData

    fun convertResultToJson(
        product: String?,
        execution: String?,
        numberBatch: String?,
        firmwareVersion: String?,
        redumProcess: String?,
        serNum: String?,
        preparationBoard: Boolean?,
        notePreparationBoard: String?,
        preparationBody: Boolean?,
        notePreparationBody: String?,
        boardInstallationCase: Boolean?,
        noteBoardInstallationCase: String?,
        installationAKB: Boolean?,
        noteInstallationAKB: String?,
        programming: Boolean?,
        noteProgramming: String?,
        check: Boolean?,
        noteCheck: String?,
        topCoverInstallation: Boolean?,
        noteTopCoverInstallation: String?,
        packing: Boolean?,
        notePacking: String?,
        titleResultFlag: Boolean?,
        releaseFlag: Boolean?,
        date: String?,
        sign: String?
    ) {
        viewModelScope.launch {
            val jsonString = repository.convertResultToJson(
                product,
                execution,
                numberBatch,
                firmwareVersion,
                redumProcess,
                serNum,
                preparationBoard,
                notePreparationBoard,
                preparationBody,
                notePreparationBody,
                boardInstallationCase,
                noteBoardInstallationCase,
                installationAKB,
                noteInstallationAKB,
                programming,
                noteProgramming,
                check,
                noteCheck,
                topCoverInstallation,
                noteTopCoverInstallation,
                packing,
                notePacking,
                titleResultFlag,
                releaseFlag,
                date,
                sign
            )
            stringJsonLiveData.postValue(jsonString)
        }
    }

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

    fun saveFileToServer(strJson: String, date: String) {
        viewModelScope.launch {
//            isLoadingLiveData.postValue(true)
            try {
                val result = repository.saveFileToServer(strJson, date)
                if (result) {
                    isSendingLiveData.postValue(true)
                }
            } catch (t: Throwable) {
                Timber.e(t)
                isErrorSettingsLiveData.postValue(t.message)
//                isErrorLiveData.postValue("")
            } finally {
//                isLoadingLiveData.postValue(false)
            }
        }
    }

    fun downloadFileToServer(year: String, month: String, day: String) {
        viewModelScope.launch {
//            isLoadingLiveData.postValue(true)
            try {
//                val result = repository.downloadFileToServer(year, month, day)
//                val adapter = stringJsonToCustomAdapter(result)
                val adapter = repository.download(year, month, day)
                downloadStringJsonSettingsLiveData.postValue(adapter)
            } catch (t: Throwable) {
                Timber.e(t)
                isErrorSettingsLiveData.postValue(t.message)
            } finally {
//                isLoadingLiveData.postValue(false)
            }
        }
    }

    fun isExistFile(year: String, month: String, day: String) {
        viewModelScope.launch {
            try {
                val result = repository.isExistFile(year, month, day)
                isFileSettingsExistLiveData.postValue(result)
            } catch (e: Exception) {
                Timber.e(e)
                isErrorSettingsLiveData.postValue(e.message)
            }
        }
    }
}