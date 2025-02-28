package com.cthugha.patches.dungeon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.characters.Cthugha;
import com.cthugha.dungeons.the_tricuspid_gate.TheTricuspidGate;
import com.cthugha.utils.CthughaHelper;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@Deprecated
@SuppressWarnings("unused")
public class SmallerPlayerPatch {
	@SpirePatch(clz = AbstractPlayer.class, method = "render", paramtypez = {SpriteBatch.class})
	public static class RenderPlayerImagePatch {
		public static boolean check() {
			return AbstractDungeon.player instanceof Cthugha &&
					CthughaHelper.isInBattle() &&
					CardCrawlGame.dungeon instanceof TheTricuspidGate;
		}

		public static void drawSmaller(SpriteBatch sb, Texture img, float x, float y,
								float width, float height, int srcX, int srcY,
								int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
			sb.draw(img, x + AbstractDungeon.player.img.getWidth() * Settings.scale / 2.0F * 0.2F,
					y, width / 2.0F, 0.0F, width * 0.8F, height * 0.8F,
					0.8F, 0.8F, 0.0F,
					0, 0, srcWidth, srcHeight, flipX, flipY);
		}

		public static void originalDraw(SpriteBatch sb, Texture img, float x, float y,
										float width, float height, int srcX, int srcY,
										int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
			sb.draw(img, x, y, width, height, srcX, srcY, srcWidth, srcHeight, flipX, flipY);
		}

//		@SpireInstrumentPatch
//		public static ExprEditor Instrument() {
//			return new ExprEditor() {
//				@Override
//				public void edit(MethodCall m) throws CannotCompileException {
//					if (m.getClassName().equals(SpriteBatch.class.getName()) &&
//							m.getMethodName().equals("draw"))
//						m.replace("{ if (" + RenderPlayerImagePatch.class.getName() +
//								".check()) { $_ = " +
//								RenderPlayerImagePatch.class.getName() +
//								".drawSmaller($0, $$); } else { $_ = $proceed($$); } }");
//				}
//			};
//		}
//
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				return LineFinder.findInOrder(ctBehavior,
//						new Matcher.MethodCallMatcher(SpriteBatch.class, "draw"));
//			}
//		}
//
//		@SpireInsertPatch(locator = Locator.class)
//		public static SpireReturn<Void> Insert(AbstractPlayer _inst, SpriteBatch sb) {
//			if (check()) {
//				drawSmaller(sb, _inst.img,
//						_inst.drawX - _inst.img.getWidth() * Settings.scale / 2.0F + _inst.animX,
//						_inst.drawY,
//						_inst.img.getWidth() * Settings.scale,
//						_inst.img.getHeight() * Settings.scale,
//						0, 0,
//						_inst.img.getWidth(), _inst.img.getHeight(),
//						_inst.flipHorizontal, _inst.flipVertical);
//
//				_inst.hb.render(sb);
//				_inst.healthHb.render(sb);
//
//				return SpireReturn.Return(null);
//			}
//			else
//				return SpireReturn.Continue();
//		}
	}
}
