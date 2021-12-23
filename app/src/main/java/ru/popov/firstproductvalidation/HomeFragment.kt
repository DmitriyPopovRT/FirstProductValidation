package ru.popov.firstproductvalidation

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.popov.firstproductvalidation.Utils.checkedImageButton
import ru.popov.firstproductvalidation.Utils.checkedTextView
import ru.popov.firstproductvalidation.Utils.toast
import ru.popov.firstproductvalidation.databinding.FragmentHomeBinding
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by viewModels()

    private val currentDateTime = LocalDateTime.now()
    private var selectedYear = currentDateTime.year
    private var selectedMonth = currentDateTime.month.value
    private var selectedDay = currentDateTime.dayOfMonth

    private var selectedDateAsPath = ""
//    private var selectedDateAsPath = "$selectedYear/$selectedMonth/$selectedDay"

    private var preparationBoard: Boolean? = null
    private var preparationBody: Boolean? = null
    private var boardInstallationCase: Boolean? = null
    private var installationAKB: Boolean? = null
    private var programming: Boolean? = null
    private var check: Boolean? = null
    private var topCoverInstallation: Boolean? = null
    private var packing: Boolean? = null
    private var titleResultFlag: Boolean? = null
    private var releaseFlag: Boolean? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Подписываемся на обновления вьюмодели
        bindViewModel()

        // Вешаем слушатели на все кнопки
        initButtons()

        // Кнопка загрузки настроек
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_load_settings -> {
                    // Получаем дату которую выбрал пользователь
                    getDate("get")
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }

        binding.date.setOnClickListener {
            getDate("set")
        }

        binding.buttonSendServer.setOnClickListener {
            Timber.d("blam")
            // Проверяем существует ли файл настроек. Если существует, отправляем данные
            viewModel.isExistFile(
                selectedYear.toString(),
                selectedMonth.toString(),
                selectedDay.toString()
            )
        }
    }

    private fun bindViewModel() {
        // Получаем строку и отправляем ее файлом на сервер
        viewModel.stringJson.observe(viewLifecycleOwner) {
            Timber.e("СТРОКА - $it")
//            Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
            viewModel.saveFileToServer(it, selectedDateAsPath)
        }
        // Получаем строку json с сервера и парсим её
        viewModel.downloadStringJsonSettings.observe(viewLifecycleOwner) {
            try {
                setSettingsIsHistory(it)
            } catch (e: Exception) {
                Timber.d("error = ${e.message}")
            }
        }
        viewModel.isError.observe(viewLifecycleOwner) {
            when (it) {
                "The system cannot find the path specified." -> {
                    toast(R.string.path_not_found)
                }
                "The system cannot find the file specified." -> {
                    if (!isTodaySettings())
                        toast(R.string.file_not_found)
                }
                "Failed to connect to server" -> {
                    toast(R.string.no_connection)
                }
                else -> {
                    Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
//                    toast(it)
                }
            }

            Timber.d("error = $it")
        }
        viewModel.isSending.observe(viewLifecycleOwner) { toast(R.string.data_send) }
        viewModel.isFileSettingsExist.observe(viewLifecycleOwner) {
            if (!it) {
                sendSettingsToServer()
            } else {
                if (isTodaySettings())
                    sendSettingsToServer()
                else
                    adminVerification()
            }
        }
    }

    private fun setSettingsIsHistory(firstProduct: FirstProductCustomAdapter.CustomFirstProduct?) {
        binding.product.setText(firstProduct?.product)
        binding.execution.setText(firstProduct?.execution)
        binding.numberBatch.setText(firstProduct?.numberBatch)
        binding.firmwareVersion.setText(firstProduct?.firmwareVersion)
        binding.redumProcess.setText(firstProduct?.redumProcess)
        binding.serNum.setText(firstProduct?.serNum)
        Utils.setCheckedImageButtonDownloadServer(
            isChecked = firstProduct?.preparationBoard == true,
            buttonOk = binding.buttonOkPreparationBoard,
            buttonClose = binding.buttonClosePreparationBoard,
            context = requireContext()
        )
        Utils.setCheckedImageButtonDownloadServer(
            isChecked = firstProduct?.preparationBody == true,
            buttonOk = binding.buttonOkPreparationBody,
            buttonClose = binding.buttonClosePreparationBody,
            context = requireContext()
        )
        Utils.setCheckedImageButtonDownloadServer(
            isChecked = firstProduct?.boardInstallationCase == true,
            buttonOk = binding.buttonOkBoardInstallationCase,
            buttonClose = binding.buttonCloseBoardInstallationCase,
            context = requireContext()
        )
        Utils.setCheckedImageButtonDownloadServer(
            isChecked = firstProduct?.installationAKB == true,
            buttonOk = binding.buttonOkInstallationAKB,
            buttonClose = binding.buttonCloseInstallationAKB,
            context = requireContext()
        )
        Utils.setCheckedImageButtonDownloadServer(
            isChecked = firstProduct?.programming == true,
            buttonOk = binding.buttonOkProgramming,
            buttonClose = binding.buttonCloseProgramming,
            context = requireContext()
        )
        Utils.setCheckedImageButtonDownloadServer(
            isChecked = firstProduct?.check == true,
            buttonOk = binding.buttonOkCheck,
            buttonClose = binding.buttonCloseCheck,
            context = requireContext()
        )
        Utils.setCheckedImageButtonDownloadServer(
            isChecked = firstProduct?.topCoverInstallation == true,
            buttonOk = binding.buttonOkTopCoverInstallation,
            buttonClose = binding.buttonCloseTopCoverInstallation,
            context = requireContext()
        )
        Utils.setCheckedImageButtonDownloadServer(
            isChecked = firstProduct?.packing == true,
            buttonOk = binding.buttonOkPacking,
            buttonClose = binding.buttonClosePacking,
            context = requireContext()
        )
        Utils.setCheckedTextViewDownloadServer(
            isChecked = firstProduct?.titleResultFlag == true,
            buttonOk = binding.titleResultFit,
            buttonClose = binding.titleResultNoFit,
            context = requireContext()
        )
        Utils.setCheckedTextViewDownloadServer(
            isChecked = firstProduct?.releaseFlag == true,
            buttonOk = binding.releaseAllow,
            buttonClose = binding.releaseNotAllow,
            context = requireContext()
        )
        binding.notePreparationBoard.setText(firstProduct?.notePreparationBoard)
        binding.notePreparationBody.setText(firstProduct?.notePreparationBody)
        binding.noteBoardInstallationCase.setText(firstProduct?.noteBoardInstallationCase)
        binding.noteInstallationAKB.setText(firstProduct?.noteInstallationAKB)
        binding.noteProgramming.setText(firstProduct?.noteProgramming)
        binding.noteCheck.setText(firstProduct?.noteCheck)
        binding.noteTopCoverInstallation.setText(firstProduct?.noteTopCoverInstallation)
        binding.notePacking.setText(firstProduct?.notePacking)
        binding.date.setText(firstProduct?.date)
        binding.sign.setText(firstProduct?.sign)
    }

    // Отправка настроек на сервер
    private fun sendSettingsToServer() {
        lifecycleScope.launch {
            showProgressSend(true)
            // Отправляем команды собрать информацию с вью и записать в строки json
            // Генерируем общую строку и отправляем на сервер
            sendSettings()

            showProgressSend(false)
        }
    }

    private fun sendSettings() {
        preparationBoard = when {
            binding.buttonOkPreparationBoard.isActivated -> true
            binding.buttonClosePreparationBoard.isActivated -> false
            else -> null
        }


        binding.run {
            viewModel.convertResultToJson(
                product.text.toString(),
                execution.text.toString(),
                numberBatch.text.toString(),
                firmwareVersion.text.toString(),
                redumProcess.text.toString(),
                serNum.text.toString(),
                preparationBoard,
                notePreparationBoard.text.toString(),
                preparationBody,
                notePreparationBody.text.toString(),
                boardInstallationCase,
                noteBoardInstallationCase.text.toString(),
                installationAKB,
                noteInstallationAKB.text.toString(),
                programming,
                noteProgramming.text.toString(),
                check,
                noteCheck.text.toString(),
                topCoverInstallation,
                noteTopCoverInstallation.text.toString(),
                packing,
                notePacking.text.toString(),
                titleResultFlag,
                releaseFlag,
                date.text.toString(),
                sign.text.toString()
            )
        }

    }

    // Диалог, возникающий если пользователь пытается перезаписать файл настроек, который уже есть
    // на сервере. Если пользователь вводит пароль администратора, то данные перезаписываются
    private fun adminVerification() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.access_confirmation))

            val etInput = EditText(requireContext()).apply {
                setSingleLine()
                inputType = EditorInfo.TYPE_CLASS_NUMBER
            }

            setView(LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                addView(TextView(requireContext()).apply {
                    text = getString(R.string.admin_access_query)
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                })
                addView(etInput)
                setPadding(32, 32, 32, 32)
            })
            setCancelable(false)

            setNegativeButton(
                getString(R.string.cancel)
            ) { dialog, _ -> dialog.cancel() }

            setPositiveButton(
                getString(R.string.ok)
            ) { _, _ ->
                val et = etInput.text.toString()
                if (et != LoginInformation.ADMIN_PASSWORD) {
                    etInput.setText("")
                    toast(R.string.password_incorrect)
                    adminVerification()
                } else {
                    sendSettingsToServer()
                    toast(R.string.access_confirmed)
                }
            }
        }.create().show()
    }

    // Показываем/скрываем progressBar
    private fun showProgressSend(isSending: Boolean) {
        with(binding) {
            buttonSendServer.isEnabled = isSending.not()
            scrollView.isVisible = isSending.not()
            progressBar.isVisible = isSending
        }
    }

    private fun getDate(str: String) {
//        val currentDateTime = LocalDateTime.now()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val localDate = LocalDate.of(year, month + 1, dayOfMonth)
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                if (str == "set") {
                    binding.date.setText(localDate.format(formatter))
                } else {
                    selectedYear = year
                    selectedMonth = month + 1
                    selectedDay = dayOfMonth
                    selectedDateAsPath = "$year/${month + 1}/$dayOfMonth"

                    loadSettings()
                }
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

    private fun loadSettings() {
        // Очищаем все view
        clearView()

        if (isTodaySettings()) {
            scene1()
        } else {
            scene2()
        }

        Timber.d("loadSettings")
        // Получаем настройки с сервера
        viewModel.downloadFileToServer(
            selectedYear.toString(),
            selectedMonth.toString(),
            selectedDay.toString()
        )
    }

    private fun scene1() {
        binding.titleCheckingSettings.isVisible = false
        binding.buttonClear.isVisible = false
        binding.constraint.setBackgroundColor(Color.WHITE)
    }

    private fun scene2() {
        binding.titleCheckingSettings.text =
            "Лист проверки от $selectedDay.$selectedMonth.$selectedYear"

        binding.titleCheckingSettings.isVisible = true
        binding.buttonClear.isVisible = true
        binding.constraint.setBackgroundColor(Color.LTGRAY)
    }

    private fun isTodaySettings(): Boolean {
        val currentDateTime = LocalDateTime.now()
        return selectedYear == currentDateTime.year &&
                selectedMonth == currentDateTime.month.value &&
                selectedDay == currentDateTime.dayOfMonth
    }

    fun clearView() {
        with(binding) {
            clearViewScreen(
                buttonOkPreparationBoard,
                buttonClosePreparationBoard,
                notePreparationBoard
            )
            preparationBoard = null
            clearViewScreen(
                buttonOkPreparationBody,
                buttonClosePreparationBody,
                notePreparationBody
            )
            preparationBody = null
            clearViewScreen(
                buttonOkBoardInstallationCase,
                buttonCloseBoardInstallationCase,
                noteBoardInstallationCase
            )
            boardInstallationCase = null
            clearViewScreen(
                buttonOkInstallationAKB,
                buttonCloseInstallationAKB,
                noteInstallationAKB
            )
            installationAKB = null
            clearViewScreen(
                buttonOkProgramming,
                buttonCloseProgramming,
                noteProgramming
            )
            programming = null
            clearViewScreen(
                buttonOkCheck,
                buttonCloseCheck,
                noteCheck
            )
            check = null
            clearViewScreen(
                buttonOkTopCoverInstallation,
                buttonCloseTopCoverInstallation,
                noteTopCoverInstallation
            )
            topCoverInstallation = null
            clearViewScreen(
                buttonOkPacking,
                buttonClosePacking,
                notePacking
            )
            packing = null
            clearViewScreen(
                titleResultFit,
                titleResultNoFit,
                null
            )
            titleResultFlag = null
            clearViewScreen(
                releaseAllow,
                releaseNotAllow,
                null
            )
            releaseFlag = null
        }
    }

    private fun clearViewScreen(buttonOk: View, buttonClose: View, editText: EditText?) {
        buttonOk.apply {
            backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.green_100)
            isActivated = false
        }
        buttonClose.apply {
            backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.red_100)
            isActivated = false
        }
        editText?.text?.clear()
    }

    private fun initButtons() {
        binding.buttonOkPreparationBoard.setOnClickListener {
            preparationBoard = checkedImageButton(
                it,
                binding.buttonOkPreparationBoard,
                binding.buttonClosePreparationBoard,
                requireContext()
            )
        }

        binding.buttonClosePreparationBoard.setOnClickListener {
            preparationBoard = checkedImageButton(
                it,
                binding.buttonOkPreparationBoard,
                binding.buttonClosePreparationBoard,
                requireContext()
            )
        }

        binding.buttonOkPreparationBody.setOnClickListener {
            preparationBody = checkedImageButton(
                it,
                binding.buttonOkPreparationBody,
                binding.buttonClosePreparationBody,
                requireContext()
            )
        }

        binding.buttonClosePreparationBody.setOnClickListener {
            preparationBody = checkedImageButton(
                it,
                binding.buttonOkPreparationBody,
                binding.buttonClosePreparationBody,
                requireContext()
            )
        }

        binding.buttonOkBoardInstallationCase.setOnClickListener {
            boardInstallationCase = checkedImageButton(
                it,
                binding.buttonOkBoardInstallationCase,
                binding.buttonCloseBoardInstallationCase,
                requireContext()
            )
        }

        binding.buttonCloseBoardInstallationCase.setOnClickListener {
            boardInstallationCase = checkedImageButton(
                it,
                binding.buttonOkBoardInstallationCase,
                binding.buttonCloseBoardInstallationCase,
                requireContext()
            )
        }

        binding.buttonOkInstallationAKB.setOnClickListener {
            installationAKB = checkedImageButton(
                it,
                binding.buttonOkInstallationAKB,
                binding.buttonCloseInstallationAKB,
                requireContext()
            )
        }

        binding.buttonCloseInstallationAKB.setOnClickListener {
            installationAKB = checkedImageButton(
                it,
                binding.buttonOkInstallationAKB,
                binding.buttonCloseInstallationAKB,
                requireContext()
            )
        }

        binding.buttonOkProgramming.setOnClickListener {
            programming = checkedImageButton(
                it,
                binding.buttonOkProgramming,
                binding.buttonCloseProgramming,
                requireContext()
            )
        }

        binding.buttonCloseProgramming.setOnClickListener {
            programming = checkedImageButton(
                it,
                binding.buttonOkProgramming,
                binding.buttonCloseProgramming,
                requireContext()
            )
        }

        binding.buttonOkCheck.setOnClickListener {
            check = checkedImageButton(
                it,
                binding.buttonOkCheck,
                binding.buttonCloseCheck,
                requireContext()
            )
        }

        binding.buttonCloseCheck.setOnClickListener {
            check = checkedImageButton(
                it,
                binding.buttonOkCheck,
                binding.buttonCloseCheck,
                requireContext()
            )
        }

        binding.buttonOkTopCoverInstallation.setOnClickListener {
            topCoverInstallation = checkedImageButton(
                it,
                binding.buttonOkTopCoverInstallation,
                binding.buttonCloseTopCoverInstallation,
                requireContext()
            )
        }

        binding.buttonCloseTopCoverInstallation.setOnClickListener {
            topCoverInstallation = checkedImageButton(
                it,
                binding.buttonOkTopCoverInstallation,
                binding.buttonCloseTopCoverInstallation,
                requireContext()
            )
        }

        binding.buttonOkPacking.setOnClickListener {
            packing = checkedImageButton(
                it,
                binding.buttonOkPacking,
                binding.buttonClosePacking,
                requireContext()
            )
        }

        binding.buttonClosePacking.setOnClickListener {
            packing = checkedImageButton(
                it,
                binding.buttonOkPacking,
                binding.buttonClosePacking,
                requireContext()
            )
        }

        binding.titleResultFit.setOnClickListener {
            titleResultFlag = checkedTextView(
                it,
                binding.titleResultFit,
                binding.titleResultNoFit,
                requireContext()
            )
        }

        binding.titleResultNoFit.setOnClickListener {
            titleResultFlag = checkedTextView(
                it,
                binding.titleResultFit,
                binding.titleResultNoFit,
                requireContext()
            )
        }

        binding.releaseAllow.setOnClickListener {
            releaseFlag = checkedTextView(
                it,
                binding.releaseAllow,
                binding.releaseNotAllow,
                requireContext()
            )
        }

        binding.releaseNotAllow.setOnClickListener {
            releaseFlag = checkedTextView(
                it,
                binding.releaseAllow,
                binding.releaseNotAllow,
                requireContext()
            )
        }
    }
}