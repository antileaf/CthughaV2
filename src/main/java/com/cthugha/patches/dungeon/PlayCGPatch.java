package com.cthugha.patches.dungeon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.dungeons.the_tricuspid_gate.TheTricuspidGate;
import com.cthugha.dungeons.the_tricuspid_gate.cg.CG;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.cutscenes.Cutscene;
import com.megacrit.cardcrawl.rooms.TrueVictoryRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;

@SuppressWarnings("unused")
public class PlayCGPatch {
	@SpirePatch(clz = TrueVictoryRoom.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<CG> cg = new SpireField<>(() -> null);
	}

	@SpirePatch(clz = TrueVictoryRoom.class, method = SpirePatch.CONSTRUCTOR)
	public static class ConstructorPatch {
		@SpirePostfixPatch
		public static void Postfix(TrueVictoryRoom _inst) {
			if (CardCrawlGame.dungeon instanceof TheTricuspidGate)
				Fields.cg.set(_inst, new CG());
		}
	}

	@SpirePatch(clz = TrueVictoryRoom.class, method = "update", paramtypez = {})
	public static class UpdatePatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(Cutscene.class, "update"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Void> Insert(TrueVictoryRoom _inst) {
			if (Fields.cg.get(_inst) != null) {
				Fields.cg.get(_inst).update();

				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = TrueVictoryRoom.class, method = "render",
			paramtypez = {SpriteBatch.class})
	public static class RenderPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(Cutscene.class, "render"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Void> Insert(TrueVictoryRoom _inst, SpriteBatch sb) {
			if (Fields.cg.get(_inst) != null) {
				Fields.cg.get(_inst).render(sb);

				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = TrueVictoryRoom.class, method = "renderAboveTopPanel",
			paramtypez = {SpriteBatch.class})
	public static class RenderAboveTopPanelPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(Cutscene.class, "renderAbove"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Void> Insert(TrueVictoryRoom _inst, SpriteBatch sb) {
			if (Fields.cg.get(_inst) != null) {
				Fields.cg.get(_inst).renderAbove(sb);

				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = TrueVictoryRoom.class, method = "dispose", paramtypez = {})
	public static class DisposePatch {
		@SpirePostfixPatch
		public static void Postfix(TrueVictoryRoom _inst) {
			if (Fields.cg.get(_inst) != null) {
				Fields.cg.get(_inst).dispose();
				Fields.cg.set(_inst, null);
			}
		}
	}
}
