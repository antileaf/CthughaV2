// package com.example.cards;

// import com.example.actions.DrawSpecificCardAction;
// import com.example.enums.AbstractCardEnum;
// import com.example.enums.CustomTags;
// import com.example.helpers.ModHelper;
// import com.megacrit.cardcrawl.actions.AbstractGameAction;
// import com.megacrit.cardcrawl.actions.common.DamageAction;
// import com.megacrit.cardcrawl.actions.common.DrawCardAction;
// import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
// import com.megacrit.cardcrawl.cards.AbstractCard;
// import com.megacrit.cardcrawl.cards.DamageInfo;
// import com.megacrit.cardcrawl.cards.status.Burn;
// import com.megacrit.cardcrawl.characters.AbstractPlayer;
// import com.megacrit.cardcrawl.core.CardCrawlGame;
// import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
// import com.megacrit.cardcrawl.localization.CardStrings;
// import com.megacrit.cardcrawl.monsters.AbstractMonster;

// import basemod.abstracts.CustomCard;

// public class SiDaJiQieFenZhuoShangHuai extends CustomCard {

//     public static final String ID = ModHelper.MakePath(SiDaJiQieFenZhuoShangHuai.class.getSimpleName());
//     private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
//     private static final String NAME = cardStrings.NAME;
//     private static final String DESCRIPTION = cardStrings.DESCRIPTION;
//     private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
//     private static final String IMG_PATH = "cthughaResources/img/card/015.png";
//     private static final int COST = 0;
//     private static final CardType TYPE = CardType.ATTACK;
//     private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
//     private static final CardRarity RARITY = CardRarity.COMMON;
//     private static final CardTarget TARGET = CardTarget.ENEMY;

//     public SiDaJiQieFenZhuoShangHuai() {
//         super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

//         this.magicNumber = this.baseMagicNumber = 3;
//         this.damage = this.baseDamage = 2;
//         this.tags.add(AbstractCard.CardTags.STRIKE);
//         this.tags.add(CustomTags.Burn_Card);
//     }

//     @Override
//     public void upgrade() {
//         if (!this.upgraded) {
//             this.upgradeName();
//             this.rawDescription = UPGRADE_DESCRIPTION;
//             this.initializeDescription();

//             this.upgradeMagicNumber(1);
//         }
//     }

//     @Override
//     public void use(AbstractPlayer p, AbstractMonster m) {
//         for (int i = 0; i < this.magicNumber; i++) {
//             this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
//                     AbstractGameAction.AttackEffect.BLUNT_HEAVY));
//         }

//         this.addToBot(new DrawSpecificCardAction(card -> ModHelper.IsStrikeCard(card), 1));
//     }

// }
