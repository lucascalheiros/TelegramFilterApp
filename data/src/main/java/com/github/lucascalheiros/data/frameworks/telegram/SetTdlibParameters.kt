package com.github.lucascalheiros.data.frameworks.telegram

import android.content.Context
import org.drinkless.tdlib.TdApi

// In order to use the api you should provide your telegram api credentials
fun setTdlibParameters(context: Context) = TdApi.SetTdlibParameters()