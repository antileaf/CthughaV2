package com.cthugha.patches.dungeon;

import com.cthugha.characters.Cthugha;
import com.cthugha.dungeons.the_tricuspid_gate.TheTricuspidGate;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class FifthActCompatibilityPatch {
	private static final Logger logger = LogManager.getLogger(FifthActCompatibilityPatch.class.getName());

	@SpirePatch(clz = AbstractDungeon.class, method = "populatePathTaken", paramtypez = {SaveFile.class})
	public static class AbstractDungeonPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(AbstractDungeon.class, "pathX"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(AbstractDungeon _inst, SaveFile saveFile) {
			if (saveFile.current_room.equals(TreasureRoomBoss.class.getName()) &&
					CardCrawlGame.dungeon instanceof TheTricuspidGate)
				AbstractDungeon.nextRoom = (AbstractDungeon.map.get(saveFile.room_y)).get(saveFile.room_x);
		}
	}

	@SpirePatch(clz = TreasureRoomBoss.class, method = "getNextDungeonName")
	public static class TreasureRoomBossPatch {
		@SpirePrefixPatch
		public static SpireReturn<String> Prefix(TreasureRoomBoss _inst) {
			if (CardCrawlGame.dungeon instanceof TheTricuspidGate)
				return SpireReturn.Return(CardCrawlGame.nextDungeon);

			return SpireReturn.Continue();
		}

		@SpirePostfixPatch
		public static String Postfix(String ret, TreasureRoomBoss _inst) {
			if (ret == null && AbstractDungeon.player instanceof Cthugha &&
					AbstractDungeon.id.equals(TheEnding.ID))
				return TheTricuspidGate.ID;

			return ret;
		}
	}

	@SpirePatch(clz = ProceedButton.class, method = "update", paramtypez = {})
	public static class ProceedButtonPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(Settings.class, "isDemo"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Void> Insert(ProceedButton _inst) {
			if (CardCrawlGame.dungeon instanceof TheTricuspidGate) {
				AbstractDungeon.dungeonMapScreen.open(false);
				_inst.hide();
				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}

//	@SpirePatch(clz = CorruptHeart.class, method = "die", paramtypez = {})
//	public static class CorruptHeartPatch {
//		@SpirePostfixPatch
//		public static void Postfix(CorruptHeart _inst) {
//			if (AbstractDungeon.player instanceof Cthugha) {
//				CardCrawlGame.stopClock = false;
//			}
//		}
//	}

//	@SpirePatch(clz = TheEnding.class, method = "generateSpecialMap", paramtypez = {})
//	public static class TheEndingPatch {
////		private static class Locator extends SpireInsertLocator {
////			@Override
////			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
////				int[] tmp = LineFinder.findAllInOrder(ctBehavior,
////						new Matcher.MethodCallMatcher(ArrayList.class, "add"));
////				return new int[] {tmp[tmp.length - 1]};
////			}
////		}
//
//		public static void log() {
//			logger.info("qwq");
//		}
//
//		@SpireInstrumentPatch
//		public static ExprEditor Instrument() {
//			return new ExprEditor() {
//				@Override
//				public void edit(NewExpr m) throws CannotCompileException {
//					if (m.getClassName().equals(TrueVictoryRoom.class.getName())) {
//						m.replace("{ if (" + AbstractDungeon.class.getName() +
//								".player instanceof " + Cthugha.class.getName() + ") { $_ = new " +
//								TreasureRoomBoss.class.getName() + "(); " +
//								TheEndingPatch.class.getName() + ".log(); } else { $_ = $proceed($$); } }");
//
//						logger.info("Instrumented TrueVictoryRoom creation.");
//					}
//				}
//			};
//		}
//	}
}
