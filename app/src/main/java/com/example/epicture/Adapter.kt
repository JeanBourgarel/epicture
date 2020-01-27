package com.example.epicture

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_row.view.*

class Adapter(val AccountFeed : AccountFeed): RecyclerView.Adapter<CustomViewHolder>() {

    /**
     * Returns the number of items
     */
    override fun getItemCount(): Int {
        return AccountFeed.data.count()
    }

    /**
     * Inflates the layout file image_row.xml and initializes the view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.image_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    /**
     * Sets the source of the ImageView according to the position in parameter
     * @param position
     */
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val img = AccountFeed.data[position]
        holder.view.img_description.text = img.description

        val imgView = holder.view.imageView
        Picasso.get().load(img.link).into(imgView)

        holder.view.nb_views.text = img.views.toString().plus(" views")
    }

}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}