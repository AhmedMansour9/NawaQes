package com.nawaqes.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nawaqes.Activities.Request_Details
import com.nawaqes.Model.Cities_Response
import com.nawaqes.Model.Requests_Shortlists_Response
import com.nawaqes.R
import com.nawaqes.View.Locationid_View

class MyRequests_Adapter  (context: Context, val data: List<Requests_Shortlists_Response.Data>)
    : RecyclerView.Adapter<MyRequests_Adapter.ViewHolder>() {
    lateinit var id: Locationid_View
    var context: Context = context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyRequests_Adapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_request, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: MyRequests_Adapter.ViewHolder, position: Int) {
        holder.T_Request.setText( ""+data.get(position).categoryName)
//        val string =data.get(position).createdAt
//        val datee: String? =  string.substringBefore(" ")

        holder.T_Date.setText( ""+data.get(position).createdAt)
        holder.T_Accept.setText(""+ data.get(position).acceptedByShop)

        holder.BTn_View.setOnClickListener() {
            val intent=Intent(context,Request_Details::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("item",data.get(position))
            context.startActivity(intent)
        }

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
        var T_Request = itemView.findViewById(R.id.T_Requestss) as TextView
        var T_Date = itemView.findViewById(R.id.T_Dates) as TextView
        var T_Accept = itemView.findViewById(R.id.T_Acceptss) as TextView
        var BTn_View = itemView.findViewById(R.id.BTn_View) as Button


    }
}