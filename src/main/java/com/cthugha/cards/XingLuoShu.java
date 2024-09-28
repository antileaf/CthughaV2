package com.cthugha.cards;

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

public class XingLuoShu extends AbstractCthughaCard {

    public static final String ID = ModHelper.MakePath(XingLuoShu.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/153.png";
    private static final int COST = 2;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public XingLuoShu() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.damage = this.baseDamage = 7;

//        this.tags.add(CustomTags.BaoYan);
        this.canBaoYan = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.upgradeDamage(3);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));

        this.addToBot(new ChannelAction(new YanZhiJing()));
        this.addToBot(new ChannelAction(new YanZhiJing()));

        XingLuoShu self = this;
        this.addToBot(new YanBaoAction(this, new AbstractGameAction() {
            public void update() {
                this.addToBot(new DamageAction(m, new DamageInfo(p, self.damage, self.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                this.addToBot(new DamageAction(m, new DamageInfo(p, self.damage, self.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                this.isDone = true;
            }
        }));

    }

}
