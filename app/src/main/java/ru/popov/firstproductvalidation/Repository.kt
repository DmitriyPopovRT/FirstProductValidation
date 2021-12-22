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
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.text.SimpleDateFormat
import java.util.*

class Repository(
    private val context: Context
) {

//    // Собираем строку json для передачи на сервер
//    suspend fun generateJsonString(): String {
//        return withContext(Dispatchers.Default) {
//            val moshi = Moshi.Builder()
//                .add(CheckingSettingsCustomAdapter())
////                .add(CheckingSettingsCustomAdapter.ColorAdapter2())
////                .add(CheckingSettingsCustomAdapter.ColorAdapter())
//                .build()
//
//            val adapter =
//                moshi.adapter(CheckingSettingsCustomAdapter.CustomCheckingSettings::class.java)
//                    .nonNull()
//
//            adapter.toJson(
//                CheckingSettingsCustomAdapter.CustomCheckingSettings(
//                    JsonSettings.packageJson,
//                    JsonSettings.programJson,
//                    JsonSettings.assemblyAndLabelJson,
//                    JsonSettings.assemblyJson,
//                    JsonSettings.speakerTestJson
//                )
//            )
//        }
//    }
//
//    // Собираем строку Упаковка
//    fun convertPackageResultToJson(
//        wp29: Boolean?,
//        textNote: String
//    ) {
//        JsonSettings.packageJson = CheckingSettingsCustomAdapter.PackageWrapper(
//            CheckingSettingsCustomAdapter.CheckingPackagedProduct(
//                wp29,
//                textNote
//            )
//        )
//    }
//
//    // Собираем строку Программирование и функциональная проверка
//    fun convertProgramResultToJson(
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
//        JsonSettings.programJson = CheckingSettingsCustomAdapter.ProgramTestWrapper(
//            CheckingSettingsCustomAdapter.ModMatchesProduction(
//                wp22Mod,
//                wp23Mod,
//                wp24Mod,
//                wp25Mod,
//                wp26Mod,
//                wp27Mod,
//                textNote
//            ),
//            CheckingSettingsCustomAdapter.FirmwareVersionIsCurrent(
//                wp22Version,
//                wp23Version,
//                wp24Version,
//                wp25Version,
//                wp26Version,
//                wp27Version,
//                textNoteVersion
//            )
//        )
//    }

//    // Сохраняем строку json в файл на сервере
//    suspend fun saveFileToServer(strJson: String, date: String): Boolean {
//        return withContext(Dispatchers.Default) {
//            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) false
//
//            // Создаем архитектуру папок на сервере и получаем путь к файлу настроек
//            val path = Utils.createFolderOnServer(date)
//
//            Timber.d("fileFolder = $path")
//
//            // Отслеживаем время загрузки данных
//            val start = System.currentTimeMillis()
//
//            // Создаем объект аутентификатор
//            val auth =
//                NtlmPasswordAuthentication("", LoginInformation.USER, LoginInformation.PASS)
//
//            // Создаем файл
//            val fileSettings = SmbFile(path + File.separator + LoginInformation.NAME_FILE, auth)
//
//            // Создаем объект для потока куда мы будем писать наш файл
//            val destFileName = SmbFileOutputStream(fileSettings)
//
//            destFileName.buffered().use { fileOutputStream ->
//                fileOutputStream.write(strJson.toByteArray())
//            }
//            Timber.d("time = ${System.currentTimeMillis() - start}")
//            true
//        }
//    }
//
//    // Загружаем с сервера строку json по выбранной дате
//    suspend fun downloadFileToServer(year: String, month: String, day: String): String {
////        return withContext(Dispatchers.Default) {
//        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) ""
//
//        // Создаем объект для аутентификации на шаре
//        val auth = NtlmPasswordAuthentication(
//            "", LoginInformation.USER, LoginInformation.PASS
//        )
//        val path = LoginInformation.PATH
//
//        // Ресолвим путь назначения в SmbFile
//        val baseDir = SmbFile(
//            "$path/$year/$month/$day/${LoginInformation.NAME_FILE}",
//            auth
//        )
//
//        val destFileName = SmbFileInputStream(baseDir)
//
//        var stringJsonSettings = ""
//        destFileName.bufferedReader().use {
//            stringJsonSettings = it.readText()
////            Timber.d("str = $stringJsonSettings")
//        }
//
//        return stringJsonSettings
////        }
//    }
//
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
//
//    // Проверяет существует ли файл настроек
//    suspend fun isExistFile(year: String, month: String, day: String): Boolean {
//        return withContext(Dispatchers.Default) {
//            // Создаем объект аутентификатор
//            val auth =
//                NtlmPasswordAuthentication("", LoginInformation.USER, LoginInformation.PASS)
//
//            // Ресолвим путь назначения в SmbFile
//            val baseDir = SmbFile(
//                "${LoginInformation.PATH}/$year/$month/$day/${LoginInformation.NAME_FILE}",
//                auth
//            )
//            baseDir.exists()
//        }
//    }
}