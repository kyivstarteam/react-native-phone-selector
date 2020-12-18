package ua.kyivstar.reactnativephoneselector

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import androidx.annotation.Nullable

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;


class PhoneSelectorModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

  private val E_PHONE_SELECTOR_ERROR = "E_PHONE_SELECTOR_ERROR"
  private val SELECTED_PHONE_PROPERTY = "selectedPhone"

  private var reactContext: ReactApplicationContext? = reactContext
  private var promise: Promise? = null

  override fun initialize() {
    super.initialize()
    reactContext?.addActivityEventListener(mActivityEventListener)
  }

  override fun getName(): String {
    return "PhoneSelector"
  }

  @ReactMethod
  fun invokePhoneSelector(promise: Promise) {
    try {
      val hintRequest: HintRequest = HintRequest.Builder()
        .setPhoneNumberIdentifierSupported(true)
        .build()
      val intent: PendingIntent = Credentials.getClient(reactContext as Context).getHintPickerIntent(hintRequest)
      this.promise = promise
      val activity = currentActivity
      activity?.startIntentSenderForResult(intent.intentSender,
        1243, null, 0, 0, 0)
    } catch (e: IllegalViewOperationException) {
      promise.reject(E_PHONE_SELECTOR_ERROR, e)
    } catch (e: SendIntentException) {
      promise.reject(E_PHONE_SELECTOR_ERROR, e)
    }
  }

  private val mActivityEventListener: ActivityEventListener = object : BaseActivityEventListener() {
    override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, intent: Intent) {
      when (requestCode) {
        1243 -> {
          if (resultCode == RESULT_OK) {
            try {
              val credential = intent.getParcelableExtra<Credential>(Credential.EXTRA_KEY)
              if (credential != null) {
                val mobNumber: String = credential.getId()
                val map = Arguments.createMap()
                map.putString(SELECTED_PHONE_PROPERTY, mobNumber)
                promise!!.resolve(map)
              } else {
                promise!!.reject(E_PHONE_SELECTOR_ERROR, Error("Credential is empty"))
              }
            } catch (e: Exception) {
              promise!!.reject(E_PHONE_SELECTOR_ERROR, e)
            }
          }
          promise = null
        }
      }
    }
  }

  fun onConnected(@Nullable bundle: Bundle?) {}

  fun onConnectionSuspended(i: Int) {}

  fun onConnectionFailed(connectionResult: ConnectionResult) {}


}
