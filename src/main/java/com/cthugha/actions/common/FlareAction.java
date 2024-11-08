package com.cthugha.actions.common;

import com.cthugha.Cthugha_Core;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.cards.FuZhuoShangHuan;
import com.cthugha.cards.ZhaoZhuoShangTianQue;
import com.cthugha.helpers.LanguageHelper;
import com.cthugha.helpers.ModHelper;
import com.cthugha.relics.LieSiTaShuJian;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FlareAction extends AbstractGameAction {
	private final AbstractCthughaCard card;
	private final ArrayList<AbstractCard> tempHand;
	private final ArrayList<AbstractCard> selected;

	public FlareAction(AbstractCthughaCard card) {
		this.card = card;
		this.tempHand = new ArrayList<>();
		this.selected = new ArrayList<>();

		this.source = AbstractDungeon.player;
		this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
		this.actionType = ActionType.EXHAUST;
	}

	@Override
	public void update() {
		if (this.duration == this.startDuration) {
			this.addToTop(new WaitAction(0F)); // 并非诗人

			this.tempHand.addAll(AbstractDungeon.player.hand.group.stream()
					.filter(c -> !ModHelper.isBurnCard(c))
					.collect(Collectors.toCollection(ArrayList::new)));

			if (this.tempHand.size() == AbstractDungeon.player.hand.size()) {
				this.tempHand.clear();
				this.flare();
				this.isDone = true;
				return;
			}

			AbstractDungeon.player.hand.group.removeAll(this.tempHand);

			AbstractDungeon.handCardSelectScreen.open(
					LanguageHelper.getShunRanString(this.card.availableShunRanLevels()),
					AbstractDungeon.player.hand.group.size(),
					true,
					true,
					false,
					false);

			this.tickDuration();
			return;
		}

		if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
			this.selected.addAll(AbstractDungeon.handCardSelectScreen.selectedCards.group);

			this.flare();
			this.returnCards();

			AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
			AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
			this.isDone = true;
		}

		this.tickDuration();
	}

	private void flare() {
		int level = this.selected.size();

		int fz = selected.stream()
				.filter(c -> c instanceof FuZhuoShangHuan)
				.mapToInt(c -> c.magicNumber)
				.max()
				.orElse(-1);

		if (fz != -1)
			level = fz;

		for (AbstractCard c : this.selected)
			if (c instanceof ZhaoZhuoShangTianQue) {
				c.costForTurn = 0;
				AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(c, false));
				this.addToBot(new ExhaustSpecificCardAction(this.card, AbstractDungeon.player.hand));

				return;
			}

		if (AbstractDungeon.player.hasRelic(LieSiTaShuJian.ID))
			level = Math.min(level, 7);

		for (AbstractCard c : this.selected) {
			AbstractDungeon.player.hand.moveToExhaustPile(c);
		}

		Cthugha_Core.logger.info("ShunRan level: {}", level);
		this.card.onShunRan(level);
	}

	private void returnCards() {
		AbstractDungeon.player.hand.group.addAll(this.tempHand);
		this.tempHand.clear();
		AbstractDungeon.player.hand.refreshHandLayout();
	}
}
