package com.cthugha.patches.misc.signature;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomUnlock;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.cards.cthugha.*;
import com.cthugha.characters.Cthugha;
import com.cthugha.dungeons.the_tricuspid_gate.TheTricuspidGate;
import com.cthugha.utils.CthughaHelper;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.neow.NeowUnlockScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import me.antileaf.signature.utils.SignatureHelper;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unused")
public class UnlockSignaturePatch {
	private static ArrayList<?> patched = null;

	@SpirePatch(clz = GameOverScreen.class, method = "calculateUnlockProgress", paramtypez = {})
	public static class CalculateUnlockProgressPatch {
		@SpirePostfixPatch
		public static void Postfix(GameOverScreen _inst) {
			if (AbstractDungeon.player instanceof Cthugha && _inst instanceof VictoryScreen &&
					CardCrawlGame.dungeon instanceof TheTricuspidGate &&
					ReflectionHacks.getPrivate(_inst, GameOverScreen.class, "unlockBundle") == null &&
					(!SignatureHelper.isUnlocked(Burn.ID) || !SignatureHelper.isUnlocked(ShiftingStar.ID))) {
				SignatureHelper.unlock(Burn.ID, true);
				SignatureHelper.enable(Burn.ID, true);

				SignatureHelper.unlock(ShiftingStar.ID, true);
				SignatureHelper.enable(ShiftingStar.ID, true);

				ArrayList<AbstractUnlock> unlocks = new ArrayList<>();
				for (int i = 0; i < 3; i++) {
					AbstractUnlock unlock = new CustomUnlock(i < 2 ? Burn.ID : ShiftingStar.ID);
					unlock.card = unlock.card.makeCopy();

					if (i == 0)
						CthughaHelper.changeBurn(unlock.card);

					unlocks.add(unlock);
				}

				patched = unlocks;
			}
			else if (AbstractDungeon.player instanceof Cthugha && _inst instanceof VictoryScreen &&
					CardCrawlGame.dungeon instanceof TheTricuspidGate && AbstractDungeon.ascensionLevel >= 20 &&
					(!SignatureHelper.isUnlocked(DaoHuoShiYan.ID) || !SignatureHelper.isUnlocked(HeLuSiZhiYan.ID) ||
							!SignatureHelper.isUnlocked(YingHuoLiHuo.ID))) {
				SignatureHelper.unlock(DaoHuoShiYan.ID, true);
				SignatureHelper.enable(DaoHuoShiYan.ID, true);

				SignatureHelper.unlock(HeLuSiZhiYan.ID, true);
				SignatureHelper.enable(HeLuSiZhiYan.ID, true);

				SignatureHelper.unlock(YingHuoLiHuo.ID, true);
				SignatureHelper.enable(YingHuoLiHuo.ID, true);

				ArrayList<AbstractUnlock> unlocks = new ArrayList<>();
				for (String id : new String[] {DaoHuoShiYan.ID, HeLuSiZhiYan.ID, YingHuoLiHuo.ID}) {
					AbstractUnlock unlock = new CustomUnlock(id);
					unlock.card = unlock.card.makeCopy();
					unlocks.add(unlock);
				}

				patched = unlocks;
			}
			else
				patched = null;

			if (Arrays.stream(AbstractPlayer.PlayerClass.values())
					.anyMatch(c -> AbstractDungeon.player.chosenClass == c) &&
					_inst instanceof VictoryScreen && CardCrawlGame.dungeon instanceof TheTricuspidGate &&
					AbstractDungeon.ascensionLevel >= 20 && !SignatureHelper.isUnlocked(ChiYanHuaZhan.ID)) {
				SignatureHelper.unlock(ChiYanHuaZhan.ID, true);
				BaseMod.logger.info("Unlocking ChiYanHuaZhan");
			}

			if (patched != null) {
				ReflectionHacks.setPrivate(_inst, GameOverScreen.class, "unlockBundle",
						patched);
			}
		}
	}

	private static UIStrings uiStrings = null;

	private static UIStrings getUIStrings() {
		if (uiStrings == null)
			uiStrings = CardCrawlGame.languagePack.getUIString("Cthugha:UnlockSignature");
		return uiStrings;
	}

	public static boolean handle(NeowUnlockScreen screen) {
		if (patched == screen.unlockBundle) {
			AbstractDungeon.dynamicBanner.appear(getUIStrings().TEXT[0]);
			return true;
		}

		return false;
	}

	@SpirePatch(clz = NeowUnlockScreen.class, method = "open")
	public static class UnlockScreenOpenPatch {
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("appearInstantly"))
						m.replace(" { if (" + UnlockSignaturePatch.class.getName() +
								".handle(this)) { } else { $_ = $proceed($$); } }");
				}
			};
		}
	}

	@SpirePatch(clz = NeowUnlockScreen.class, method = "reOpen")
	public static class UnlockScreenReOpenPatch {
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return UnlockScreenOpenPatch.Instrument();
		}
	}

	@SpirePatch(clz = NeowUnlockScreen.class, method = "render", paramtypez = {SpriteBatch.class})
	public static class UnlockScreenRenderPatch {
		@SpireInsertPatch(rloc = 36)
		public static SpireReturn<Void> Insert(NeowUnlockScreen _inst, SpriteBatch sb) {
			if (patched == _inst.unlockBundle) {
				FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont,
						getUIStrings().TEXT[1],
						(float) Settings.WIDTH / 2.0F,
						(float)Settings.HEIGHT / 2.0F - 330.0F * Settings.scale,
						Settings.CREAM_COLOR);

				_inst.button.render(sb);

				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}
}
