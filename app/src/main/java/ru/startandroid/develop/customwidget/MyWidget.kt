package ru.startandroid.develop.customwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.RemoteViews
import java.util.*

class MyWidget : AppWidgetProvider() {
    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        Log.d(LOG_TAG, "onEnabled")
    }

    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds))
        val sp: SharedPreferences = context.getSharedPreferences(
            ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE)
        for (id in appWidgetIds) {
            updateWidget(context, appWidgetManager, sp, id)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds))

        val editor: SharedPreferences.Editor = context.getSharedPreferences(
            ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE).edit()
        for (widgetID in appWidgetIds) {
            editor.remove(ConfigActivity.WIDGET_TEXT + widgetID)
            editor.remove(ConfigActivity.WIDGET_COLOR + widgetID)
        }
        editor.commit()
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        Log.d(LOG_TAG, "onDisabled")
    }

    companion object {
        const val LOG_TAG = "myLogs"
        fun updateWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            sp: SharedPreferences, widgetID: Int,
        ) {
            Log.d(LOG_TAG, "updateWidget $widgetID")

            val widgetText = sp.getString(ConfigActivity.WIDGET_TEXT + widgetID, null) ?: return
            val widgetColor = sp.getInt(ConfigActivity.WIDGET_COLOR + widgetID, 0)

            val widgetView = RemoteViews(context.getPackageName(),
                R.layout.widget)
            widgetView.setTextViewText(R.id.tv, widgetText)
            widgetView.setInt(R.id.tv, "setBackgroundColor", widgetColor)


            appWidgetManager.updateAppWidget(widgetID, widgetView)
        }
    }
}