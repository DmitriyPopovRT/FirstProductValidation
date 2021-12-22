package ru.popov.firstproductvalidation

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import jcifs.smb1.smb1.NtlmPasswordAuthentication
import jcifs.smb1.smb1.SmbFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

object Utils {

    fun <T : Fragment> T.toast(@StringRes message: Int) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun <T : Fragment> T.toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun <T : ViewBinding> ViewGroup.inflate(
        inflateBinding: (
            inflater: LayoutInflater,
            root: ViewGroup?,
            attachToRoot: Boolean
        ) -> T, attachToRoot: Boolean = false
    ): T {
        val inflater = LayoutInflater.from(context)
        return inflateBinding(inflater, this, attachToRoot)
    }

    // Обрабатываем нажатие ImageButton
    fun checkedImageButton(
        view: View,
        buttonOk: AppCompatImageButton,
        buttonClose: AppCompatImageButton,
        context: Context
    ): Boolean? {
        when (view) {
            buttonOk -> {
                if (view.isActivated) {
                    view.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.green_100)
                    view.isActivated = false
                } else {
                    view.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.green_500)
                    view.isActivated = true
                    buttonClose.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.red_100)
                    buttonClose.isActivated = false
                }
            }

            buttonClose -> {
                if (view.isActivated) {
                    view.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.red_100)
                    view.isActivated = false
                } else {
                    view.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.red_500)
                    view.isActivated = true
                    buttonOk.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.green_100)
                    buttonOk.isActivated = false
                }
            }
        }

        return when {
            buttonOk.isActivated -> {
                true
            }
            buttonClose.isActivated -> {
                false
            }
            else -> {
                null
            }
        }
    }

    // Установка значений вью, полученных с сервера
    fun setCheckedImageButtonDownloadServer(
        isChecked: Boolean,
        buttonOk: AppCompatImageButton,
        buttonClose: AppCompatImageButton,
        context: Context
    ) {
        if (isChecked) {
            buttonOk.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.green_500)
            buttonOk.isActivated = true
            buttonClose.isActivated = false
        } else if (!isChecked) {
            buttonClose.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.red_500)
            buttonClose.isActivated = true
            buttonOk.isActivated = false
        }
    }

    // Создаем архитектуру папок на сервере
    suspend fun createFolderOnServer(date: String): String {
        return withContext(Dispatchers.Default) {

            val calendar: Calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH) + 1
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

            val pathDay = if (date.isEmpty()) {
                "${LoginInformation.PATH}/$currentYear/$currentMonth/$currentDay"
            } else {
                "${LoginInformation.PATH}/$date"
            }

            val auth = NtlmPasswordAuthentication("", LoginInformation.USER, LoginInformation.PASS)
            val smbFolder = SmbFile(pathDay, auth)

            if (!smbFolder.exists()) {
                smbFolder.mkdirs()
            }
            pathDay
        }
    }

    fun haveQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }
}