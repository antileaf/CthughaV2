package com.cthugha.cards;

import com.cthugha.actions.ForEachYanZhiJingAction;
import com.cthugha.actions.YanBaoAction;
import com.cthugha.actions.common.BaoYanAction;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.enums.CustomTags;
import com.cthugha.helpers.ModHelper;
import com.cthugha.orbs.YanZhiJing;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.RemoveNextOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import basemod.abstracts.CustomCard;

public class XingYouGuangNi extends AbstractCthughaCard {

    public static final String ID = ModHelper.MakePath(XingYouGuangNi.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/127.png";

    private static final int COST = 0;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public XingYouGuangNi() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.block = this.baseBlock = 3;
        this.secondaryBlock = this.baseSecondaryBlock = 20;

//        this.tags.add(CustomTags.BaoYan);
        this.canBaoYan = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new BaoYanAction(this, new GainBlockAction(p, this.secondaryBlock)));

        this.addToBot(new ForEachYanZhiJingAction(count -> {
            for (int i = 0; i < count; i++)
                this.addToTop(new GainBlockAction(p, this.block));
        }, true));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeSecondaryBlock(10);
            this.upgradeBlock(1);
            this.initializeDescription();
        }
    }
}