package com.cthugha.patches.dungeon;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.cardmodifier.ColorificStampModifier;
import com.cthugha.dungeons.the_tricuspid_gate.TheTricuspidGate;
import com.cthugha.dungeons.the_tricuspid_gate.confirm.ConfirmCheckbox;
import com.cthugha.dungeons.the_tricuspid_gate.confirm.ConfirmUseCardPopup;
import com.cthugha.enums.CurrentScreenEnum;
import com.cthugha.power.StampPower;
import com.cthugha.utils.ConfigHelper;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SuppressWarnings("unused")
public class ConfirmationPatch {
	@SpirePatch(clz = CardQueueItem.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<Integer> needConfirm = new SpireField<>(() -> -1); // -1 means no need to confirm
	}

	private static boolean shouldConfirm(AbstractCard card) {
		return CardModifierManager.getModifiers(card,
						ColorificStampModifier.ID).stream()
				.filter(m -> m instanceof ColorificStampModifier)
				.map(m -> ((ColorificStampModifier) m).color)
				.anyMatch(c -> AbstractDungeon.player.powers.stream()
						.anyMatch(p -> p instanceof StampPower &&
								((StampPower) p).color.equals(c)));
	}

	public static void mark(Object obj) {
		if (obj instanceof CardQueueItem) {
			CardQueueItem item = (CardQueueItem) obj;

			if (CardCrawlGame.dungeon instanceof TheTricuspidGate &&
					ConfigHelper.secondConfirm()) {
				Fields.needConfirm.set(item, 1);
			}
		}
	}

	@SpirePatch(clz = AbstractPlayer.class, method = "playCard", paramtypez = {})
	public static class PlayCardPatch {
		@SpireInstrumentPatch
		public static ExprEditor instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("add"))
						m.replace(" { $_ = $proceed($$); " +
								ConfirmationPatch.class.getName() + ".mark($1); }");
				}
			};
		}
	}

	@SpirePatch(clz = GameActionManager.class, method = "getNextAction", paramtypez = {})
	public static class GetNextActionPatch {
		@SpireInsertPatch(rloc = 49)
		public static SpireReturn<Void> Insert(GameActionManager _inst) {
			if (ConfigHelper.secondConfirm() &&
					Fields.needConfirm.get(_inst.cardQueue.get(0)) == 1 &&
					shouldConfirm(_inst.cardQueue.get(0).card)) {
				BaseMod.openCustomScreen(CurrentScreenEnum.USE_CARD_CONFIRM_SCREEN);
				return SpireReturn.Return(null);
			}

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = AbstractDungeon.class, method = "render")
	public static class RenderPatch {
		@SpireInsertPatch(rloc = 28)
		public static void insert(AbstractDungeon _inst, SpriteBatch sb) {
			if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
				ConfirmCheckbox.inst.render(sb);
			}
		}
	}
}
