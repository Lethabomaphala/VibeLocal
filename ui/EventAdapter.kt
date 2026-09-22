package com.vibelocal.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vibelocal.app.databinding.ItemEventBinding
import com.vibelocal.app.model.EventDto

class EventAdapter(private var items: List<EventDto>, private val click: (EventDto) -> Unit) : RecyclerView.Adapter<EventAdapter.VH>() {
    class VH(val b: ItemEventBinding) : RecyclerView.ViewHolder(b.root)
    override fun onCreateViewHolder(p: ViewGroup, v: Int) = VH(ItemEventBinding.inflate(LayoutInflater.from(p.context), p, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(h: VH, pos: Int) {
        val e = items[pos]
        h.b.txtCategory.text = e.category
        h.b.txtEventTitle.text = e.title
        h.b.txtEventDate.text = e.dateTime.replace('T', ' ')
        h.b.txtEventLocation.text = e.location
        h.b.txtEventPrice.text = if (e.price <= 0) "Free" else "R${e.price.toInt()}"
        h.b.txtMatch.text = e.vibeMatch?.let { "$it% Vibe Match" } ?: "VibeLocal"
        h.b.root.setOnClickListener { click(e) }
    }
    fun submit(list: List<EventDto>) { items = list; notifyDataSetChanged() }
}
