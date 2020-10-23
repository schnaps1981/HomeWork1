package com.example.homework1.fragments.fragmentC

import android.view.View
import android.view.ViewGroup
import com.example.homework1.base.BaseViewHolder
import com.example.homework1.models.PhoneContact
import kotlinx.android.synthetic.main.v_contact.view.*

class ContactItemViewHolder(itemView: View, private val parent: ViewGroup) :
    BaseViewHolder<PhoneContact>(itemView) {

    override fun bind(model: PhoneContact) {
        itemView.tvName.text = model.name
        itemView.tvPhone.text = model.phone
    }
}
