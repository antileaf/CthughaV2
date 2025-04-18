package com.cthugha.patches.cards;

import com.cthugha.cards.AbstractCthughaCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.cthugha.cards.interfaces.RightClickableCard;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputAction;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.ExhaustPileViewScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unused")
public class RightClickableCardPatch {
	private static final int RS_CODE = 9;

	@SpirePatch(clz = CInputActionSet.class, method = SpirePatch.CLASS)
	public static class Controller {
		public static StaticSpireField<CInputAction> RS =
				new StaticSpireField<>(() -> new CInputAction(RS_CODE));
	}

	@SpirePatch(clz = CInputActionSet.class, method = "load", paramtypez = {})
	public static class CInputActionSetPatch {
		@SpirePostfixPatch
		public static void Postfix() {
//			CInputHelper.actions.add(Controller.LS.get());
			CInputHelper.actions.add(Controller.RS.get());
		}
	}

	private static boolean controllerRightClick() {
		return Settings.isControllerMode && Controller.RS.get().isJustPressed();
	}

	public static class State {
		public boolean rightClickStarted = false;
		public boolean rightClick = false;
	}

	@SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<State> state = new SpireField<>(State::new);
		public static SpireField<AbstractCard> copiedFrom = new SpireField<>(() -> null);
	}

	@SpirePatch(clz = ExhaustPileViewScreen.class, method = "open", paramtypez = {})
	public static class ExhaustPileViewScreenPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(CardGroup.class, "addToBottom"));
			}
		}

		@SpireInsertPatch(locator = Locator.class, localvars = {"c", "toAdd"})
		public static void Insert(ExhaustPileViewScreen _inst, AbstractCard c, AbstractCard toAdd) {
			Fields.copiedFrom.set(toAdd, c);
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "update")
	public static class UpdatePatch {
		public static void Postfix(AbstractCard _inst) {
			if (AbstractDungeon.player == null || !(_inst instanceof RightClickableCard))
				return;

			if (!Settings.isControllerMode && AbstractDungeon.player.isDraggingCard) {
//				Cthugha_Core.logger.info("Dragging card, return");
				return;
			}

			RightClickableCard clickable = (RightClickableCard) _inst;

			State state = Fields.state.get(_inst);
			if (state.rightClickStarted && (Settings.isControllerMode || InputHelper.justReleasedClickRight)) {
				ArrayList<RightClickableCard.Position> positions =
						new ArrayList<>(Arrays.asList(clickable.clickablePositions()));

				if (positions.contains(RightClickableCard.Position.HAND)) {
					if (AbstractDungeon.player.hoveredCard == _inst &&
							(Settings.isControllerMode || _inst.isHoveredInHand(1.0F)))
						state.rightClick = true;
				}

				if ((positions.contains(RightClickableCard.Position.DRAW_PILE) &&
						AbstractDungeon.player.drawPile.group.contains(_inst)) ||
						(positions.contains(RightClickableCard.Position.DISCARD_PILE) &&
								AbstractDungeon.player.discardPile.group.contains(_inst)) ||
						(positions.contains(RightClickableCard.Position.EXHAUST_PILE) &&
								AbstractDungeon.player.exhaustPile.group.contains(Fields.copiedFrom.get(_inst)))) {
//					if (_inst instanceof SleeplessMoon)
//						Cthugha_Core.logger.info("Sleepless Moon in {}",
//								AbstractDungeon.player.drawPile.group.contains(_inst) ? "draw pile" :
//										AbstractDungeon.player.discardPile.group.contains(_inst) ? "discard pile" :
//												"exhaust pile");

					if (_inst.hb.hovered)
						state.rightClick = true;

//					if (state.rightClick)
//						Cthugha_Core.logger.info("Right click on {} in {}", _inst.name, positions);
				}

				state.rightClickStarted = false;
			}

			if (_inst.hb != null && _inst.hb.hovered &&
					(InputHelper.justClickedRight || controllerRightClick()))
				state.rightClickStarted = true;

			if (Fields.copiedFrom.get(clickable) != null)
				clickable = (RightClickableCard) Fields.copiedFrom.get(clickable);

			if (state.rightClick) {
				if (_inst instanceof AbstractCthughaCard && _inst != clickable) {
					((AbstractCthughaCard) _inst).triggeredShunRanThisTurn = true;
					((AbstractCthughaCard) _inst).updateBgImg();
				}

				clickable.onRightClick();
				state.rightClick = false;
			}
		}
	}
}
