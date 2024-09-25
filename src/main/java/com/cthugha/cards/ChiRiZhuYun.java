package com.cthugha.cards;

import com.cthugha.actions.BreakBlockDamageAction;
import com.cthugha.actions.DecreaseMonsterMaxHealthAction;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

public class ChiRiZhuYun extends CustomCard {

    public static final String ID = ModHelper.MakePath(ChiRiZhuYun.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/敕日烛云.png";
    private static final int COST = 2;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public ChiRiZhuYun() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.damage = this.baseDamage = 5;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.upgradeDamage(2);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = 0;
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (ModHelper.IsBurnCard(card)) {
                count++;
            }
        }

        for (int i = 0; i < 3 + count; i++) {
            this.addToBot(new BreakBlockDamageAction(m, new DamageInfo(p, this.damage), lastDamageTaken -> {
                // m.maxHealth = m.currentHealth;
                // m.healthBarUpdatedEvent();
                // int delta = m.maxHealth - m.currentHealth;
                AbstractGameAction action = new DecreaseMonsterMaxHealthAction(m, -1);
                this.addToBot(action);
            }));
        }
    }

}
