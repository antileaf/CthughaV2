package com.cthugha.cards.cthugha;

import com.cthugha.actions.common.BaoYanAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.orbs.FireVampire;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;

public class BaoLie extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(BaoLie.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/爆裂.png";

    private static final int COST = 0;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public BaoLie() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.damage = this.baseDamage = 20;
        this.shunRan = this.baseShunRan = 2;
        this.magicNumber = this.baseMagicNumber = 1;

        this.canBaoYan = true;
//        this.tags.add(CustomTags.BaoYan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p,
                new DrawCardNextTurnPower(p, this.magicNumber)));

        this.addToBot(new BaoYanAction(this, () -> {
            this.addToTop(new DamageAction(m, new DamageInfo(p, this.damage),
                    AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }));
    }

    @Override
    public void onFlare(int level) {
        if (level >= this.shunRan)
            this.addToBot(new ChannelAction(new FireVampire()));
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
