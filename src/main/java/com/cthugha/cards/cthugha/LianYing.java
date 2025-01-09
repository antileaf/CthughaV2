package com.cthugha.cards.cthugha;

import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.power.LianYingPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LianYing extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(LianYing.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/连营.png";
    private static final int COST = 0;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public LianYing(boolean doNotSetPreview) {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.damage = this.baseDamage = 5;
        this.magicNumber = this.baseMagicNumber = 4;
        this.shunRan = this.baseShunRan = 2;

        if (!doNotSetPreview)
            this.cardsToPreview = new LianYing(true);
    }

    public LianYing() {
        this(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage)));
        this.addToBot(new ApplyPowerAction(p, p, new LianYingPower(this.magicNumber), this.magicNumber));
    }

    @Override
    public void onShunRan(int level) {
        if (level >= this.shunRan) {
            LianYing card = new LianYing();
            if (this.upgraded)
                card.upgrade();

            this.addToBot(new MakeTempCardInHandAction(card));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
            this.rawDescription = UPGRADE_DESCRIPTION;
            if (this.cardsToPreview != null)
                this.cardsToPreview.upgrade();
            this.initializeDescription();
        }
    }
}
