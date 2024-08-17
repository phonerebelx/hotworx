package com.hotworx.micsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.hotworx.R

class SpinnerAdapter(
    private val context: Context,
    private var listItemsTxt: List<String>,
    private val hint: String? = null
) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return if (hint != null) listItemsTxt.size + 1 else listItemsTxt.size
    }

    override fun getItem(position: Int): Any? {
        return if (hint != null && position == 0) hint else listItemsTxt[position - if (hint != null) 1 else 0]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.custom_spinner_item, parent, false)
            vh = ItemRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }

        // Show the hint as the selected item when the spinner is closed
        vh.label.text = if (hint != null && position == 0) {
            vh.label.setTextColor(ContextCompat.getColor(context, R.color.colorNewBurned))
            hint
        } else {
            vh.label.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
            listItemsTxt[position - if (hint != null) 1 else 0]
        }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (hint != null && position == 0) {
            val hiddenView = View(context)
            hiddenView.layoutParams = ViewGroup.LayoutParams(0, 0)
            return hiddenView
        }

        val view: View
        val vh: ItemRowHolder

        if (convertView == null || convertView.tag == null) {
            view = inflater.inflate(R.layout.custom_spinner_item, parent, false)
            vh = ItemRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }

        val actualPosition = if (hint != null) position - 1 else position
        vh.label.text = listItemsTxt[actualPosition]
        vh.label.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))

        return view
    }

    private class ItemRowHolder(row: View) {
        val label: TextView = row.findViewById(R.id.txtDropDownLabel)
    }
}
