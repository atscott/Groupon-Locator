package com.groupon.activities;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.text.InputType;
import com.groupon.R;

/**
 * User: atscott
 * Date: 11/10/13
 * Time: 11:44 AM
 */
public class SettingsActivity extends PreferenceActivity
{
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
  }

  public static class MyPreferenceFragment extends PreferenceFragment
  {
    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.preferences);
      EditTextPreference pref = (EditTextPreference)findPreference("radius");
      pref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
    }
  }
}