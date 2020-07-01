package org.mifos.core

import android.content.Context
import org.mifos.core.preferencesmanager.MifosPreferenceManager
import org.mifos.core.viewmodels.AuthViewModel

/**
 * Created by grandolf49 on 06-06-2020
 *
 * Access point to the sdk. All the services provided by the sdk will be accessed using the MifosSdk
 * object. It uses Builder design pattern to return an object of MifosSdk. The user will ask the SDK
 * for a service which he/she needs. The sdk talks to the DataManager to fetch the appropriate service
 * and return the object.
 */
class MifosSdk private constructor(context: Context) {

    private val mifosPreferenceManager = MifosPreferenceManager(context)

    private val authViewModel: AuthViewModel = AuthViewModel()

    fun getAuthApi(): AuthViewModel {
        return authViewModel
    }

    /**
     * Returns the currently signed in user if any.
     * Else returns null value

    fun getCurrentUser(): User? {
    if ()
    }*/

    /**
     * Builder class to build a MifosSdk object which can be used to access the APIs.
     * */
    data class Builder(private var context: Context) {
        fun setContext(context: Context) = apply { this.context = context }
        fun build() = MifosSdk(context)
    }
}