package com.cthugha.patch.flare;

import com.cthugha.actions.common.FlareAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.flare.FlareCardQueueItem;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class FlareMechanicsPatch {
	private static final Logger logger = LogManager.getLogger(FlareMechanicsPatch.class.getName());

	@SpirePatch(clz = GameActionManager.class, method = "getNextAction", paramtypez = {})
	public static class GameActionManagerPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				int[] tmp = LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(GameActionManager.class, "usingCard"));
				return new int[]{tmp[0]};
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Void> Insert(GameActionManager _inst) {
			if (_inst.cardQueue.get(0) instanceof FlareCardQueueItem) {
				logger.info("Queue: {}", _inst.cardQueue.stream()
						.map(i -> i.card.name)
						.reduce((a, b) -> a + ", " + b));

				_inst.addToBottom(new FlareAction((AbstractCthughaCard) _inst.cardQueue.get(0).card));
				_inst.cardQueue.remove(0);

				return SpireReturn.Return(null);
			}

			return SpireReturn.Continue();
		}
	}
}
