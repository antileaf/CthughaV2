package com.cthugha.cards.cthugha;

import com.cthugha.actions.common.BaoYanAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.orbs.FireVampire;
import com.cthugha.power.RevitalizingLifePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RevitalizingLife extends AbstractCthughaCard {
    public static final String ID = CthughaHelper.makeID(RevitalizingLife.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/焕生灵.png";

    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public RevitalizingLife() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 0;
        this.secondaryMagicNumber = this.baseSecondaryMagicNumber = 2;
        this.shunRan = this.baseShunRan = 1;

        this.exhaust = true;

        this.canBaoYan = true;
    }

    @Override
    public int getExtraYanZhiJing() {
        return this.magicNumber;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        this.magicNumber = this.baseMagicNumber;
        if (AbstractDungeon.player.hasPower(RevitalizingLifePower.POWER_ID))
            this.magicNumber += AbstractDungeon.player.getPower(RevitalizingLifePower.POWER_ID).amount;

        this.isMagicNumberModified = this.magicNumber != this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; i++)
            this.addToBot(new ChannelAction(new FireVampire()));

        this.addToBot(new ApplyPowerAction(p, p,
                new RevitalizingLifePower(p, this.secondaryMagicNumber)));

        this.addToBot(new BaoYanAction(this, new ApplyPowerAction(p, p,
                new RevitalizingLifePower(p, this.secondaryMagicNumber))));
    }

    @Override
    public void onFlare(int level) {
        if (level >= this.shunRan)
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new RevitalizingLifePower(AbstractDungeon.player, this.secondaryMagicNumber)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeShunRan(-1);
            this.initializeDescription();
        }
    }
}
