package com.cthugha.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.cthugha.cards.colorless.BloodboonRitual;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class ReplacedVampires extends PhasedEvent {
	public static final String SIMPLE_NAME = ReplacedVampires.class.getSimpleName();
	public static final String ID = CthughaHelper.makeID(SIMPLE_NAME);
	public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

	private static final String INTRO = "INTRO";
	private static final String PUNCH = "PUNCH";
	private static final String PUNCH2 = "PUNCH2";
	private static final String LEAVE = "LEAVE";
	private static final String REFUSE = "REFUSE";

	public ReplacedVampires() {
		super(ID, eventStrings.NAME, "images/events/vampires.jpg");

		this.registerPhase(INTRO, new TextPhase(eventStrings.DESCRIPTIONS[0])
				.addOption(eventStrings.OPTIONS[0], i -> {
					AbstractCard[] burns = AbstractDungeon.player.masterDeck.group.stream()
									.filter(c -> c instanceof Burn)
									.toArray(AbstractCard[]::new);

					for (AbstractCard c : burns)
						AbstractDungeon.player.masterDeck.removeCard(c);

					this.transitionKey(PUNCH);
				})
				.addOption(eventStrings.OPTIONS[1], i -> this.transitionKey(REFUSE))
		);

		this.registerPhase(PUNCH, new TextPhase(eventStrings.DESCRIPTIONS[1])
				.addOption(eventStrings.OPTIONS[2], i -> this.transitionKey(PUNCH2))
		);

		this.registerPhase(PUNCH2, new TextPhase(eventStrings.DESCRIPTIONS[2])
				.addOption(new TextPhase.OptionInfo(eventStrings.OPTIONS[3], new BloodboonRitual()),
						i -> {
					for (int k = 0; k < 3; k++)
						AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(
								new BloodboonRitual(),
								Settings.WIDTH * (0.3F + 0.2F * k),
								Settings.HEIGHT / 2.0F));

					this.transitionKey(LEAVE);
						}));

		this.registerPhase(LEAVE, new TextPhase(eventStrings.DESCRIPTIONS[3])
				.addOption(eventStrings.OPTIONS[4], i -> this.openMap())
		);

		this.registerPhase(REFUSE, new TextPhase(eventStrings.DESCRIPTIONS[4])
				.addOption(eventStrings.OPTIONS[4], i -> this.openMap())
		);

		this.transitionKey(INTRO);
	}
}
