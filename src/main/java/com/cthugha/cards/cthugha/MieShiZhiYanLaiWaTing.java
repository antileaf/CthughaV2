package com.cthugha.cards.cthugha;

import basemod.BaseMod;
import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.unique.ExpertiseAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MieShiZhiYanLaiWaTing extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(MieShiZhiYanLaiWaTing.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/107.png";
    private static final int COST = 2;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public MieShiZhiYanLaiWaTing() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.shunRan = this.baseShunRan = 3;
        this.damage = this.baseDamage = 16;
        this.secondaryShunRan = this.baseSecondaryShunRan = 9;
        this.secondaryDamage = this.baseSecondaryDamage = 132;
        this.isMultiSecondaryDamage = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(6);
            this.upgradeSecondaryDamage(26);
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AnonymousAction(() -> {
                int toAdd = BaseMod.MAX_HAND_SIZE - AbstractDungeon.player.hand.size();
                if (toAdd > 0)
                    this.addToTop(new MakeTempCardInHandAction(new Burn(), toAdd));
        }));
    }

    @Override
    public void onFlare(int level) {
        if (level >= this.shunRan) {
            this.addToBot(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, this.damage),
                    AttackEffect.BLUNT_HEAVY));
        }

        if (level >= this.secondaryShunRan) {
            this.calculateCardDamage(null);
            this.addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, this.multiSecondaryDamage,
                    DamageType.NORMAL, AttackEffect.BLUNT_HEAVY));

            this.addToBot(new ExpertiseAction(AbstractDungeon.player, BaseMod.MAX_HAND_SIZE));
        }
    }
}
