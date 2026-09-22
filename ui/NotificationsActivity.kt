package com.vibelocal.app.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vibelocal.app.databinding.ActivityNotificationsBinding
import kotlinx.coroutines.launch

class NotificationsActivity:BaseActivity(){private lateinit var b:ActivityNotificationsBinding;private val a=NotificationAdapter(emptyList());override fun onCreate(s:Bundle?){super.onCreate(s);b=ActivityNotificationsBinding.inflate(layoutInflater);setContentView(b.root);b.recyclerNotifications.layoutManager=LinearLayoutManager(this);b.recyclerNotifications.adapter=a;lifecycleScope.launch{try{a.submit(api.notifications(session.userId()))}catch(_:Exception){}}}}
