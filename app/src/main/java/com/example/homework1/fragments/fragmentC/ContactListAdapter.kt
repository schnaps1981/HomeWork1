package com.example.homework1.fragments.fragmentC

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.homework1.R
import com.example.homework1.base.BaseAdapter
import com.example.homework1.base.BaseViewHolder
import com.example.homework1.models.PhoneContact


class ContactListAdapter : BaseAdapter<PhoneContact>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<PhoneContact> {
        return ContactItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.v_contact, parent, false), parent
        )
    }
}
