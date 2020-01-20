package com.nawaqes.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nawaqes.Activities.Request_Details
import com.nawaqes.Activities.Sent_Message
import com.nawaqes.Model.DetailsShortlists_Response
import com.nawaqes.Model.Requests_Shortlists_Response
import com.nawaqes.R
import com.nawaqes.View.Locationid_View
import kotlinx.android.synthetic.main.textcolorspinner.view.*

class RequestDetails_Adapter   (context: Context, val data: List<DetailsShortlists_Response.Data>)
    : RecyclerView.Adapter<RequestDetails_Adapter.ViewHolder>() {
    lateinit var id: Locationid_View
    var context: Context = context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestDetails_Adapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_detailsrequest, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: RequestDetails_Adapter.ViewHolder, position: Int) {
        if(data.get(position).premium.equals("2")){
            holder.Rela_Border.setBackgroundResource(R.drawable.bv_gold)
            holder.Img_Star.visibility=View.VISIBLE
        }
        holder.Title.text = ""+data.get(position).name
        holder.address.text= ""+data.get(position).address
        Glide.with(context)
            .load( data.get(position).image)
            .into(holder.Img_Shop)
        holder.Img_Chat.setOnClickListener() {
            val intent= Intent(context, Sent_Message::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("type","inbox")
            intent.putExtra("name",data.get(position).name)
            intent.putExtra("phone",data.get(position).phone)
            intent.putExtra("address",data.get(position).address)
            intent.putExtra("lat",data.get(position).lat)
            intent.putExtra("lng",data.get(position).lng)

            intent.putExtra("id",data.get(position).id.toString())
            context.startActivity(intent)
        }
        holder.Img_Phone.setOnClickListener(){
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", data.get(position).phone, null))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        var Title = itemView.findViewById(R.id.Title) as TextView
        var address = itemView.findViewById(R.id.address) as TextView
        var Img_Shop = itemView.findViewById(R.id.Img_Shop) as ImageView
        var Img_Star=itemView.findViewById(R.id.Img_Star)as ImageView
       var Rela_Border=itemView.findViewById(R.id.Rela_Border) as RelativeLayout
        var Img_Chat = itemView.findViewById(R.id.Img_Chat) as ImageView
        var Img_Phone = itemView.findViewById(R.id.Img_Phone) as ImageView

    }

    }
