package com.cthugha.cards.cthugha;

import com.cthugha.actions.common.BaoYanAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.orbs.FireVampire;
import com.cthugha.power.LoseYanZhiJingPower;
import com.cthugha.power.XingYunPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.RemoveNextOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class XingYun extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(XingYun.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/073.png";
    private static final int COST = 0;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public XingYun() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 1;
        this.secondaryMagicNumber = this.baseSecondaryMagicNumber = 2;

//        this.tags.add(CustomTags.BaoYan);
        this.canBaoYan = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new XingYunPower(p, 4), 4));

        this.addToBot(new BaoYanAction(this, () -> {
            for (int i = 0; i < this.secondaryMagicNumber; i++)
                this.addToTop(new RemoveNextOrbAction());
        }));

        for (int i = 0; i < this.magicNumber; i++)
            this.addToBot(new ChannelAction(new FireVampire()));

        this.addToBot(new ApplyPowerAction(p, p, new LoseYanZhiJingPower(p, this.magicNumber), this.magicNumber));
//
//        ArrayList<AbstractOrb> orbs = new ArrayList<>();
//        for (int i = 0; i < this.magicNumber; i++) {
//            YanZhiJing orb = new YanZhiJing();
//            this.addToBot(new ChannelAction(orb));
//        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.initializeDescription();
        }
    }
}
