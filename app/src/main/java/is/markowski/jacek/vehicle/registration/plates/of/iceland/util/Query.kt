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
import android.widget.TextView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.lang.ref.WeakReference
import java.net.URL
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
              tvNextInspection: TextView) {
        if (Connection.isConnected(context)) {
            QueryTask(tvBrand, tvRegistration, tvAlias, tvVin, tvRegisteredAt, tvEmission, tvWeight, tvStatus, tvNextInspection).execute(plate)
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
            tvNextInspection: TextView) : AsyncTask<String, Int, JSONObject>() {
        internal var tvBrand: WeakReference<TextView> = WeakReference(tvBrand)
        internal var tvRegistration: WeakReference<TextView> = WeakReference(tvRegistration)
        internal var tvAlias: WeakReference<TextView> = WeakReference(tvAlias)
        internal var tvVin: WeakReference<TextView> = WeakReference(tvVin)
        internal var tvRegisteredAt: WeakReference<TextView> = WeakReference(tvRegisteredAt)
        internal var tvEmission: WeakReference<TextView> = WeakReference(tvEmission)
        internal var tvWeight: WeakReference<TextView> = WeakReference(tvWeight)
        internal var tvStatus: WeakReference<TextView> = WeakReference(tvStatus)
        internal var tvNextInspection: WeakReference<TextView> = WeakReference(tvNextInspection)


        private val s = "..."

        override fun onPostExecute(jsonCarInfo: JSONObject) {
            super.onPostExecute(jsonCarInfo)
            try {
                tvBrand.get()!!.text = jsonCarInfo.optString("type", "Not found.")
                tvRegistration.get()!!.text = jsonCarInfo.optString("number", s)
                tvAlias.get()!!.text = jsonCarInfo.optString("registryNumber", s)
                tvVin.get()!!.text = jsonCarInfo.optString("factoryNumber", s)
                tvRegisteredAt.get()!!.text = jsonCarInfo.optString("registeredAt", s)
                tvEmission.get()!!.text = jsonCarInfo.optString("pollution", s)
                tvWeight.get()!!.text = jsonCarInfo.optString("weight", s)
                tvStatus.get()!!.text = jsonCarInfo.optString("status", s)
                tvNextInspection.get()!!.text = jsonCarInfo.optString("nextCheck", s)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun doInBackground(vararg params: String): JSONObject {

            publishProgress(1)
            var plate = ""
            try {
                plate = URLEncoder.encode(params[0].trim { it <= ' ' }.replace("\\s", ""), "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }


            val fullUrl = "http://apis.is/car?number=$plate"

            var url: URL? = null
            try {
                url = URL(fullUrl)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            var jsonCarInfo: JSONObject? = null

            try {
                val cc = url!!.openConnection()
                cc.connectTimeout = 500
                val br = BufferedReader(InputStreamReader(cc.getInputStream()))
                jsonCarInfo = JSONObject(br.readLine())
                br.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                return jsonCarInfo!!.getJSONArray("results").getJSONObject(0)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return JSONObject()
        }
    }
}
