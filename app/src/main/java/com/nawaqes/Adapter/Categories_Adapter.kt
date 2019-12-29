package com.nawaqes.Adapter

import android.content.Context
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
import com.nawaqes.Model.Categories_Response
import com.nawaqes.R
import com.nawaqes.View.Locationid_View
import kotlinx.android.synthetic.main.row_category.view.*

class Categories_Adapter (context: Context, val userList: List<Categories_Response.Data>)
    : RecyclerView.Adapter<Categories_Adapter.ViewHolder>() {
    lateinit var productbyid: Locationid_View
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Categories_Adapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_category, parent, false)
        return ViewHolder(v)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun onBindViewHolder(holder: Categories_Adapter.ViewHolder, position: Int) {
        holder.bindItems(userList.get(position))
        holder.itemView.setOnClickListener(){
           this.productbyid.Cat_id(userList.get(position).id,userList.get(position).countSubcategoires,userList.get(position).name)
        }

        if (position == 0) {
            val linearParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            )
            linearParams.setMargins(0, 70, 0, 0)
            holder.ReLative.setLayoutParams(linearParams)
            holder.ReLative.requestLayout()

        }


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }
    fun onClick(productI:Locationid_View){
        this.productbyid=productI
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val context: Context = itemView.context
        val ReLative=itemView.findViewById(R.id.ReLative) as RelativeLayout

        fun bindItems(dataModel: Categories_Response.Data) {
            itemView.Name.text=dataModel.name
            val img = itemView.findViewById(R.id.img) as ImageView
            Glide.with(context)
                .load(dataModel.image)
                .into(img)

        }
    }
}