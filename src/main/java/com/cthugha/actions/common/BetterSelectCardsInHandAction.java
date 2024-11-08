package com.cthugha.actions.common;

import com.cthugha.Cthugha_Core;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BetterSelectCardsInHandAction extends AbstractGameAction {
	private final Predicate<AbstractCard> predicate;
	private final Consumer<List<AbstractCard>> callback;
	private final String text;
	private final boolean anyNumber;
	private final boolean canPickZero;
	private final ArrayList<AbstractCard> hand;
	private final ArrayList<AbstractCard> tempHand;

	private final boolean returnToHand;

	public BetterSelectCardsInHandAction(int amount, String textForSelect, boolean anyNumber, boolean canPickZero, Predicate<AbstractCard> cardFilter, Consumer<List<AbstractCard>> callback, boolean returnToHand) {
		this.tempHand = new ArrayList<>();
		this.amount = amount;
		this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
		this.text = textForSelect;
		this.anyNumber = anyNumber;
		this.canPickZero = canPickZero;
		this.predicate = cardFilter;
		this.callback = callback;
		this.hand = AbstractDungeon.player.hand.group;
		this.returnToHand = returnToHand;
	}
	
	@Override
	public void update() {
		if (this.duration == this.startDuration) {
			if (this.callback == null) {
				this.isDone = true;
			} else {
				this.hand.removeIf((c) -> {
					return !this.predicate.test(c) && this.tempHand.add(c);
				});
				if (this.hand.isEmpty()) {
					this.hand.addAll(this.tempHand);
					this.finish();
				} else if (this.hand.size() <= this.amount && !this.anyNumber && !this.canPickZero) {
					ArrayList<AbstractCard> spoof = new ArrayList<>(this.hand);
					this.hand.clear();
					this.hand.addAll(this.tempHand);
					this.callback.accept(spoof);
					if (this.returnToHand)
						this.hand.addAll(spoof);
					this.finish();
				} else {
					AbstractDungeon.handCardSelectScreen.open(this.text, this.amount, this.anyNumber, this.canPickZero);
					this.tickDuration();
				}
			}
		} else if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
			this.hand.addAll(this.tempHand);
			this.callback.accept(AbstractDungeon.handCardSelectScreen.selectedCards.group);
			if (this.returnToHand)
				this.hand.addAll(AbstractDungeon.handCardSelectScreen.selectedCards.group);
			AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
			AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
			this.finish();
		} else {
			this.tickDuration();
		}
	}
	
	private void finish() {
//		Cthugha_Core.logger.info("finish, hand: {}",
//				String.join(", ", this.hand.stream().map(c -> c.name).toArray(String[]::new)));

		AbstractDungeon.player.hand.refreshHandLayout();
		AbstractDungeon.player.hand.applyPowers();
		this.isDone = true;
	}
}
