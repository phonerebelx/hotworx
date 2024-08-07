package com.hotworx.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class GenericAdapter<T> internal constructor(
    private val layout: Int,
    private val itemClickListener: OnItemClickListener<T>? = null
) : RecyclerView.Adapter<GenericAdapter<T>.MyViewHolder>() {

    var items: ArrayList<T> = ArrayList()
        set(value) {
            field = value
            val diffUtil = LastDiffUtilCallback(items, itemsFiltered)
            val diffUtilResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffUtil)
            itemsFiltered.clear()
            itemsFiltered.addAll(value)
            diffUtilResult.dispatchUpdatesTo(this)
        }

    var itemsFiltered: ArrayList<T> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface OnItemClickListener<T> {
        fun onItemClick(item: T) {}
        fun onItemClickTwo(item: T) {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsFiltered[position]
        holder.itemView.setOnClickListener { itemClickListener?.onItemClick(item) }
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemsFiltered.size
    }

    override fun getItemViewType(position: Int): Int {
        return layout
    }

    inner class MyViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: T) {
//            binding.setVariable(BR.item, item)
//            binding.setVariable(BR.listener, itemClickListener)
            binding.executePendingBindings()
        }
    }

    fun removeItem(item: T, position: Int) {
        itemsFiltered.remove(item)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemsFiltered.size)
    }

    fun removeItem(item: T){
        itemsFiltered.remove(item)
        notifyDataSetChanged()
    }

    fun addItem(item: T){
        itemsFiltered.add(item)
        notifyDataSetChanged()
    }

    fun addList(items: List<T>){
        itemsFiltered.clear()
        itemsFiltered.addAll(items)
        notifyDataSetChanged()
    }

    fun addNewList(items: List<T>){
        itemsFiltered.addAll(items)
        notifyDataSetChanged()
    }

    fun addSearchedList(items: List<T>){
        itemsFiltered = items as ArrayList<T>
        notifyDataSetChanged()
    }

    inner class LastDiffUtilCallback(
        private val newList: ArrayList<T>,
        private val oldList: ArrayList<T>
    ) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newList[newItemPosition] == oldList[oldItemPosition]
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newList[newItemPosition] == oldList[oldItemPosition]
        }

    }
}