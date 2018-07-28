/*
 * Copyright 2018 Jacek Markowski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense,  and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package `is`.markowski.jacek.vehicle.registration.plates.of.iceland.util

import android.content.Context
import android.os.AsyncTask
import android.test.mock.MockContext
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.UnsupportedEncodingException
import java.lang.ref.WeakReference
import java.net.URLEncoder


object Query {

    fun query(context: Context,
              plate: String,
              tvBrand: TextView,
              tvRegistration: TextView,
              tvAlias: TextView,
              tvVin: TextView,
              tvRegisteredAt: TextView,
              tvEmission: TextView,
              tvWeight: TextView,
              tvStatus: TextView,
              tvNextInspection: TextView,
              progressBar: ProgressBar,
              btSearch: ImageButton) {
        if (Connection.isConnected(context)) {
            QueryTask(tvBrand, tvRegistration, tvAlias, tvVin, tvRegisteredAt, tvEmission, tvWeight, tvStatus, tvNextInspection,
                    progressBar, btSearch).execute(plate)
        } else {
            Connection.toastNoConnection(context)
        }
    }

    private open class QueryTask internal constructor(
            tvBrand: TextView,
            tvRegistration: TextView,
            tvAlias: TextView,
            tvVin: TextView,
            tvRegisteredAt: TextView,
            tvEmission: TextView,
            tvWeight: TextView,
            tvStatus: TextView,
            tvNextInspection: TextView,
            progressBar: ProgressBar,
            btSearch: ImageButton) : AsyncTask<String, Int, Elements>() {
        internal var tvBrand: WeakReference<TextView> = WeakReference(tvBrand)
        internal var tvRegistration: WeakReference<TextView> = WeakReference(tvRegistration)
        internal var tvAlias: WeakReference<TextView> = WeakReference(tvAlias)
        internal var tvVin: WeakReference<TextView> = WeakReference(tvVin)
        internal var tvRegisteredAt: WeakReference<TextView> = WeakReference(tvRegisteredAt)
        internal var tvEmission: WeakReference<TextView> = WeakReference(tvEmission)
        internal var tvWeight: WeakReference<TextView> = WeakReference(tvWeight)
        internal var tvStatus: WeakReference<TextView> = WeakReference(tvStatus)
        internal var tvNextInspection: WeakReference<TextView> = WeakReference(tvNextInspection)
        internal var progressBar: WeakReference<ProgressBar> = WeakReference(progressBar)
        internal var btSearch: WeakReference<ImageButton> = WeakReference(btSearch)


        override fun onPostExecute(elems: Elements) {
            super.onPostExecute(elems)
            try {
                tvBrand.get()!!.text = elems[0].text()
                tvRegistration.get()!!.text = elems[2].text()
                tvAlias.get()!!.text = elems[1].text()
                tvVin.get()!!.text = elems[3].text()
                tvRegisteredAt.get()!!.text = elems[4].text()
                tvEmission.get()!!.text = elems[5].text()
                tvWeight.get()!!.text = elems[6].text()
                tvStatus.get()!!.text = elems[7].text()
                tvNextInspection.get()!!.text = elems[8].text()
            } catch (e: Exception) {
                e.printStackTrace()
                tvBrand.get()!!.text = "Not Found."
            }
            (btSearch.get() ?: View(MockContext())).visibility = VISIBLE
            (progressBar.get() ?: View(MockContext())).visibility = INVISIBLE

        }

        override fun doInBackground(vararg params: String): Elements {

            publishProgress(1)
            var plate = ""
            try {
                plate = URLEncoder.encode(params[0].trim { it <= ' ' }.replace("\\s", ""), "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

            val fullUrl = "https://www.samgongustofa.is/umferd/okutaeki/okutaekjaskra/uppfletting?vq=$plate"
            return try {
                val doc = Jsoup.connect(fullUrl).get()
                doc.select("div.boxbody span")
            } catch (e: Exception) {
                Elements()
            }

        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            (btSearch.get() ?: View(MockContext())).visibility = INVISIBLE
            (progressBar.get() ?: View(MockContext())).visibility = VISIBLE
        }
    }
}
