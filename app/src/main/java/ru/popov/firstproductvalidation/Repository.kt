package ru.popov.firstproductvalidation

import android.content.Context
import android.os.Environment
import com.squareup.moshi.Moshi
import jcifs.smb1.smb1.NtlmPasswordAuthentication
import jcifs.smb1.smb1.SmbFile
import jcifs.smb1.smb1.SmbFileInputStream
import jcifs.smb1.smb1.SmbFileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.*

class Repository(
    private val context: Context
) {
    // Собираем строку json для передачи на сервер
    suspend fun convertResultToJson(
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
    ): String {
        return withContext(Dispatchers.Default) {
            val resultJson = FirstProductCustomAdapter.CustomFirstProduct(
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

            val moshi = Moshi.Builder()
                .add(FirstProductCustomAdapter())
                .build()

            val adapter =
                moshi.adapter(FirstProductCustomAdapter.CustomFirstProduct::class.java)
                    .nonNull()

            adapter.toJson(resultJson)
        }
    }

    // Сохраняем строку json в файл на сервере
    suspend fun saveFileToServer(strJson: String, date: String): Boolean {
        return withContext(Dispatchers.Default) {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) false

            // Создаем архитектуру папок на сервере и получаем путь к файлу настроек
            val path = Utils.createFolderOnServer(date)

            Timber.d("fileFolder = $path")

            // Отслеживаем время загрузки данных
            val start = System.currentTimeMillis()

            // Создаем объект аутентификатор
            val auth =
                NtlmPasswordAuthentication("", LoginInformation.USER, LoginInformation.PASS)

            // Создаем файл
            val fileSettings = SmbFile(path + File.separator + LoginInformation.NAME_FILE, auth)

            // Создаем объект для потока куда мы будем писать наш файл
            val destFileName = SmbFileOutputStream(fileSettings)

            destFileName.buffered().use { fileOutputStream ->
                fileOutputStream.write(strJson.toByteArray())
            }
            Timber.d("time = ${System.currentTimeMillis() - start}")
            true
        }
    }

    suspend fun download(
        year: String,
        month: String,
        day: String
    ): FirstProductCustomAdapter.CustomFirstProduct? {
        return withContext(Dispatchers.IO) {
            var strJson = ""
//            Timber.d("$day isExistFile = ${isExistFile(year, month, day)}")
            if (isExistFile(year, month, day)) {
                strJson = downloadFileToServer(year, month, day)
            }

//            Timber.d("$day strJson = ${strJson}")
            if (strJson.isNotEmpty()) {

//                Timber.e("$day Adapter = ${stringJsonToCustomAdapter(strJson)}")
                stringJsonToCustomAdapter(strJson)
            } else
                null
        }
    }

    private fun stringJsonToCustomAdapter(strJson: String): FirstProductCustomAdapter.CustomFirstProduct? {
        val moshi = Moshi.Builder()
            .add(FirstProductCustomAdapter())
            .build()

        val adapter =
            moshi.adapter(FirstProductCustomAdapter.CustomFirstProduct::class.java)
                .nonNull()

        return adapter.fromJson(strJson)
    }

    // Загружаем с сервера строку json по выбранной дате
    suspend fun downloadFileToServer(year: String, month: String, day: String): String {
//        return withContext(Dispatchers.Default) {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) ""

        // Создаем объект для аутентификации на шаре
        val auth = NtlmPasswordAuthentication(
            "", LoginInformation.USER, LoginInformation.PASS
        )
        val path = LoginInformation.PATH

        // Ресолвим путь назначения в SmbFile
        val baseDir = SmbFile(
            "$path/$year/$month/$day/${LoginInformation.NAME_FILE}",
            auth
        )

        val destFileName = SmbFileInputStream(baseDir)

        var stringJsonSettings = ""
        destFileName.bufferedReader().use {
            stringJsonSettings = it.readText()
//            Timber.d("str = $stringJsonSettings")
        }

        return stringJsonSettings
//        }
    }

//    private fun stringJsonToCustomAdapter(strJson: String): CheckingSettingsCustomAdapter.CustomCheckingSettings? {
//        val moshi = Moshi.Builder()
//            .add(CheckingSettingsCustomAdapter())
////            .add(CheckingSettingsCustomAdapter.ColorAdapter2())
////            .add(CheckingSettingsCustomAdapter.ColorAdapter())
//            .build()
//
//        val adapter =
//            moshi.adapter(CheckingSettingsCustomAdapter.CustomCheckingSettings::class.java)
//                .nonNull()
//
////        return try {
//        return adapter.fromJson(strJson)
//        //            Timber.d("parse t = $t")
////        } catch (e: Exception) {
////            Timber.d("parse error = ${e.message}")
////            null
////        }
//    }

    // Проверяет существует ли файл настроек
    suspend fun isExistFile(year: String, month: String, day: String): Boolean {
        return withContext(Dispatchers.Default) {
            // Создаем объект аутентификатор
            val auth =
                NtlmPasswordAuthentication("", LoginInformation.USER, LoginInformation.PASS)

            // Ресолвим путь назначения в SmbFile
            val baseDir = SmbFile(
                "${LoginInformation.PATH}/$year/$month/$day/${LoginInformation.NAME_FILE}",
                auth
            )
            baseDir.exists()
        }
    }
}