package ru.popov.firstproductvalidation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.popov.firstproductvalidation.databinding.FragmentHomeBinding
import timber.log.Timber
import java.time.LocalDateTime

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by viewModels()

    private var selectedDateAsPath = ""

    private val currentDateTime = LocalDateTime.now()
    private var selectedYear = currentDateTime.year
    private var selectedMonth = currentDateTime.month.value
    private var selectedDay = currentDateTime.dayOfMonth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Подписываемся на обновления вьюмодели
        bindViewModel()

        // Кнопка загрузки настроек
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_load_settings -> {
                    // Получаем дату которую выбрал пользователь
                    getDate()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }
    }

    private fun bindViewModel() {
//        // Получаем строку и отправляем ее файлом на сервер
//        viewModel.stringJsonSettings.observe(viewLifecycleOwner) {
//            Timber.e("СТРОКА - $it")
//            viewModel.saveFileToServer(it, selectedDateAsPath)
//        }
//        // Получаем строку json с сервера и парсим её
//        viewModel.downloadStringJsonSettings.observe(viewLifecycleOwner) {
//            try {
//                setSettingsIsHistory(it)
//            } catch (e: Exception) {
//                Timber.d("error = ${e.message}")
//            }
//        }
//        viewModel.isError.observe(viewLifecycleOwner) {
//            when (it) {
//                "The system cannot find the path specified." -> {
//                    toast(R.string.path_not_found)
//                }
//                "The system cannot find the file specified." -> {
//                    if (!isTodaySettings())
//                        toast(R.string.file_not_found)
//                }
//                "Failed to connect to server" -> {
//                    toast(R.string.no_connection)
//                }
//                else -> {
//                    Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
////                    toast(it)
//                }
//            }
//
//            Timber.d("error = $it")
//        }
//        viewModel.isSending.observe(viewLifecycleOwner) {
//            toast(R.string.data_send)
//        }
//        viewModel.isFileSettingsExist.observe(viewLifecycleOwner) {
//            if (!it) {
//                sendSettingsToServer()
//            } else {
//                if (isTodaySettings())
//                    sendSettingsToServer()
//                else
//                    adminVerification()
//            }
//        }
    }



    // Показываем/скрываем progressBar
    private fun showProgressSend(isSending: Boolean) {
        with(binding) {
            buttonSendServer.isEnabled = isSending.not()
            scrollView.isVisible = isSending.not()
            progressBar.isVisible = isSending
        }
    }

    private fun getDate() {
//        val currentDateTime = LocalDateTime.now()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                selectedYear = year
                selectedMonth = month + 1
                selectedDay = dayOfMonth
                selectedDateAsPath = "$year/${month + 1}/$dayOfMonth"

//                loadSettings()
            },
            selectedYear,
            selectedMonth - 1,
            selectedDay
//            if (selectedYear == 0) currentDateTime.year else selectedYear,
//            if (selectedMonth == 0) currentDateTime.month.value - 1 else selectedMonth - 1,
//            if (selectedDay == 0) currentDateTime.dayOfMonth else selectedDay
        )
            .show()
    }
}