package com.cthugha.cards.cthugha;

import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.power.HeiYanPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HeiYanYiShi extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(HeiYanYiShi.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/132.png";

    private static final int COST = 2;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public HeiYanYiShi() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 21;
        this.shunRan = this.baseShunRan = 1;
        this.secondaryMagicNumber = this.baseSecondaryMagicNumber = 5;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters)
            if (!mo.isDeadOrEscaped())
                this.addToBot(new ApplyPowerAction(mo, p,
                        new HeiYanPower(mo, this.magicNumber)));
    }

    @Override
    public void onShunRan(int level) {
        if (level >= this.shunRan) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                if (!m.isDeadOrEscaped())
                    this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player,
                            new HeiYanPower(m, this.secondaryMagicNumber)));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(7);
            this.upgradeSecondaryMagicNumber(2);
            this.initializeDescription();
        }
    }
}
