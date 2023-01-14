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

import android.view.View
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.catrobat.catroid.R
import org.catrobat.catroid.UiTestCatroidApplication.Companion.projectManager
import org.catrobat.catroid.content.Project
import org.catrobat.catroid.content.Sprite
import org.catrobat.catroid.ui.SpriteActivity
import org.catrobat.catroid.ui.settingsfragments.SettingsFragment
import org.catrobat.catroid.uiespresso.util.rules.FragmentActivityTestRule
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AdvancedModeActionButtonsTest {

    @get:Rule
    var baseActivityTestRule = FragmentActivityTestRule(
        SpriteActivity::class.java, SpriteActivity.EXTRA_FRAGMENT_POSITION,
        SpriteActivity.FRAGMENT_SCRIPTS
    )

    @Before
    fun setUp() {
        SettingsFragment.setUseCatBlocks(ApplicationProvider.getApplicationContext(), false)
        SettingsFragment.setCatBlocksAdvancedMode(ApplicationProvider.getApplicationContext(), true)
        createProject()
        baseActivityTestRule.launchActivity()
    }

    @After
    fun tearDown() {
        SettingsFragment.setUseCatBlocks(ApplicationProvider.getApplicationContext(), false)
        SettingsFragment.setCatBlocksAdvancedMode(ApplicationProvider.getApplicationContext(), false)
        baseActivityTestRule.finishActivity()
    }

    @Test
    fun advancedModeActionButtons() {

        Espresso.openContextualActionModeOverflowMenu()

        Espresso.onView(withText(R.string.catblocks)).perform(ViewActions.click())

        val playBtn = baseActivityTestRule.activity.findViewById<View?>(R.id.button_play)

        val addBtn = baseActivityTestRule.activity.findViewById<View?>(R.id.button_add)

        Assert.assertEquals(playBtn.backgroundTintList, "advcolor")

        Assert.assertEquals(addBtn.backgroundTintList, "advcolor")

        Espresso.openContextualActionModeOverflowMenu()

        Espresso.onView(withText(R.string.catblocks)).perform(ViewActions.click())

        Assert.assertEquals(playBtn.backgroundTintList, "standardcolor")

        Assert.assertEquals(addBtn.backgroundTintList, "standardcolor")
    }

    private fun createProject() {
        val projectName = javaClass.simpleName
        val project = Project(ApplicationProvider.getApplicationContext(), projectName)
        val sprite = Sprite("testSprite")
        project.defaultScene.addSprite(sprite)
        projectManager.currentProject = project
        projectManager.currentSprite = sprite
    }
}
