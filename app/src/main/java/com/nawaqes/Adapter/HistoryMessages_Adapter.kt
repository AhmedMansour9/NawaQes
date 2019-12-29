package com.nawaqes.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nawaqes.Activities.History_Messages
import com.nawaqes.Activities.Sent_Message
import com.nawaqes.Model.HistoryMessages_Response
import com.nawaqes.Model.Inbox_Response
import com.nawaqes.R
import com.nawaqes.View.Locationid_View
import kotlinx.android.synthetic.main.item_historymessage.view.*
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.android.synthetic.main.item_message.view.T_Date
import kotlinx.android.synthetic.main.item_message.view.T_Mesage
import kotlinx.android.synthetic.main.item_message.view.T_Title

class HistoryMessages_Adapter (context: Context, val userList: List<HistoryMessages_Response.Data>)
    : RecyclerView.Adapter<HistoryMessages_Adapter.ViewHolder>() {
    lateinit var productbyid: Locationid_View
    var context: Context = context
   companion object {
       lateinit var id:String
       lateinit var name:String

   }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryMessages_Adapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_historymessage, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: HistoryMessages_Adapter.ViewHolder, position: Int) {
        holder.bindItems(userList.get(position))

        id=userList.get(position).shop.id.toString()
        name=userList.get(position).shop.name


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

        fun bindItems(dataModel: HistoryMessages_Response.Data) {
            itemView.T_Title.text = dataModel.shop.name
            itemView.T_Mesage.text = dataModel.message
            itemView.T_Date.text = dataModel.createdAt
            Glide.with(context)
                .load(dataModel.image)
                .into(itemView.Img_Msg)
        }
    }
}