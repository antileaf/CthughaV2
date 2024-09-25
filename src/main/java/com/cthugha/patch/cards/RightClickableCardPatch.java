package com.cthugha.patch.cards;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.cthugha.Cthugha_Core;
import com.cthugha.cards.interfaces.RightClickableCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

@SuppressWarnings("unused")
public class RightClickableCardPatch {
	public static class State {
		public boolean rightClickStarted = false;
		public boolean rightClick = false;
		public boolean doubleClick = false;
		public long lastClick = 0;
	}

	@SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<State> state = new SpireField<>(State::new);
	}

	@SpirePatch(clz = AbstractCard.class, method = "update")
	public static class UpdatePatch {
		public static void Postfix(AbstractCard _inst) {
			if (AbstractDungeon.player == null || !(_inst instanceof RightClickableCard))
				return;

			if (AbstractDungeon.player.isDraggingCard) {
//				Cthugha_Core.logger.info("Dragging card, return");
				return;
			}

			RightClickableCard clickable = (RightClickableCard) _inst;

			State state = Fields.state.get(_inst);
			if (state.rightClickStarted && InputHelper.justReleasedClickRight) {
				if (clickable.clickablePositions() == RightClickableCard.Position.HAND) {
					if (_inst.isHoveredInHand(0.9F)) // TODO: 如果不合适，改回 1.0F
						state.rightClick = true;
				}
				else if (clickable.clickablePositions() == RightClickableCard.Position.ALL_PILES) {
					if (_inst.hb.hovered)
						state.rightClick = true;
				}

				state.rightClickStarted = false;
			}

			if (AbstractDungeon.player.hand.group.contains(_inst) &&
					_inst.hb != null &&
					_inst.hb.hovered &&
					InputHelper.justClickedRight)
				state.rightClickStarted = true;

			long now = System.currentTimeMillis();
			if (now - state.lastClick >= 300L && state.doubleClick) { // 三击应当按照双击之后的下一次单击处理
				state.doubleClick = false;
				clickable.onRightClick();
			}

			if (state.rightClick) {
				state.rightClick = false;
				state.doubleClick = true;

				if (now - state.lastClick < 300L) // 双击
					clickable.onDoubleRightClick();

				state.lastClick = now;
			}
		}
	}
}
