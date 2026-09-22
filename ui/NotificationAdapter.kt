package com.vibelocal.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vibelocal.app.databinding.ItemNotificationBinding
import com.vibelocal.app.model.NotificationDto

class NotificationAdapter(private var items: List<NotificationDto>) : RecyclerView.Adapter<NotificationAdapter.VH>() {
    class VH(val b: ItemNotificationBinding) : RecyclerView.ViewHolder(b.root)
    override fun onCreateViewHolder(p: ViewGroup, v: Int) = VH(ItemNotificationBinding.inflate(LayoutInflater.from(p.context), p, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(h: VH, p: Int) { h.b.txtNotificationTitle.text = if (items[p].read) "Notification" else "New notification"; h.b.txtNotificationMessage.text = items[p].message }
    fun submit(list: List<NotificationDto>) { items = list; notifyDataSetChanged() }
}
