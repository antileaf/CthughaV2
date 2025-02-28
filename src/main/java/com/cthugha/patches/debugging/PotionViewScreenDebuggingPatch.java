package com.cthugha.patches.debugging;

@SuppressWarnings("unused")
public class PotionViewScreenDebuggingPatch {
//	@SpirePatch(clz = PotionViewScreen.class, method = "renderList")
//	public static class RenderListPatch {
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				int[] tmp = LineFinder.findAllInOrder(ctBehavior,
//						new Matcher.MethodCallMatcher(AbstractPotion.class, "labRender"));
//				return new int[]{tmp[tmp.length - 1]};
//			}
//		}
//
//		@SpireInsertPatch(locator = Locator.class, localvars = {"r"})
//		public static void Insert(PotionViewScreen _inst, AbstractPotion r) {
//			Cthugha_Core.logger.info("r.labOutlineColor: {}",
//					(Color) ReflectionHacks.getPrivate(r, AbstractPotion.class, "labOutlineColor"));
//		}
//	}
}
