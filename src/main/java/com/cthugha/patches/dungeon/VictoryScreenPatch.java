package com.cthugha.patches.dungeon;

// Thanks to temple9!
@Deprecated
@SuppressWarnings("unused")
public class VictoryScreenPatch {
//	private static final ReturnToMenuButton continueButton = new ReturnToMenuButton();
//	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(CthughaHelper.makeID("ContinueButton"));
//	private static boolean shouldSubmitVictoryMetrics = false;
//
//	private static boolean canContinue() {
//		return AbstractDungeon.player instanceof Cthugha && !Settings.isEndless &&
//				AbstractDungeon.id.equals(TheEnding.ID);
//	}
//
//	@SpirePatch(
//			clz = VictoryScreen.class,
//			method = SpirePatch.CONSTRUCTOR
//	)
//	public static class PatchConstructor {
//		public static void Postfix(VictoryScreen screen) {
//			if (canContinue()) {
//				ReturnToMenuButton returnButton = ReflectionHacks.getPrivate(screen,
//						GameOverScreen.class, "returnButton");
//
//				if (returnButton != null) {
//					returnButton.appear(0.4F * Settings.WIDTH, 0.15F * Settings.HEIGHT, VictoryScreen.TEXT[0]);
//					continueButton.appear(0.6F * Settings.WIDTH, 0.15F * Settings.HEIGHT, uiStrings.TEXT[0]);
//				}
//			}
//		}
//	}
//
//	@SpirePatch(
//			clz = VictoryScreen.class,
//			method = "reopen",
//			paramtypez = {
//					boolean.class
//			}
//	)
//	public static class PatchReopen {
//		@SpireInsertPatch(rloc = 6)
//		public static SpireReturn<Void> Insert(VictoryScreen screen, boolean fromVictoryUnlock) {
//			if (!canContinue()) {
//				return SpireReturn.Continue();
//			}
//
//			boolean showingStats = ReflectionHacks.getPrivate(screen, GameOverScreen.class, "showingStats");
//			ReturnToMenuButton returnButton = ReflectionHacks.getPrivate(screen,
//					GameOverScreen.class, "returnButton");
//			ArrayList<AbstractUnlock> unlockBundle = ReflectionHacks.getPrivate(screen,
//					GameOverScreen.class, "unlockBundle");
//
//			if (returnButton == null)
//				return SpireReturn.Continue();
//
//			if (fromVictoryUnlock) {
//				returnButton.appear(Settings.WIDTH * 0.4F, Settings.HEIGHT * 0.15F, VictoryScreen.TEXT[0]);
//				continueButton.appear(Settings.WIDTH * 0.6F, Settings.HEIGHT * 0.15F, uiStrings.TEXT[0]);
//			}
//			else if (!showingStats) {
//				returnButton.appear(Settings.WIDTH * 0.4F, Settings.HEIGHT * 0.15F, VictoryScreen.TEXT[0]);
//				continueButton.appear(Settings.WIDTH * 0.6F, Settings.HEIGHT * 0.15F, uiStrings.TEXT[0]);
//			}
//			else if (unlockBundle == null) {
//				returnButton.appear(Settings.WIDTH * 0.4F, Settings.HEIGHT * 0.15F, VictoryScreen.TEXT[0]);
//				continueButton.appear(Settings.WIDTH * 0.6F, Settings.HEIGHT * 0.15F, uiStrings.TEXT[0]);
//			} else {
//				returnButton.appear(Settings.WIDTH * 0.4F, Settings.HEIGHT * 0.15F, VictoryScreen.TEXT[5]);
//				continueButton.appear(Settings.WIDTH * 0.6F, Settings.HEIGHT * 0.15F, uiStrings.TEXT[0]);
//			}
//			return SpireReturn.Return();
//		}
//	}
//
//	@SpirePatch(
//			clz = VictoryScreen.class,
//			method = "updateStatsScreen"
//	)
//	public static class PatchUpdateStatsScreen {
//		@SpireInsertPatch(rloc = 9)
//		public static SpireReturn<Void> Insert(VictoryScreen screen) {
//			ReturnToMenuButton returnButton = ReflectionHacks.getPrivate(screen,
//					GameOverScreen.class, "returnButton");
//
//			if (returnButton == null)
//				return SpireReturn.Continue();
//
//			if (canContinue()) {
//				returnButton.x = 0.4F * Settings.WIDTH;
//				continueButton.y = returnButton.y;
//			}
//			return SpireReturn.Continue();
//		}
//	}
//
//	@SpirePatch(
//			clz = VictoryScreen.class,
//			method = "update"
//	)
//	public static class PatchUpdate {
//		@SpireInsertPatch(rloc = 0)
//		public static void Insert(VictoryScreen screen) {
//			if (canContinue()) {
//				ReturnToMenuButton returnButton = ReflectionHacks.getPrivate(screen,
//						GameOverScreen.class, "returnButton");
//
//				if (returnButton == null)
//					return;
//
//				continueButton.update();
//				if (continueButton.hb.clicked) {
//					continueButton.hb.clicked = false;
//					returnButton.hide();
//					continueButton.hide();
//					CardCrawlGame.nextDungeon = TheTricuspidGate.ID;
//					AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;
//					AbstractDungeon.fadeOut();
//					AbstractDungeon.isDungeonBeaten = true;
//				} else if (returnButton.hb.clicked) {
//					SaveAndContinue.deleteSave(AbstractDungeon.player);
//					shouldSubmitVictoryMetrics = true;
//
//					ReflectionHacks.privateMethod(VictoryScreen.class, "submitVictoryMetrics")
//							.invoke(screen);
//
//					shouldSubmitVictoryMetrics = false;
//				}
//			}
//		}
//	}
//
//	@SpirePatch(
//			clz = VictoryScreen.class,
//			method = "render"
//	)
//	public static class PatchRender {
//		public static void Postfix(VictoryScreen screen, SpriteBatch sb) {
//			if (canContinue()) {
//				continueButton.render(sb);
//			}
//		}
//	}
//
//	@SpirePatch(
//			clz = VictoryScreen.class,
//			method = "hide"
//	)
//	public static class PatchHide {
//		public static void Postfix(VictoryScreen screen) {
//			continueButton.hide();
//		}
//	}
//
//	@SpirePatch(
//			clz = VictoryScreen.class,
//			method = "submitVictoryMetrics"
//	)
//	public static class PatchSubmitVictoryMetrics {
//		public static SpireReturn<Void> Prefix(VictoryScreen screen) {
//			if (AbstractDungeon.player instanceof Cthugha && AbstractDungeon.id.equals(TheEnding.ID) &&
//					!shouldSubmitVictoryMetrics) {
//				return SpireReturn.Return();
//			}
//			return SpireReturn.Continue();
//		}
//	}
}
