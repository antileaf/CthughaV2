package com.cthugha.patches.dungeon;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.dungeons.the_tricuspid_gate.TheTricuspidGate;
import com.cthugha.dungeons.the_tricuspid_gate.monsters.Areshkagal;
import com.cthugha.utils.CthughaHelper;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryPath;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryScreen;
import com.megacrit.cardcrawl.screens.runHistory.RunPathElement;
import com.megacrit.cardcrawl.screens.stats.BattleStats;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.Objects;

@SuppressWarnings("unused")
public class VictoryQuestionPatch {
	private static String victoryQuestion, soaring;

	@SpirePatch(clz = DeathScreen.class, method = SpirePatch.CONSTRUCTOR,
			paramtypez = {MonsterGroup.class})
	public static class DeathScreenPatch {
		@SpireInsertPatch(rloc = 33)
		public static void Insert(DeathScreen _inst, MonsterGroup m) {
			if (CardCrawlGame.dungeon instanceof TheTricuspidGate)
				GameOverScreen.isVictory = true;
		}
	}

	@SpirePatch(clz = DeathScreen.class, method = "getDeathBannerText", paramtypez = {})
	public static class DeathScreenTextPatch {
		public static String Postfix(String _res, DeathScreen _inst) {
			if (CardCrawlGame.dungeon instanceof TheTricuspidGate)
				return DeathScreen.TEXT[1];
			return _res;
		}
	}

	@SpirePatch(clz = DeathScreen.class, method = "submitVictoryMetrics", paramtypez = {})
	public static class DeathScreenMetricsPatch {
		public static void Prefix(DeathScreen _inst) {
			if (CardCrawlGame.dungeon instanceof TheTricuspidGate &&
					AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss &&
					AbstractDungeon.lastCombatMetricKey.equals(Areshkagal.ID))
				CardCrawlGame.metricData.addEncounterData();
		}
	}

	@SpirePatch(clz = RunHistoryScreen.class, method = "renderRunHistoryScreen",
			paramtypez = {SpriteBatch.class})
	public static class RunHistoryScreenPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior)
					throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(Settings.class, "GREEN_TEXT_COLOR"));
			}
		}

		@SpireInsertPatch(locator = Locator.class, localvars = {"resultText", "specialModeText"})
		public static void Insert(RunHistoryScreen _inst, SpriteBatch sb,
								  @ByRef String[] resultText, String specialModeText) {
			if (victoryQuestion == null) {
				UIStrings ui = CardCrawlGame.languagePack.getUIString(CthughaHelper.makeID("VictoryQuestion"));
				victoryQuestion = ui.TEXT[0];
				soaring = ui.TEXT[1];
			}

			RunHistoryPath path = ReflectionHacks.getPrivate(_inst,
					RunHistoryScreen.class, "runPath");

			if (path != null && !path.pathElements.isEmpty()) {
				boolean hasDefeatedHeart = path.pathElements.stream()
						.filter(e -> e.nodeType == RunPathElement.PathNodeType.BOSS)
						.map(e -> (BattleStats) ReflectionHacks
								.getPrivate(e, RunPathElement.class, "battleStats"))
						.filter(Objects::nonNull)
						.anyMatch(s -> s.enemies.equals(CorruptHeart.ID));

				boolean hasDefeatedAreshkagal = path.pathElements.stream()
						.filter(e -> e.nodeType == RunPathElement.PathNodeType.BOSS)
						.map(e -> (BattleStats) ReflectionHacks
								.getPrivate(e, RunPathElement.class, "battleStats"))
						.filter(Objects::nonNull)
						.anyMatch(s -> s.enemies.equals(Areshkagal.ID));

				RunPathElement last = path.pathElements.get(path.pathElements.size() - 1);

				if (hasDefeatedAreshkagal && last.nodeType != RunPathElement.PathNodeType.BOSS)
					resultText[0] = soaring + specialModeText;
				else {
					boolean killedByAreshkagal = false;
					if (last.nodeType == RunPathElement.PathNodeType.BOSS) {
						BattleStats stats = ReflectionHacks.getPrivate(last, RunPathElement.class,
								"battleStats");
						if (stats != null && stats.enemies.equals(Areshkagal.ID))
							killedByAreshkagal = true;
					}

					if (hasDefeatedHeart || killedByAreshkagal)
						resultText[0] = victoryQuestion + specialModeText;
				}
			}
		}
	}
}
