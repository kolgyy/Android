package com.korotaev.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Адаптер связывает данные (список телефонов) с RecyclerView
class PhonesAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Внутренний список данных, который будет отображаться в RecyclerView
    private var mPhonesList: ArrayList<PhoneModel> = ArrayList()

    // Метод для передачи нового списка телефонов в адаптер
    fun setupPhones(phonesList: ArrayList<PhoneModel>) {
        mPhonesList.clear()              // очищаем старые данные
        mPhonesList.addAll(phonesList)  // добавляем новые данные
        notifyDataSetChanged()          // уведомляем RecyclerView, что данные изменились
    }

    // Вызывается RecyclerView для привязки данных к конкретной позиции
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Проверяем, что ViewHolder именно нашего типа
        if (holder is PhonesViewHolder) {
            // Передаём конкретный объект PhoneModel в ViewHolder
            holder.bind(mPhones = mPhonesList[position])
        }
    }

    // Возвращает количество элементов в списке
    override fun getItemCount(): Int {
        return mPhonesList.count()
    }

    // Создаёт новый ViewHolder (вызывается когда RecyclerView нужен новый элемент)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Получаем LayoutInflater для создания View из XML
        val layoutInflater = LayoutInflater.from(parent.context)

        // "Надуваем" (inflate) разметку recyclerview_item.xml
        val itemView = layoutInflater.inflate(R.layout.recyclerview_item, parent, false)

        // Возвращаем созданный ViewHolder с этим View
        return PhonesViewHolder(itemView)
    }

    // ViewHolder хранит ссылки на View одного элемента списка
    class PhonesViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {

        // Находим TextView внутри itemView по их id
        private val tvPhoneName = itemView.findViewById<TextView>(R.id.tvPhoneName)
        private val tvPrice = itemView.findViewById<TextView>(R.id.tvPrice)
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        private val tvScore = itemView.findViewById<TextView>(R.id.tvScore)

        // Метод bind связывает объект PhoneModel с View
        fun bind(mPhones: PhoneModel) {
            // Заполняем TextView данными из модели
            tvPhoneName.text = mPhones.name
            tvPrice.text = mPhones.price
            tvDate.text = mPhones.date
            tvScore.text = mPhones.score
        }
    }
}
