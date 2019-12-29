package com.nawaqes.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nawaqes.Model.Offers_Response
import com.nawaqes.Model.Requests_Shortlists_Response
import com.nawaqes.R
import com.nawaqes.View.Locationid_View

class Offers_Adapter (context: Context, val data: List<Offers_Response.Data>)
    : RecyclerView.Adapter<Offers_Adapter.ViewHolder>() {
    lateinit var id: Locationid_View
    var context: Context = context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Offers_Adapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_offer, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: Offers_Adapter.ViewHolder, position: Int) {
        holder.T_Title.text=data.get(position).title
        holder.T_Descrption.text=data.get(position).description

        Glide.with(context)
            .load( data.get(position).image)
            .into(holder.img_offer)

        Glide.with(context)
            .load( data.get(position).shopImage)
            .into(holder.img_logo)

//        holder.itemView.setOnClickListener() {
//            this.id.CityId(data.get(position).id.toString(), data.get(position).name)
//        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return data.size
    }

    fun onClick(id: Locationid_View) {
        this.id = id
    }


    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val context: Context = itemView.context
        var T_Title = itemView.findViewById(R.id.T_Title) as TextView
        var T_Descrption = itemView.findViewById(R.id.T_Descrption) as TextView
        var img_offer=itemView.findViewById(R.id.img_offer) as ImageView
        var img_logo=itemView.findViewById(R.id.img_logo) as ImageView

    }
}