package com.cthugha.patch;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.characters.Cthugha;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.cthugha.enums.MyPlayerClassEnum;
import com.cthugha.ui.SkinSelectScreen;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;

@SuppressWarnings("unused")
public class SkinSelectPatch {
	public static boolean isCthughaSelected() {
		return (CardCrawlGame.chosenCharacter == MyPlayerClassEnum.CTHUGHA_PLAYER_CLASS
				&& (Boolean) ReflectionHacks.getPrivate(CardCrawlGame.mainMenuScreen.charSelectScreen,
				CharacterSelectScreen.class, "anySelected"));
	}

	@SpirePatch(clz = CharacterSelectScreen.class, method = "update", paramtypez = {})
	public static class UpdatePatch {
		@SpirePostfixPatch
		public static void Postfix(CharacterSelectScreen _inst) {
			if (SkinSelectPatch.isCthughaSelected()) {
				SkinSelectScreen.Inst.update();

				if (SkinSelectScreen.shouldUpdateBackground) {
					_inst.bgCharImg = SkinSelectScreen.Inst.getPortrait();
					SkinSelectScreen.shouldUpdateBackground = false;
				}
			}
		}
	}

//	@SpirePatch(clz = CharacterSelectScreen.class, method = "render")
//	public static class RenderPortraitPatch {
//		@SpireInsertPatch(rloc = 62)
//		public static void Insert(CharacterSelectScreen _inst, SpriteBatch sb) {
//			if (SkinSelectPatch.isKaltsitSelected())
//				SkinSelectScreen.Inst.renderPortrait(sb);
//		}
//	}


	@SpirePatch(clz = CharacterSelectScreen.class, method = "render", paramtypez = {SpriteBatch.class})
	public static class RenderPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				int[] cancelButton = LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(CharacterSelectScreen.class, "cancelButton"));
				int[] buttonRender = LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(MenuCancelButton.class, "render"));

				if (cancelButton[0] != buttonRender[0])
					throw new PatchingException("Failed to find cancel button render call");

				return new int[]{buttonRender[0]};
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(CharacterSelectScreen _inst, SpriteBatch sb) {
			if (SkinSelectPatch.isCthughaSelected())
				SkinSelectScreen.Inst.render(sb);
		}
	}

	@SpirePatch(clz = CharacterOption.class, method = "updateHitbox", paramtypez = {})
	public static class UpdateBackgroundPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				int[] tmp = LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(
								AbstractPlayer.class, "doCharSelectScreenSelectEffect"));

				return new int[]{tmp[tmp.length - 1] - 2};
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(CharacterOption _inst) {
			if (_inst.c.chosenClass == MyPlayerClassEnum.CTHUGHA_PLAYER_CLASS)
				SkinSelectScreen.shouldUpdateBackground = true;
		}
	}
}
