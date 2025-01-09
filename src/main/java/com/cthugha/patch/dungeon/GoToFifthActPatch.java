package com.cthugha.patch.dungeon;

import com.cthugha.characters.Cthugha;
import com.cthugha.dungeons.the_tricuspid_gate.TheTricuspidGate;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;

@SuppressWarnings("unused")
public class GoToFifthActPatch {

	private static boolean canContinue() {
		return AbstractDungeon.player instanceof Cthugha && !Settings.isEndless &&
				AbstractDungeon.id.equals(TheEnding.ID);
	}

	@SpirePatch(clz = ProceedButton.class, method = "goToTrueVictoryRoom", paramtypez = {})
	public static class ProceedButtonPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(ProceedButton _inst) {
			if (canContinue()) {
				CardCrawlGame.music.fadeOutBGM();
				CardCrawlGame.music.fadeOutTempBGM();
				AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;
				AbstractDungeon.fadeOut();
				AbstractDungeon.isDungeonBeaten = true;
				CardCrawlGame.nextDungeon = TheTricuspidGate.ID;

				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}
}
