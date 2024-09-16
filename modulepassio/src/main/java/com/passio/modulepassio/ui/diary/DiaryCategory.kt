package com.passio.modulepassio.ui.diary

import com.passio.modulepassio.ui.diary.DiaryLogsAdapter
import com.passio.modulepassio.R
import com.passio.modulepassio.databinding.DiaryCategoryLayoutBinding
import com.passio.modulepassio.ui.model.FoodRecord
import com.passio.modulepassio.ui.model.MealLabel
import com.passio.modulepassio.ui.util.DesignUtils
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.yanzhenjie.recyclerview.SwipeMenuItem

class DiaryCategory @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    interface CategoryListener {
        fun onLogEdit(foodRecord: FoodRecord)
        fun onLogDelete(foodRecord: FoodRecord)
    }

    private var _binding: DiaryCategoryLayoutBinding? = null
    private val binding: DiaryCategoryLayoutBinding get() = _binding!!
    private val adapter = DiaryLogsAdapter(::onRecordSelected)
    private lateinit var mealLabel: MealLabel
    private var expanded = false
    private var listener: CategoryListener? = null

    init {
        _binding = DiaryCategoryLayoutBinding.inflate(LayoutInflater.from(context), this)
        orientation = VERTICAL
        background = ContextCompat.getDrawable(context, R.drawable.rc_8_white)
        elevation = DesignUtils.dp2pxFloat(4f)

        with(binding) {
            categoryLayout.setOnClickListener {
                expanded = !expanded
                invalidateState()
            }

            logList.adapter = null
            logList.visibility = View.GONE

            logList.setSwipeMenuCreator { _, rightMenu, position ->
                val editItem = SwipeMenuItem(context).apply {
                    text = context.getString(R.string.edit)
                    setTextColor(Color.WHITE)
                    setBackgroundColor(ContextCompat.getColor(context, R.color.passio_primary))
                    width = DesignUtils.dp2px(80f)
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                }
                rightMenu.addMenuItem(editItem)
                val deleteItem = SwipeMenuItem(context).apply {
                    text = context.getString(R.string.delete)
                    setTextColor(Color.WHITE)
                    setBackgroundColor(ContextCompat.getColor(context, R.color.passio_red500))
                    width = DesignUtils.dp2px(80f)
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                }
                rightMenu.addMenuItem(deleteItem)
            }
            logList.setOnItemMenuClickListener { menuBridge, adapterPosition ->
                menuBridge.closeMenu()
                when (menuBridge.position) {
                    0 -> listener?.onLogEdit(adapter.getFoodRecordAt(adapterPosition))
                    1 ->  listener?.onLogDelete(adapter.getFoodRecordAt(adapterPosition))
                }
            }

            logList.adapter = adapter
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }

    fun setup(mealLabel: MealLabel, listener: CategoryListener) {
        this.mealLabel = mealLabel
        this.listener = listener
        binding.category.text = mealLabel.value
    }

    fun update(records: List<FoodRecord>) {
        if (records.isNotEmpty() && !expanded) {
            expanded = true
            post { invalidateState() }
        }
        adapter.updateLogs(records)
    }

    private fun onRecordSelected(foodRecord: FoodRecord) {
        listener?.onLogEdit(foodRecord)
    }

    private fun invalidateState() {
        with(binding) {
            if (!expanded) {
                chevron.rotationX = 0f
                logList.visibility = View.GONE
                setPadding(0, 0, 0, 0)
            } else {
                chevron.rotationX = 180f
                logList.visibility = View.VISIBLE
                if (adapter.itemCount > 0) {
                    setPadding(0, 0, 0, DesignUtils.dp2px(8f))
                }
            }
        }
    }
}