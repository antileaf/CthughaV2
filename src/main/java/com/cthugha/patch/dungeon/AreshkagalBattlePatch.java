package com.cthugha.patch.dungeon;

import basemod.ReflectionHacks;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.cardmodifier.ColorificStampModifier;
import com.cthugha.dungeons.the_tricuspid_gate.monsters.Areshkagal;
import com.cthugha.dungeons.the_tricuspid_gate.monsters.BurialWatchdogScepter;
import com.cthugha.dungeons.the_tricuspid_gate.monsters.BurialWatchdogSword;
import com.cthugha.power.StampPower;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.BlueCandle;
import com.megacrit.cardcrawl.relics.MedicalKit;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class AreshkagalBattlePatch {
	@SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfCombatPreDrawLogic", paramtypez = {})
	public static class StartOfCombatPatch {
		@SpirePostfixPatch
		public static void Prefix(AbstractPlayer _inst) {
			if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss &&
					AbstractDungeon.lastCombatMetricKey.equals(Areshkagal.ID)) {
				ArrayList<AbstractCard> choices = _inst.drawPile.group.stream()
						.filter(c -> {
							if (c.type == AbstractCard.CardType.STATUS && c.costForTurn < -1 &&
									!_inst.hasRelic(MedicalKit.ID))
								return false;
							else if (c.type == AbstractCard.CardType.CURSE && c.costForTurn < -1 &&
									!_inst.hasRelic(BlueCandle.ID))
								return false;
							else
								return true;
						})
						.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

				for (int i = 0; i < 8; i++) {
					if (choices.isEmpty())
						break;

					AbstractCard card = choices.remove(AbstractDungeon.cardRandomRng.random(choices.size() - 1));
					CardModifierManager.addModifier(card, new ColorificStampModifier(
							ColorificStampModifier.ColorEnum.values()[i % 4]));
				}
			}
		}
	}

	@SpirePatch(clz = AbstractPlayer.class, method = "playCard", paramtypez = {})
	public static class SurroundedPatch {
		private static boolean backup;

		@SpirePrefixPatch
		public static void Prefix(AbstractPlayer _inst) {
			backup = _inst.flipHorizontal;
		}

		@SpirePostfixPatch
		public static void Postfix(AbstractPlayer _inst) {
			AbstractMonster m = ReflectionHacks.getPrivate(_inst, AbstractPlayer.class, "hoveredMonster");

			if (m instanceof Areshkagal)
				_inst.flipHorizontal = backup;
		}
	}

	@SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
	public static class MovePositionPatch {
		public static boolean check() {
			return (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss &&
					AbstractDungeon.lastCombatMetricKey.equals(Areshkagal.ID));
		}

		public static void move() {
			AbstractDungeon.player.movePosition(Settings.WIDTH / 2.0F,
					AbstractDungeon.floorY - 10.0F * Settings.yScale);
		}

		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("movePosition"))
						m.replace("{ if (" + MovePositionPatch.class.getName() + ".check()) " +
								"{" + AreshkagalBattlePatch.class.getName() +
								".MovePositionPatch.move(); } else { $proceed($$); } }");
				}
			};
		}
	}

	@SpirePatch(clz = MonsterGroup.class, method = "queueMonsters", paramtypez = {})
	public static class QueueMonstersPatch {
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("add")) {
						m.replace("{ if (((m instanceof " + Areshkagal.class.getName() +
								") || (m instanceof " + BurialWatchdogSword.class.getName() +
								") || (m instanceof " + BurialWatchdogScepter.class.getName() +
								")) && m.maxHealth <= 1) { } else { $_ = $proceed($$); } }");
					}
				}
			};
		}
	}

	@SpirePatch(clz = AbstractPlayer.class, method = "render", paramtypez = {SpriteBatch.class})
	public static class RenderPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractPlayer _inst, SpriteBatch sb) {
			if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss &&
					AbstractDungeon.lastCombatMetricKey.equals(Areshkagal.ID))
				StampPower.render(sb);
		}
	}
}
