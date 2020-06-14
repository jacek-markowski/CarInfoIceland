package `is`.markowski.jacek.vehicle.registration.plates.of.iceland

import `is`.markowski.jacek.vehicle.registration.plates.of.iceland.util.BrandLogo
import `is`.markowski.jacek.vehicle.registration.plates.of.iceland.util.Query
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_main)
        ibt_search.isEnabled = true
        ibt_search.setOnClickListener { v ->
            val plateNumber: String = ed_search_number.text.trim { it == ' ' }.toString().replace(" ", "").toLowerCase()
            Thread.sleep(100)
            tv_brand_name.text = ""
            tv_registration_number.text = ""
            tv_alias.text = ""
            tv_vin.text = ""
            tv_first_registered.text = ""
            tv_emmision.text = ""
            tv_weight.text = ""
            tv_status.text = ""
            tv_next_inspection.text = ""
            Query.query(this,
                    plateNumber,
                    tv_brand_name,
                    tv_registration_number,
                    tv_alias,
                    tv_vin,
                    tv_first_registered,
                    tv_emmision,
                    tv_weight,
                    tv_status,
                    tv_next_inspection,
                    progressBar,
                    ibt_search)
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
        }

        tv_brand_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val brand = BrandLogo.getBrandName(p0)
                val source = "http://www.carlogos.org/logo/$brand-logo.png"
                BrandLogo.displayImage(this@MainActivity, source, img_brand)

            }
        })

        ibt_copy_vin.setOnClickListener { _ ->
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("vin", tv_vin.text.toString())
            clipboard.primaryClip = clip
            Toast.makeText(this@MainActivity, "VIN copied to clipboard!", LENGTH_LONG).show()
        }

        bt_samgongustofa.setOnClickListener {
            var plate = ""
            try {
                plate = URLEncoder.encode(ed_search_number.text.trim { it == ' ' }.toString().replace(" ", ""), "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            val url = "https://www.samgongustofa.is/umferd/okutaeki/okutaekjaskra/uppfletting?vq=$plate"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }


}
