package com.cthugha.utils;

import basemod.ModLabeledButton;
import basemod.ModPanel;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.io.IOException;
import java.util.Properties;

public class ConfigHelper {
	public static final String SKIN = "skin";
	public static final String SHOW_TUTORIAL = "show_tutorial";
	public static final String SHOW_CONTROLLER_TUTORIAL = "show_controller_tutorial";

	@Deprecated
	public static final String HAS_UNLOCKED_SIGNATURE = "has_unlocked_signature";
	@Deprecated
	public static final String USE_SIGNATURE = "use_signature";

	public static final String HAS_CHECKED_HISTORY = "has_checked_history";

	public static SpireConfig conf = null;

	public static void load() {
		Properties defaults = new Properties();
		defaults.setProperty(SKIN, "0");
		defaults.setProperty(SHOW_TUTORIAL, "true");
		defaults.setProperty(SHOW_CONTROLLER_TUTORIAL, "true");
//		defaults.setProperty(HAS_UNLOCKED_SIGNATURE, "false");
//		defaults.setProperty(USE_SIGNATURE, "false");
		defaults.setProperty(HAS_CHECKED_HISTORY, "false");

		try {
			conf = new SpireConfig("Cthugha", "config", defaults);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getSkin() {
		return conf.getInt(SKIN);
	}

	public static void setSkin(int skin) {
		conf.setInt(SKIN, skin);
	}

	public static boolean showTutorial() {
		return conf.getBool(SHOW_TUTORIAL);
	}

	public static void setShowTutorial(boolean show) {
		conf.setBool(SHOW_TUTORIAL, show);
	}

	public static boolean showControllerTutorial() {
		return conf.getBool(SHOW_CONTROLLER_TUTORIAL);
	}

	public static void setShowControllerTutorial(boolean show) {
		conf.setBool(SHOW_CONTROLLER_TUTORIAL, show);
	}

//	public static boolean hasUnlockedSignature() {
//		return true;
////		return conf.getBool(HAS_UNLOCKED_SIGNATURE);
//	}
//
//	public static void setHasUnlockedSignature(boolean unlocked) {
//		conf.setBool(HAS_UNLOCKED_SIGNATURE, unlocked);
//	}
//
//	public static boolean useSignature() {
//		return conf.getBool(USE_SIGNATURE);
//	}
//
//	public static void setUseSignature(boolean use) {
//		conf.setBool(USE_SIGNATURE, use);
//	}

	public static boolean hasCheckedHistory() {
		return conf.getBool(HAS_CHECKED_HISTORY);
	}

	public static void setHasCheckedHistory(boolean checked) {
		conf.setBool(HAS_CHECKED_HISTORY, checked);
	}

	public static void save() {
		try {
			conf.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ModPanel createConfigPanel() {
		ModPanel panel = new ModPanel();

		UIStrings ui = CardCrawlGame.languagePack.getUIString(CthughaHelper.makeID("ResetTutorial"));
		panel.addUIElement(new ModLabeledButton(ui.TEXT[0], 350.0F, 700.0F, panel, (me) -> {
			setShowTutorial(true);
			setShowControllerTutorial(true);
			save();
		}));

		return panel;
	}
}
