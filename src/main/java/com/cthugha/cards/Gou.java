package com.cthugha.cards;

import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class Gou extends AbstractCthughaCard {

    public static final String ID = ModHelper.MakePath(Gou.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/125.png";

    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Gou() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.shunRan = this.baseShunRan = 1;
        this.magicNumber = this.baseMagicNumber = 4;
        this.damage = this.baseDamage = 4;
        this.secondaryMagicNumber = this.baseSecondaryMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.secondaryMagicNumber; i++) {
            this.addToBot(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, this.damage),
                    AttackEffect.NONE));
        }
    }

    @Override
    public void onShunRan(int level) {
        if (level >= this.shunRan) {
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new VigorPower(AbstractDungeon.player, this.magicNumber)));
        }
        if (this.upgraded) {
            if (level >= this.secondaryShunRan) {
                this.addToBot(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, this.damage),
                        AbstractGameAction.AttackEffect.NONE));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.secondaryShunRan = this.baseSecondaryShunRan = 3;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}