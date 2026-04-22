package com.flosunn.pomodoro.ui.widgets

//
//object CalendarWidget : GlanceAppWidget() {
//    @SuppressLint("RestrictedApi")
//    override suspend fun provideGlance(
//        context: Context,
//        id: GlanceId
//    ) {
//        provideContent {
//            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
//            val currentTime = timeFormat.format(Date())
//
//            Column(
//                modifier = GlanceModifier
//                    .fillMaxSize()
//                    .cornerRadius(12.dp)
//                    .background(Color(0xFF404040).copy(alpha = 0.6f)),
//                verticalAlignment = Alignment.Vertical.CenterVertically,
//                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
//            ) {
//                Text(
//                    text = currentTime,
//                    style = TextStyle(
//                        fontFamily = FontFamily.Cursive,
//                        fontWeight = FontWeight.Normal,
//                        fontSize = 48.sp,
//                        color = ColorProvider(Color.White)
//                    )
//                )
//            }
//        }
//    }
//}
//
//
//class CalendarWidgetReceiver : GlanceAppWidgetReceiver() {
//    override val glanceAppWidget: GlanceAppWidget
//        get() = CalendarWidget
//}
