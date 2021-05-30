package com.bearbrand.fourney.drawer

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DrawerAdapter : RecyclerView.Adapter<DrawerAdapter.ViewHolder>() {
   abstract class ViewHolder : RecyclerView.ViewHolder(), View.OnClickListener{

       val drawerAdapter = DrawerAdapter()

       override fun onClick(v: View?) {
           TODO("Not yet implemented")
       }
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}