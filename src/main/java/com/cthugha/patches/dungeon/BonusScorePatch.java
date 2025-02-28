package com.cthugha.patches.dungeon;

import basemod.ReflectionHacks;
import com.cthugha.characters.Cthugha;
import com.cthugha.dungeons.the_tricuspid_gate.TheTricuspidGate;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.ScoreBonusStrings;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.screens.GameOverStat;
import com.megacrit.cardcrawl.screens.VictoryScreen;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class BonusScorePatch {
	public static boolean bonus = false;
	public static boolean is_cthugha = false;

	@SpirePatch(clz = GameOverScreen.class, method = "resetScoreChecks")
	public static class GameOverScreenResetPatch {
		@SpirePostfixPatch
		public static void Postfix() {
			bonus = false;
			is_cthugha = false;
		}
	}

	@SpirePatch(clz = GameOverScreen.class, method = "checkScoreBonus", paramtypez = {boolean.class})
	public static class GameOverScreenCheckBonusPatch {
		@SpirePostfixPatch
		public static int Postfix(int points, boolean isVictory) {
			is_cthugha = (AbstractDungeon.player instanceof Cthugha);

			if (isVictory && GameOverScreen.isVictory && CardCrawlGame.dungeon instanceof TheTricuspidGate) {
				bonus = true;
				points += 500;
			}
			else
				bonus = false;

			if (CardCrawlGame.dungeon instanceof TheTricuspidGate)
				points += 250; // 碎心者

			return points;
		}
	}

	private static GameOverStat getStat() {
		ScoreBonusStrings bonusStrings = CardCrawlGame.languagePack.getScoreString("Cthugha:BonusScore");
		return new GameOverStat(bonusStrings.NAME, bonusStrings.DESCRIPTIONS[0],
				Integer.toString(500));
	}

	@SpirePatch(clz = VictoryScreen.class, method = "createGameOverStats")
	public static class VictoryScreenCreateStatsPatch {
		@SpireInsertPatch(rloc = 199)
		public static void Insert(VictoryScreen _inst, ArrayList<GameOverStat> ___stats) {
			if (CardCrawlGame.dungeon instanceof TheTricuspidGate) {
				ScoreBonusStrings heart_breaker = ReflectionHacks.getPrivateStatic(
						GameOverScreen.class, "HEARTBREAKER");

				___stats.add(new GameOverStat(heart_breaker.NAME, heart_breaker.DESCRIPTIONS[0],
						Integer.toString(250)));
			}

			if (bonus)
				___stats.add(getStat());
		}
	}

	@SpirePatch(clz = DeathScreen.class, method = "createGameOverStats")
	public static class DeathScreenCreateStatsPatch {
		@SpireInsertPatch(rloc = 191)
		public static void Insert(DeathScreen _inst, ArrayList<GameOverStat> ___stats) {
			if (CardCrawlGame.dungeon instanceof TheTricuspidGate) {
				ScoreBonusStrings heart_breaker = ReflectionHacks.getPrivateStatic(
						GameOverScreen.class, "HEARTBREAKER");

				___stats.add(new GameOverStat(heart_breaker.NAME, heart_breaker.DESCRIPTIONS[0],
						Integer.toString(250)));
			}
		}
	}
}
