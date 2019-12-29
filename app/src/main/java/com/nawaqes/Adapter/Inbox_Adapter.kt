package com.nawaqes.Adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nawaqes.Activities.History_Messages
import com.nawaqes.Activities.Request_Details
import com.nawaqes.Model.Categories_Response
import com.nawaqes.Model.Inbox_Response
import com.nawaqes.R
import com.nawaqes.View.Locationid_View
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.android.synthetic.main.row_category.view.*

class Inbox_Adapter  (context: Context, val userList: List<Inbox_Response.Data>)
    : RecyclerView.Adapter<Inbox_Adapter.ViewHolder>() {
    lateinit var productbyid: Locationid_View
    var context: Context = context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Inbox_Adapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: Inbox_Adapter.ViewHolder, position: Int) {
        holder.bindItems(userList.get(position))

        holder.itemView.setOnClickListener() {
            val intent= Intent(context, History_Messages::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("type","inbox")
            intent.putExtra("inbox",userList.get(position))
            context.startActivity(intent)
        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    fun onClick(productI: Locationid_View) {
        this.productbyid = productI
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val context: Context = itemView.context

        fun bindItems(dataModel: Inbox_Response.Data) {
            itemView.T_Title.text = dataModel.shop.name
            itemView.T_Mesage.text = dataModel.message
            itemView.T_Date.text=dataModel.createdAt

        }
    }
}