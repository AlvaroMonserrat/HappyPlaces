package com.rrat.happyplaces.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rrat.happyplaces.activities.AddHappyPlaceActivity
import com.rrat.happyplaces.activities.MainActivity
import com.rrat.happyplaces.database.DatabaseHandler
import com.rrat.happyplaces.databinding.ItemPlaceHappyBinding
import com.rrat.happyplaces.models.HappyPlaceModel

open class HappyPlacesAdapter(
    private val context: Context,
    private val list: ArrayList<HappyPlaceModel>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    lateinit var binding: ItemPlaceHappyBinding

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemPlaceHappyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)    }


    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model: HappyPlaceModel = list[position]

        if(holder is MyViewHolder){
            holder.itemImageViewCircle.setImageURI(Uri.parse(model.image))
            holder.itemTextViewTitle.text = model.title
            holder.itemTextViewDescription.text = model.description

        }

        holder.itemView.setOnClickListener{
            if(onClickListener != null){
                onClickListener!!.onClick(position, model)
            }
        }
    }


    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int){
        val intent = Intent(context, AddHappyPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, list[position])
        activity.startActivityForResult(intent, requestCode)
        notifyItemChanged(position)
    }

    fun removeAt(position: Int){
        //DELETE ITEM DATABASE
        val dbHandler = DatabaseHandler(context)
        val isDelete = dbHandler.deleteHappyPlace(list[position])
        if(isDelete > 0){
            list.removeAt(position)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    private class MyViewHolder(private val binding: ItemPlaceHappyBinding) : RecyclerView.ViewHolder(binding.root){
        val layoutMain = binding.mainLayoutItem
        val itemTextViewTitle = binding.itemTextViewTitle
        val itemTextViewDescription = binding.itemTextViewDescription
        val itemImageViewCircle = binding.imageViewCircle
    }


    interface OnClickListener{
        fun onClick(position: Int, model: HappyPlaceModel)
    }

}