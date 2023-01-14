/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2022 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.ui.settingsfragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import org.catrobat.catroid.R;
import org.catrobat.catroid.sync.ProjectsCategoriesSync;
import org.catrobat.catroid.ui.MainMenuActivity;
import org.catrobat.catroid.ui.SettingsActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static org.koin.java.KoinJavaComponent.inject;

public class AdvancedModeSettingsFragment extends PreferenceFragment {
	public static final String TAG = AdvancedModeSettingsFragment.class.getSimpleName();

	@Override
	public void onStop() {
		super.onStop();
		changeLanguage(SettingsFragment.isAdvancedModeLanguageEnabled(getContext()));
	}

	@Override
	public void onResume() {
		super.onResume();
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getPreferenceScreen().getTitle());
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SettingsFragment.setToChosenLanguage(getActivity());

		addPreferencesFromResource(R.xml.advanced_mode_preferences);

		CheckBoxPreference advModePreference =
				(CheckBoxPreference) findPreference(SettingsFragment.SETTINGS_CATBLOCKS_ADV_MODE);
		CheckBoxPreference advModeLanguagePreference =
				 (CheckBoxPreference) findPreference(SettingsFragment.SETTINGS_CATBLOCKS_ADV_MODE_LANGUAGE);
		advModeLanguagePreference.setEnabled(advModePreference.isChecked());

		advModePreference.setOnPreferenceChangeListener((preference, isChecked) -> {
			advModeLanguagePreference.setEnabled((Boolean) isChecked);
			advModeLanguagePreference.setChecked((Boolean) isChecked);
			return true;
		});
	}

	public void changeLanguage(@NonNull Boolean enabled) {
		String currentLanguage = SettingsFragment.getCurrentLanguage(getContext());
		String savedLanguage = SettingsFragment.getLastUsedLanguage(getContext());
		String englishTag = "en";
		if (enabled) {
			if (currentLanguage.equals(englishTag)) {
				return;
			}
			SettingsFragment.setLastUsedLanguage(getContext(), currentLanguage);
			SettingsFragment.setLanguageSharedPreference(getActivity().getBaseContext(), englishTag);
			SettingsFragment.listPreference.setValue(englishTag);
		} else {
			if (!currentLanguage.equals(englishTag) || savedLanguage.equals(englishTag) ||
					savedLanguage.isEmpty() || currentLanguage.equals(savedLanguage)) {
				return;
			}
			SettingsFragment.setLanguageSharedPreference(getActivity().getBaseContext(), savedLanguage);
			SettingsFragment.listPreference.setValue(savedLanguage);
		}
		startActivity(new Intent(getActivity().getBaseContext(), MainMenuActivity.class));
		startActivity(new Intent(getActivity().getBaseContext(), SettingsActivity.class));
		getActivity().finishAffinity();
		new Thread(() -> inject(ProjectsCategoriesSync.class).getValue().sync(true));
	}
}
