package ru.kondachok.core2.sample.presentation.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.kondachok.core2.R
import ru.kondachok.core2.sample.domian.Answer
import ru.kondachok.core2.sample.presentation.list.AnswersAdapter.VH

class AnswersAdapter : ListAdapter<Answer, VH>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        (holder.itemView as TextView).text = getItem(position).text
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object Diff : DiffUtil.ItemCallback<Answer>() {
        override fun areItemsTheSame(oldItem: Answer, newItem: Answer): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: Answer, newItem: Answer): Boolean =
            oldItem.text == newItem.text
    }
}
