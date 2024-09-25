package com.cthugha.patch;

// public class CardHelperPatch {
//     @SpirePatch(clz = CardHelper.class, method = "obtain")
//     public static class FastCardObtainEffectaa {
//         @SpirePostfixPatch
//         public static void Postfix(String key, AbstractCard.CardRarity rarity, AbstractCard.CardColor color) {
//             // if (card.color == AbstractCard.CardColor.CURSE &&
//             // AbstractDungeon.player.hasRelic("Omamori") &&
//             // (AbstractDungeon.player.getRelic("Omamori")).counter != 0) {
//             // ((Omamori) AbstractDungeon.player.getRelic("Omamori")).use();
//             // this.duration = 0.0F;
//             // this.isDone = true;
//             // }

//             AbstractCard  card = CardLibrary.getCard(key);
//             // if (card.cardID == "Burn") {
//                 System.out.println("==========------------===========");
//                 if (card != null) {

//                     System.out.println(key);
//                     System.out.println(card.upgraded);
//                 } else {
//                     System.out.println("card null");
//                 }
//             // AbstractCard card = AbstractDungeon.player.masterDeck.findCardById(YiChiRuXi.ID);
//             // System.out.println(card.cardID);
//             // System.out.println(card.magicNumber);
//             // if (key == YiChiRuXi.ID) {
//             //     for (int i = 0; i < card.magicNumber; i++) {
//             //         AbstractDungeon.player.masterDeck.removeCard("Burn");
//             //     }
//             // }
//         }
//     }
// }

