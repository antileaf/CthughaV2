package com.cthugha.patches.dungeon;

import actlikeit.patches.ContinueOntoHeartPatch;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.cthugha.characters.Cthugha;
import com.cthugha.dungeons.the_tricuspid_gate.TheTricuspidGate;
import com.cthugha.dungeons.the_tricuspid_gate.proceed.AnotherProceedButton;
import com.cthugha.enums.CurrentScreenEnum;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SuppressWarnings("unused")
public class GoToFifthActPatch {

	private static boolean canContinue() {
		return !Settings.isEndless && AbstractDungeon.id.equals(TheEnding.ID) &&
						AbstractDungeon.getCurrRoom() != null &&
						AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss;
	}

	@SpirePatch(clz = OverlayMenu.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<AnotherProceedButton> another = new SpireField<>(AnotherProceedButton::new);
	}

	@SpirePatch(clz = ProceedButton.class, method = "update", paramtypez = {})
	public static class UpdateAnotherButtonPatch {
		@SpirePostfixPatch
		public static void Postfix(ProceedButton _inst) {
			if (_inst == AbstractDungeon.overlayMenu.proceedButton)
				Fields.another.get(AbstractDungeon.overlayMenu).update();
		}
	}

	@SpirePatch(clz = ProceedButton.class, method = "render", paramtypez = {SpriteBatch.class})
	public static class RenderAnotherButtonPatch {
		@SpirePostfixPatch
		public static void Postfix(ProceedButton _inst, SpriteBatch sb) {
			if (_inst == AbstractDungeon.overlayMenu.proceedButton)
				Fields.another.get(AbstractDungeon.overlayMenu).render(sb);
		}
	}

	@SpirePatch(clz = ProceedButton.class, method = "show", paramtypez = {})
	public static class ShowAnotherButtonPatch {
		@SpirePostfixPatch
		public static void Postfix(ProceedButton _inst) {
			if (_inst == AbstractDungeon.overlayMenu.proceedButton && canContinue())
				Fields.another.get(AbstractDungeon.overlayMenu).show();
		}
	}

	@SpirePatch(clz = ProceedButton.class, method = "hide", paramtypez = {})
	public static class HideAnotherButtonPatch {
		@SpirePostfixPatch
		public static void Postfix(ProceedButton _inst) {
			if (_inst == AbstractDungeon.overlayMenu.proceedButton && canContinue())
				Fields.another.get(AbstractDungeon.overlayMenu).hide();
		}
	}

//	public static boolean work(ProceedButton _inst) {
//		System.out.println("qwq");
//
//		if (canContinue()) {
//			System.out.println("true");
//
//			BaseMod.openCustomScreen(CurrentScreenEnum.PROCEED_CONFIRM_SCREEN,
//					_inst == Fields.another.get(AbstractDungeon.overlayMenu));
//
//			return true;
//		}
//
//		return false;
//	}

	@SpirePatch(clz = ProceedButton.class, method = "goToTrueVictoryRoom", paramtypez = {})
	public static class ProceedButtonPatch {
//		@SpireInstrumentPatch
//		public static ExprEditor instrument() {
//			return new ExprEditor() {
//				@Override
//				public void edit(MethodCall m) throws CannotCompileException {
//					if (m.getMethodName().equals("goToTrueVictoryRoom"))
//						m.replace("{ if (" + GoToFifthActPatch.class.getName() +
//								".work(this)) { } else { $_ = $proceed($$); } }");
//				}
//			};
//		}

//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				return LineFinder.findInOrder(ctBehavior,
//						new Matcher.MethodCallMatcher(ProceedButton.class, "goToTrueVictoryRoom"));
//			}
//		}

//		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Void> Prefix(ProceedButton _inst) {
			if (canContinue()) {
//				System.out.println("opening screen");

				BaseMod.openCustomScreen(CurrentScreenEnum.PROCEED_CONFIRM_SCREEN,
						_inst == Fields.another.get(AbstractDungeon.overlayMenu));

				float current_x = ReflectionHacks.getPrivate(_inst, ProceedButton.class, "current_x");
				float target_x = ReflectionHacks.getPrivate(_inst, ProceedButton.class, "target_x");

				if (current_x != target_x) {
					current_x = MathUtils.lerp(current_x, target_x,
							Gdx.graphics.getDeltaTime() * 9.0F);

					if (Math.abs(current_x - target_x) < Settings.UI_SNAP_THRESHOLD)
						current_x = target_x;

					ReflectionHacks.setPrivate(_inst, ProceedButton.class,
							"current_x", current_x);
				}

				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = ContinueOntoHeartPatch.class, method = "Insert", paramtypez = {ProceedButton.class})
	public static class DisableActLikeItPatch { // 你干嘛
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(ProceedButton _inst) {
			if (canContinue()) {
				System.out.println("qwq");
				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}
}
