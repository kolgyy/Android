package com.korotaev.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Фрагмент отвечает за отображение экрана со списком телефонов
class OneFragment : Fragment() {

    // Создаём экземпляр адаптера, который будет управлять элементами RecyclerView
    private val myAdapter = PhonesAdapter()

    // Метод вызывается системой для создания интерфейса фрагмента
    // Здесь мы "надуваем" (inflate) XML-разметку и настраиваем RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Преобразуем XML (fragment_one.xml) в объект View
        val root = inflater.inflate(R.layout.fragment_one, container, false)

        // Находим RecyclerView внутри созданной разметки
        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)

        // Загружаем данные в адаптер (передаём список телефонов)
        loadData()

        // Устанавливаем LayoutManager
        // LinearLayoutManager означает, что элементы будут отображаться вертикальным списком
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Подключаем адаптер к RecyclerView
        recyclerView.adapter = myAdapter

        // Возвращаем готовый View системе Android
        return root
    }

    // Метод отвечает за загрузку данных в адаптер
    // Здесь мы берём массив телефонов из PhonesData
    // и передаём его в адаптер через setupPhones()
    private fun loadData() {
        myAdapter.setupPhones(PhonesData.phonesArr)
    }
}
