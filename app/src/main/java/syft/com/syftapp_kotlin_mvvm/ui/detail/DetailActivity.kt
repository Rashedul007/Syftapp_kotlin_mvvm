package syft.com.syftapp_kotlin_mvvm.ui.detail

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import syft.com.syftapp_kotlin_mvvm.R
import syft.com.syftapp_kotlin_mvvm.models.GitResult
import syft.com.syftapp_kotlin_mvvm.models.ItemList

class DetailActivity : AppCompatActivity() {

    var wbVw: WebView? = null
    var intent_GitObj: ItemList ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val extras = intent.extras
        if (extras != null) {
            intent_GitObj = extras.getParcelable("intent_Main_obj")
        }

        try {
            wbVw = findViewById<View>(R.id.webview) as WebView
            wbVw?.webViewClient = WebViewClient()

            //  txtVw.setText(text);
          wbVw!!.loadUrl(intent_GitObj?.html_url)

            val webSettings = wbVw!!.settings
            webSettings.javaScriptEnabled = true
            webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        }
        catch (ex:Exception ) {
            setContentView(R.layout.activity_error);
        }

    }
}
