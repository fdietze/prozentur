package org.foodieboys.zentuhr

import android.os.Bundle
import android.preference.{PreferenceFragment, PreferenceActivity}

class WidgetPreferences extends PreferenceActivity {

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit()


  }

  class MyPreferenceFragment extends PreferenceFragment {
    override def onCreate(savedInstanceState: Bundle): Unit = {
      super.onCreate(savedInstanceState)
      addPreferencesFromResource(R.xml.widget_preferences)
    }
  }


}