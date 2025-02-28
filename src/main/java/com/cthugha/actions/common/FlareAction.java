package com.cthugha.actions.common;

import basemod.ReflectionHacks;
import com.cthugha.Cthugha_Core;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.cards.cthugha.CastleOfTheSun;
import com.cthugha.cards.cthugha.FuZhuoShangHuan;
import com.cthugha.cards.cthugha.ZhaoZhuoShangTianQue;
import com.cthugha.utils.LanguageHelper;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;

import java.util.ArrayList;
import java.util.Comparator;
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
					.filter(c -> !CthughaHelper.isBurnCard(c))
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

			if (!(this.card instanceof CastleOfTheSun)) {
				ArrayList<AbstractCard> defaultSelection = this.defaultSelection();
				AbstractDungeon.handCardSelectScreen.selectedCards.group.addAll(defaultSelection);
				defaultSelection.forEach(c -> {
					AbstractDungeon.player.hand.group.remove(c);
					c.setAngle(0.0F, false);
				});

				ReflectionHacks.privateMethod(HandCardSelectScreen.class, "refreshSelectedCards")
						.invoke(AbstractDungeon.handCardSelectScreen);
				AbstractDungeon.player.hand.refreshHandLayout();
			}

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
				.filter(c -> c instanceof AbstractCthughaCard)
				.mapToInt(c -> ((AbstractCthughaCard) c).modifyFlareLevel())
				.max()
				.orElse(-1);

		if (fz != -1)
			level = fz;

		for (AbstractCard c : this.selected)
			if (c instanceof AbstractCthughaCard) {
				AbstractCthughaCard ac = (AbstractCthughaCard) c;
				if (ac.onFlareSelectedBy(this.card)) {
					AbstractDungeon.player.hand.group.addAll(this.selected);
					this.selected.clear();
					return;
				}
			}

		for (AbstractCard c : this.selected)
			AbstractDungeon.player.hand.moveToExhaustPile(c);

		Cthugha_Core.logger.info("ShunRan level: {}", level);
		this.card.onFlare(level);
	}

	private void returnCards() {
		AbstractDungeon.player.hand.group.addAll(this.tempHand);
		this.tempHand.clear();
		AbstractDungeon.player.hand.refreshHandLayout();
	}

	private ArrayList<AbstractCard> get(int count) {
		return AbstractDungeon.player.hand.group.stream()
				.filter(CthughaHelper::isBurnCard)
				.filter(c -> !(c instanceof AbstractCthughaCard) ||
						(((AbstractCthughaCard) c).modifyFlareLevel() == -1) &&
								!((AbstractCthughaCard) c).onFlareSelectedBy(this.card)).sorted((a, b) -> {
					if (a instanceof Burn && b instanceof Burn)
						return ((Burn) b).baseMagicNumber - ((Burn) a).baseMagicNumber;
					if (a instanceof Burn)
						return -1;
					if (b instanceof Burn)
						return 1;

					return a.baseDamage - b.baseDamage;
				})
				.limit(count)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	private ArrayList<AbstractCard> checkLevel(int count) {
		int burnCardCount = (int) AbstractDungeon.player.hand.group.stream()
				.filter(CthughaHelper::isBurnCard)
				.filter(c -> !(c instanceof AbstractCthughaCard) ||
						(((AbstractCthughaCard) c).modifyFlareLevel() == -1) &&
								!((AbstractCthughaCard) c).onFlareSelectedBy(this.card))
				.count();

		if (burnCardCount >= count)
			return this.get(count);

		if (count <= 6 && AbstractDungeon.player.hand.group.stream()
				.anyMatch(c -> c instanceof FuZhuoShangHuan))
			return AbstractDungeon.player.hand.group.stream()
					.filter(c -> c instanceof FuZhuoShangHuan)
					.limit(1)
					.collect(Collectors.toCollection(ArrayList::new));

		return null;
	}

	private ArrayList<AbstractCard> defaultSelection() {
		if (this.card.secondaryShunRan != -1) {
			ArrayList<AbstractCard> tmp = this.checkLevel(this.card.secondaryShunRan);
			if (tmp != null)
				return tmp;
		}

		if (this.card.shunRan != -1) {
			ArrayList<AbstractCard> tmp = this.checkLevel(this.card.shunRan);
			if (tmp != null)
				return tmp;
		}

		return new ArrayList<>();
	}
}
