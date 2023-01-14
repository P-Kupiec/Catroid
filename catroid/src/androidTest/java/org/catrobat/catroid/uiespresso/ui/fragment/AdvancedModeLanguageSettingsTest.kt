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

package org.catrobat.catroid.uiespresso.ui.fragment

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.catrobat.catroid.ui.SpriteActivity
import org.catrobat.catroid.ui.settingsfragments.AdvancedModeSettingsFragment
import org.catrobat.catroid.ui.settingsfragments.SettingsFragment
import org.catrobat.catroid.uiespresso.util.rules.FragmentActivityTestRule
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class AdvancedModeLanguageSettingsTest {

    @get:Rule
    var baseActivityTestRule = FragmentActivityTestRule(
        SpriteActivity::class.java, SpriteActivity.EXTRA_FRAGMENT_POSITION,
        SpriteActivity.FRAGMENT_SCRIPTS
    )

    private val currentLanguage = Locale.GERMAN
    private val advModeLanguage = Locale.ENGLISH

    @Before
    fun setUp() {
        SettingsFragment.setUseCatBlocks(ApplicationProvider.getApplicationContext(), false)
        SettingsFragment.setCatBlocksAdvancedMode(ApplicationProvider.getApplicationContext(), true)
        SettingsFragment.setAdvancedModeLanguage(ApplicationProvider.getApplicationContext(),
                                                 true, currentLanguage.toLanguageTag())
    }

    @After
    fun tearDown() {
        SettingsFragment.setUseCatBlocks(ApplicationProvider.getApplicationContext(), false)
        SettingsFragment.setCatBlocksAdvancedMode(ApplicationProvider.getApplicationContext(), false)
        SettingsFragment.setAdvancedModeLanguage(ApplicationProvider.getApplicationContext(),
                                                 false, "en")
        baseActivityTestRule.finishActivity()
    }

    @Test
    fun advancedModeLanguageSwitch() {
        SettingsFragment.setLanguageSharedPreference(
            ApplicationProvider.getApplicationContext(),
            currentLanguage.toLanguageTag()
        )
        baseActivityTestRule.launchActivity()

        val advModeSetting = AdvancedModeSettingsFragment()
        advModeSetting.changeLanguage(SettingsFragment.isAdvancedModeLanguageEnabled(ApplicationProvider.getApplicationContext()))

        Assert.assertEquals(
            Locale.getDefault().displayLanguage,
            advModeLanguage.displayLanguage
        )

        advModeSetting.changeLanguage(false)

        Assert.assertEquals(
            Locale.getDefault().displayLanguage,
            currentLanguage.displayLanguage
        )
    }
}
