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
import android.text.Editable
import android.widget.ImageView

object BrandLogo {

    fun displayImage(context: Context, source: String, destination: ImageView) {
        GlideApp.with(context)
                .load(source)
                .fitCenter()
                .into(destination)
    }

    fun getBrandName(p0: Editable?): String {
        val split = p0?.split(" - ") ?: List(1, { "" })
        val brandWords = split[0].replace("-", " ")
        val splitWords = brandWords.split(" ")
        var brand = ""
        splitWords.forEach({ it -> brand += "-${it.toLowerCase().capitalize()}" })
        brand = brand.substring(1)
        if (brand.length <= 3) { // BMW
            brand = brand.toUpperCase()
        }
        return brand
    }
}
