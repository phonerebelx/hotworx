package com.hotworx.ui.adapters.ShowSlots

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.databinding.FragmentLocationSelectionAdapterBinding
import com.hotworx.databinding.FragmentShowSlotAdapterBinding
import com.hotworx.interfaces.OnClickTypeListener
import com.hotworx.interfaces.OnItemClickInterface
import com.hotworx.models.SessionBookingModel.FinalSessionBookingModel.GetShowSlotDataModelItem
import com.hotworx.ui.adapters.DatePicker.DataPickerAdapter
//import kotlinx.android.synthetic.main.fragment_show_slot_adapter.view.*

class ShowSlotAdapter(val context: Context, val onClickTypeListener: OnClickTypeListener) :
    RecyclerView.Adapter<ShowSlotAdapter.ViewHolder>() {
    lateinit var binding: FragmentShowSlotAdapterBinding
    lateinit var getShowSlotDataModelItemArrayList: ArrayList<GetShowSlotDataModelItem>
    lateinit var tvSauna: TextView
    lateinit var tvFullTime: TextView
//    lateinit var tvSessionName: TextView
//    lateinit var ivImg1: ImageView
//    lateinit var ivImg2: ImageView
//    lateinit var ivImg3: ImageView
    lateinit var viewType: String

    var imagesNames: ArrayList<String> = arrayListOf<String>()
    var imagesNamesArray: ArrayList<String> = ArrayList()
    var arrayOfImagesNamesArray: ArrayList<ArrayList<String>> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: GetShowSlotDataModelItem) {
            this.setIsRecyclable(false)

            if (viewType == "by_session_type"){
//                binding.tvSessionName.visibility = View.GONE
                binding.tvSauna.text = item.suana_no
                binding.tvFullTime.text = item.time_slot
                setImageInView(item.slot1, itemView, binding.ivImg11)
                setImageInView(item.slot2, itemView, binding.ivImg22)
                setImageInView(item.slot3, itemView, binding.ivImg33)
            }else{
//                binding.tvSessionName.visibility = View.VISIBLE
                binding.tvSauna.text = item.suana_no
//                binding.tvSessionName.text = item.session_name
                binding.tvFullTime.text = item.time_slot
                setImageInView(item.slot1, itemView, binding.ivImg11)
                setImageInView(item.slot2, itemView, binding.ivImg22)
                setImageInView(item.slot3, itemView, binding.ivImg33)
            }

//            arrayOfImagesNamesArray.add(imagesNamesArray)
            arrayOfImagesNamesArray.add(ArrayList(imagesNamesArray))
            imagesNamesArray = ArrayList()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflater = LayoutInflater.from(context)
//        val view = inflater.inflate(R.layout.fragment_show_slot_adapter, parent, false)
//        tvSauna = view.findViewById(R.id.tvSauna)
//        tvSessionName = view.findViewById(R.id.tvSessionName)
//        tvFullTime = view.findViewById(R.id.tvFullTime)
//        ivImg1 = view.findViewById(R.id.ivImg1)
//        ivImg2 = view.findViewById(R.id.ivImg2)
//        ivImg3 = view.findViewById(R.id.ivImg3)
//        return ViewHolder(view)

        binding = FragmentShowSlotAdapterBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: ArrayList<GetShowSlotDataModelItem>, viewType: String) {
        getShowSlotDataModelItemArrayList = list
        this.viewType = viewType
        notifyDataSetChanged()
    }

    private fun setImageInView(item: String, itemView: View, slots: ImageView) {
        if (getImageString(item) == "availables.jfif") {
            slots.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.available
                )
            )
            imagesNamesArray.add("availables")
        } else if (getImageString(item) == "booked.jfif") {
            slots.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.taken))
            imagesNamesArray.add("takens")
        } else {
            slots.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.blocked))
            imagesNamesArray.add("blocked")
        }
    }

//    private fun setImageInView(item: String, itemView: View, slots: ImageView, position: Int) {
//        val imageName = getImageString(item)
//        when (imageName) {
//            "availables.jfif" -> {
//                slots.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        itemView.context,
//                        R.drawable.available
//                    )
//                )
//                imagesNamesArray.add("availables")
//            }
//            "booked.jfif" -> {
//                slots.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        itemView.context,
//                        R.drawable.taken
//                    )
//                )
//                imagesNamesArray.add("takens")
//            }
//            else -> {
//                slots.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        itemView.context,
//                        R.drawable.blocked
//                    )
//                )
//                imagesNamesArray.add("blocked")
//            }
//        }
//
//        // Ensure arrayOfImagesNamesArray is updated for the given position
//        if (arrayOfImagesNamesArray.size > position) {
//            arrayOfImagesNamesArray[position] = imagesNamesArray
//        } else {
//            arrayOfImagesNamesArray.add(imagesNamesArray)
//        }
//    }
    private fun getImageString(date: String): String {
        if (date.isNotEmpty() || date != ""){
            imagesNames = date.split("/") as ArrayList<String>
            return imagesNames[2]
        }
        return ""
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getShowSlotDataModelItemArrayList[position]

        holder.bindItems(item)

        binding.ivImg11.setOnClickListener {
            if (arrayOfImagesNamesArray[position][0] == "availables") onClickTypeListener.onItemClick(item,"FromShowSlot")
        }
        binding.ivImg22.setOnClickListener {
            if (arrayOfImagesNamesArray[position][1] == "availables") onClickTypeListener.onItemClick(item,"FromShowSlot")
        }
        binding.ivImg33.setOnClickListener {
            if (arrayOfImagesNamesArray[position][2] == "availables") onClickTypeListener.onItemClick(item,"FromShowSlot")
        }
    }

    override fun getItemCount(): Int {
        return when {
            ::getShowSlotDataModelItemArrayList.isInitialized -> getShowSlotDataModelItemArrayList.size
            else -> 0
        }
    }

}