package com.nawaqes.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nawaqes.Model.Cities_Response
import com.nawaqes.R
import com.nawaqes.View.Locationid_View

class SortsArea_Adapter   (context: Context, val data: List<Cities_Response.Data>)
    : RecyclerView.Adapter<SortsArea_Adapter.ViewHolder>() {
    lateinit var id: Locationid_View
    var context: Context = context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SortsArea_Adapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_sort, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: SortsArea_Adapter.ViewHolder, position: Int) {
        holder.T_Title.text = data.get(position).name
        holder.itemView.setOnClickListener(){
            this.id.Areaid(data.get(position).id.toString(),data.get(position).name)
        }

    }
    fun onClick(id: Locationid_View){
        this.id=id
    }
    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return data.size
    }


    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val context: Context = itemView.context
        var T_Title = itemView.findViewById(R.id.title) as TextView


    }
}