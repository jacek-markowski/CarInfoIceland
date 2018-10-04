package `is`.markowski.jacek.vehicle.registration.plates.of.iceland.util

import android.app.Activity
import android.util.Log
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.security.ProviderInstaller
// android 4.x hack
fun updateAndroidSecurityProvider(callingActivity: Activity) {
    try {
        ProviderInstaller.installIfNeeded(callingActivity)
    } catch (e: GooglePlayServicesRepairableException) {
        // Thrown when Google Play Services is not installed, up-to-date, or enabled
        // Show dialog to allow users to install, update, or otherwise enable Google Play services.
        GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0)
    } catch (e: GooglePlayServicesNotAvailableException) {
        Log.e("SecurityException", "Google Play Services not available.")
    }

}