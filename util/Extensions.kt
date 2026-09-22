package com.vibelocal.app.util

import com.vibelocal.app.data.LocalEvent
import com.vibelocal.app.model.EventDto

fun EventDto.toLocal() = LocalEvent(eventId, title, description, dateTime, location, latitude, longitude, category, price)
