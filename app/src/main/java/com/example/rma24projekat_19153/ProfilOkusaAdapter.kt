package com.example.rma24projekat_19153

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ProfilOkusaAdapter(context: Context, resource: Int, objects: Array<ProfilOkusaBiljke>):
    ArrayAdapter<ProfilOkusaBiljke>(context,resource,objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_single_choice, parent, false)
        val item = getItem(position)
        itemView.findViewById<TextView>(android.R.id.text1).text = item?.opis
        return itemView
    }
}