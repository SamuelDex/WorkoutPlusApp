package com.example.workoutplus.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutplus.R
import com.example.workoutplus.data.model.DiaryEntryModel
import com.example.workoutplus.data.model.LoginResponseModel
import com.example.workoutplus.databinding.CalendarDayLayoutBinding
import com.example.workoutplus.databinding.CalendarHeaderLayoutBinding
import com.example.workoutplus.databinding.FragmentCalendarBinding
import com.example.workoutplus.ui.adapter.diary.DiaryEntryAdapter
import com.google.gson.Gson
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

class CalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding
    private val workoutViewModel: WorkoutViewModel by viewModels()
    private lateinit var loginResponseModel: LoginResponseModel
    private lateinit var allActivity: List<DiaryEntryModel>
    private lateinit var dailyActivity: List<DiaryEntryModel>
    private val adapter = DiaryEntryAdapter()
    //Calendar
    private var selectedDate: LocalDate? = null
    private val titleSameYearFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val titleFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    private val selectionFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserFromPreferences()

        workoutViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            showMessage(it)
        })

        workoutViewModel.allActivity.observe(viewLifecycleOwner, Observer {
            allActivity = it
            setupCalendar()
        })

        workoutViewModel.dailyActivity.observe(viewLifecycleOwner, Observer {
            dailyActivity = it
            adapter.setEtries(dailyActivity)
        })

        binding.entriesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.entriesRecyclerView.adapter = adapter
        binding.entriesRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))

    }

    private fun setupCalendar() {
        binding.calendar.monthScrollListener = {
            binding.month.text = if (it.yearMonth.year == allActivity[allActivity.size-1].date!!.year) {
                titleSameYearFormatter.format(it.yearMonth)
            } else {
                titleFormatter.format(it.yearMonth)
            }
            selectDate(it.yearMonth.atDay(1))
        }

        val daysOfWeek = daysOfWeek()
        val startMonth = dateToYearMonth(allActivity[0].date!!)
        val endMonth =  dateToYearMonth(allActivity[allActivity.size-1].date!!)
        configureBinders(daysOfWeek)
        binding.calendar.apply {
            setup(startMonth, endMonth, daysOfWeek.first())
            scrollToMonth(endMonth)
        }
    }


    fun dateToYearMonth(date: Date): YearMonth {
        val localDate = date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        return YearMonth.from(localDate)
    }

    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val binding = CalendarDayLayoutBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        selectDate(day.date)
                    }
                }
            }
        }

        binding.calendar.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val textView = container.binding.dayText
                val dotView = container.binding.dotView

                textView.text = data.date.dayOfMonth.toString()

                if (data.position == DayPosition.MonthDate) {
                    textView.isVisible = true
                    when (data.date) {
                        LocalDate.now() -> {
                            textView.setTextColor(Color.WHITE)
                            textView.setBackgroundResource(R.drawable.today_bg)
                            dotView.isVisible = false
                        }

                        selectedDate -> {
                            textView.setTextColor(getResources().getColor(R.color.blue, null))
                            textView.setBackgroundResource(R.drawable.selected_bg)
                            dotView.isVisible = false
                        }

                        else -> {
                            textView.setTextColor(Color.BLACK)
                            textView.background = null
                            dotView.isVisible = false
                            for (entry in allActivity) {
                                if (isSameDay(entry.date!!, data.date))
                                    dotView.isVisible = true
                            }
                        }
                    }
                } else {
                    textView.isVisible = false
                    dotView.isVisible = false
                }
            }

        }
        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = CalendarHeaderLayoutBinding.bind(view).legendLayout.root
        }
        binding.calendar.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = true
                        container.legendLayout.children.map { it as TextView }
                            .forEachIndexed { index, tv ->
                                tv.text = daysOfWeek[index].name.first().toString()
                                tv.setTextColor(Color.BLACK)
                            }
                    }
                }
            }
    }

    fun isSameDay(date: Date, localDate: LocalDate): Boolean {
        val dateAsLocalDate = date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        return dateAsLocalDate == localDate
    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.calendar.notifyDateChanged(it) }
            binding.calendar.notifyDateChanged(date)
            workoutViewModel.getDailyActivity(
                loginResponseModel.user.id!!,
                date
            )
            binding.selectedDateText.text = selectionFormatter.format(date)
        }
    }

    private fun getUserFromPreferences() {
        val prefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString("usuario", null)

        if (json != null) {
            loginResponseModel = gson.fromJson(json, LoginResponseModel::class.java)
            workoutViewModel.getAllActivity(loginResponseModel.user.id!!)
        }else
            showMessage("No se ha podido recuperar el usuario")
    }

    private fun showMessage(s: String?) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }


}

