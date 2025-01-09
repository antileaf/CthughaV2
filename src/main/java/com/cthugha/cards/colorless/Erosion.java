package com.cthugha.cards.colorless;

import basemod.helpers.CardModifierManager;
import com.cthugha.actions.common.DrawSpecificCardAction;
import com.cthugha.cardmodifier.ColorificStampModifier;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.CustomTags;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Erosion extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(Erosion.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/侵蚀.png";
    private static final int COST = 0;
    private static final CardType TYPE = CardType.STATUS;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Erosion() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 4;

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(p, new DamageInfo(p, this.magicNumber, DamageInfo.DamageType.THORNS),
                AbstractGameAction.AttackEffect.POISON));
    }

    @Override
    public void upgrade() {
    }

    public static Erosion create(boolean[] stats) {
        boolean[] statistics = ColorificStampModifier.statistics();
        for (int i = 0; i < statistics.length; i++)
            statistics[i] |= stats[i];

        Erosion card = new Erosion();

        CardModifierManager.addModifier(card, ColorificStampModifier.random(statistics));

        return card;
    }
}
