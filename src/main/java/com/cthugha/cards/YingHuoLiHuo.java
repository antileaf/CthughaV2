package com.cthugha.cards;

import com.cthugha.actions.EachYanZhiJingWeakAction;
import com.cthugha.actions.YanBaoAction;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.enums.CustomTags;
import com.cthugha.helpers.ModHelper;
import com.cthugha.orbs.YanZhiJing;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

public class YingHuoLiHuo extends CustomCard {

    public static final String ID = ModHelper.MakePath(YingHuoLiHuo.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/093.png";
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public YingHuoLiHuo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.tags.add(CustomTags.Yan_Bao);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.upgraded) {
            this.addToBot(new ChannelAction(new YanZhiJing()));
        }

        this.addToBot(new EachYanZhiJingWeakAction(p, m, 1, false));

        this.addToBot(new YanBaoAction(this, new DamageAction(m, new DamageInfo(p, 20, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY)));

    }

}