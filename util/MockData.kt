package com.vibelocal.app.util

import com.vibelocal.app.model.EventDto

object MockData {
    val events = listOf(
        EventDto(1, "Johannesburg Music Festival", "Live local artists and an evening of music.", "2026-09-26T18:00:00", "Constitution Hill, Johannesburg", -26.1894, 28.0365, "Music", 150.0, 95),
        EventDto(2, "Soweto Food & Culture Day", "Local food, culture and community experiences.", "2026-09-30T12:00:00", "Soweto, Johannesburg", -26.2485, 27.8540, "Food", 80.0, 88),
        EventDto(3, "Student Tech Networking Night", "Meet students and young professionals in technology.", "2026-09-23T17:00:00", "Braamfontein, Johannesburg", -26.1929, 28.0354, "Networking", 0.0, 92),
        EventDto(4, "Pretoria Botanical Garden Concert", "Outdoor concert in the beautiful gardens.", "2026-10-10T15:00:00", "Botanical Garden, Pretoria", -25.7392, 28.2736, "Music", 200.0, 90),
        EventDto(5, "Centurion Farmer's Market", "Fresh local produce and handmade crafts.", "2026-10-02T08:00:00", "Centurion, Gauteng", -25.8640, 28.1858, "Food", 0.0, 85),
        EventDto(6, "Midrand Tech Expo", "Discover the latest innovations in technology.", "2026-10-15T09:00:00", "Gallagher Convention Centre, Midrand", -26.0028, 28.1311, "Networking", 50.0, 87),
        EventDto(7, "Sandton Art Gallery Opening", "New exhibition featuring contemporary African art.", "2026-10-08T19:00:00", "Sandton, Johannesburg", -26.1076, 28.0567, "Culture", 0.0, 94),
        EventDto(8, "Vanderbijlpark River Festival", "Water sports and music on the Vaal River.", "2026-11-05T10:00:00", "Vanderbijlpark, Gauteng", -26.6736, 27.8319, "Entertainment", 120.0, 82),
        EventDto(9, "Kempton Park Night Run", "5km night run through the city streets.", "2026-10-12T19:30:00", "Kempton Park, Gauteng", -26.0950, 28.2320, "Fitness", 100.0, 89),
        EventDto(10, "Centurion Wine Tasting", "Experience premium wines from across the country.", "2026-10-20T18:00:00", "Irene, Centurion", -25.8786, 28.2239, "Food", 300.0, 91),
        EventDto(11, "Pretoria Craft Beer Festival", "Celebrate local breweries and great food.", "2026-10-25T12:00:00", "Menlyn, Pretoria", -25.7824, 28.2750, "Food", 150.0, 86)
    )
}
