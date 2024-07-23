package com.hotworx.ui.adapters.GetStarted

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hotworx.R
import com.hotworx.activities.DockActivity
import com.hotworx.helpers.Utils
import com.hotworx.models.GettingStarted.GettingStartedModel.Video
import com.hotworx.ui.fragments.VPT.VideoPlayerActivty

class GetStartedAdapter(val context: Context,val activty: DockActivity): RecyclerView.Adapter<GetStartedAdapter.ViewHolder>(){
    lateinit var getVideoDetail: ArrayList<Video>
    private lateinit var tvVideoTitle: TextView
    private lateinit var tvVideoText: TextView
    private lateinit var ivVideoImg: ImageView
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        @SuppressLint("Range")
        fun bindItems(item: Video) {
            this.setIsRecyclable(false)
            Glide.with(itemView.context)
                .load(item.image_url)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                .into(ivVideoImg)
            tvVideoTitle.text = item.video_name
            tvVideoText.text = item.video_text ?: ""
        }
    }
    fun setList(list: ArrayList<Video>) {
        getVideoDetail = list
        notifyDataSetChanged()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.fragment_get_started_adapter, parent, false)
        ivVideoImg = view.findViewById(R.id.ivVideoImg)
        tvVideoTitle = view.findViewById(R.id.tvVideoTitle)
        tvVideoText = view.findViewById(R.id.tvVideoText)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getVideoDetail[position]
        holder.bindItems(item)
        holder.itemView.findViewById<ConstraintLayout>(R.id.clVideoLayout).setOnClickListener{
            showVideoPlayer(item.video_link)
//            if (item.is_allow == "yes") {
//                onItemClickInterface.onItemClick(item.location_name)
//            }
        }
    }

    override fun getItemCount(): Int {
        return when {
            ::getVideoDetail.isInitialized -> getVideoDetail.size
            else -> 0
        }
    }

    fun showVideoPlayer(videoUrl: String?) {
        val intent = Intent(activty, VideoPlayerActivty::class.java)
        if (!videoUrl.isNullOrEmpty()) {
            intent.putExtra("url", videoUrl)
            activty.startActivity(intent)
        } else {
            Utils.customToast(activty, "No Video Available")
        }
    }


}