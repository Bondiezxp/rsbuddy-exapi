package com.rsbuddy.script.methods;

import com.rsbuddy.script.wrappers.Player;

public class ExPrayer {

	public enum Equipment {
		SPIRIT_SHIELD(13734, 1),
		OTHER_SPIRIT_SHIELDS(new int[] { 1373, 13744, 13738, 13742, 13740 }, 3),
		FALADOR_SHIELD_1(14577, 3),
		FALADOR_SHIELD_2(14578, 5),
		FALADOR_SHIELD_3_4(new int[] { 14579, 19749 }, 7),
		BROODOO_SHIELD(new int[] { 6279, 6277, 6275, 6273, 6271, 6269, 6267,
				6265, 6263, 6261, 6259, 6257, 6255, 6253, 6251, 6249, 6247,
				6245, 6243, 6241, 6239, 6237, 6235, 6233, 6231, 6229, 6227,
				6225, 6223, 6221, 6219, 6217, 6215 }, 5),
		GOD_BOOKS(new int[] { 3839, 3840, 3841, 3842, 3843, 3844, 19612, 19613,
				19614, 19615, 19615, 19617 }, 5),
		FAITHFUL_SHIELD(18747, 6),
		WHITE_SQ_SHIELD(6631, 1),
		WHITE_KITE_SHIELD(6633, 1),
		AMULET_OF_POWER(1731, 1),
		AMULET_OF_GLORY(new int[] { 1704, 1706, 1708, 1710, 1712, 10362, 10360,
				10358, 10356, 10354 }, 3),
		SALVE_AMULET(new int[] { 4081, 10588 }, 3),
		AMULET_OF_FURY(new int[] { 6585, 13442, 19335, 19513 }, 5),
		SYMBOL(new int[] { 1718, 1724 }, 8),
		GOD_STOLE(new int[] { 10470, 10472, 10474, 19392, 19394, 19396 }, 10),
		TWISTED_BIRD_SKULL_NECKLACE(19886, 9),
		SPLIT_DRAGONTOOTH_NECKLACE(19887, 11),
		DEMON_HORN_NECKLACE(19888, 13),
		EXPLORERS_RING(new int[] { 13560, 13561, 13562, 19760 }, 1),
		GWS_CHESTPLATES(new int[] { 11720, 11724 }, 1),
		ZAMORAK_ROBE_TOP(1035, 3),
		DRUIDS_ROBE_TOP(540, 4),
		ELITE_VOID_TOP(new int[] { 19785, 19787, 19789, 19803 }, 4),
		GOD_ROBE_TOP(new int[] { 19382, 19380, 19384, 10462, 10458, 10460 }, 4),
		SHADES_ROBE_TOP(548, 5),
		VERACS_BRASSARD(new int[] { 4757, 4988, 4989, 4990, 4991 }, 5),
		MONKS_ROBE_TOP(544, 6),
		INITIATE_HAUBERK(5575, 6),
		THIRD_AGE_DRUIDIC_ROBE_TOP(19317, 6),
		PROSELYTE_HAUBERK(9674, 8),
		PRIEST_GOWN_TOP(426, 3),
		DAGONHAI_ROBE_TOP(14497, 2),
		WHITE_CHAINBODY(6615, 1),
		WHITE_PLATEBODY(6617, 1),
		GWS_LEGS(new int[] { 11726, 11722 }, 1),
		ZAMORAK_ROBE_BOTTOM(1033, 3),
		DRUIDS_ROBE_BOTTOM(538, 4),
		ELITE_VOID_LEGS(new int[] { 19786, 19788, 19790, 19804 }, 4),
		SHADES_ROBE_BOTTOM(546, 4),
		GOD_ROBE_BOTTOM(new int[] { 10466, 10464, 10468, 19390, 19386, 19388 },
				4),
		VERACS_PLATESKIRT(new int[] { 4759, 4994, 4995, 4996, 4997 }, 4),
		MONKS_ROBE_BOTTOM(542, 5),
		INITIATE_CUISSE(5576, 5),
		PROSELYTE_LEGS(new int[] { 9676, 9678 }, 5),
		THIRD_AGE_DRUIDIC_ROBE_BOTTOM(19320, 5),
		PRIEST_GOWN_BOTTOM(428, 3),
		DAGONHAI_ROBE_BOTTOM(14501, 2),
		WHITE_PLATELEGS(6625, 1),
		WHITE_PLATESKIRT(6627, 1),
		ARMADYL_HELMET(new int[] { 11718, 12670, 12671 }, 1),
		SEERS_HEADBAND_3_4(new int[] { 14663, 19763 }, 3),
		INITIATE_SALLET(5574, 3),
		VERACS_HELMET(new int[] { 4753, 4976, 4977, 4978, 4979, 4980 }, 3),
		HELM_OF_NEITIZNOT(new int[] { 10828, 12680, 12681 }, 3),
		PROSELYTE_SALLET(9672, 4),
		MITRES(new int[] { 19378, 19374, 19376, 10454, 10452, 10456 }, 5),
		THIRD_AGE_DRUIDIC_WREATH(19314, 7),
		WHITE_MED_HELM(6621, 1),
		WHITE_FULL_HELM(6623, 1),
		WHITE_BOOTS(6619, 1),
		BANDOS_BOOTS(11728, 1),
		WHITE_GLOVES(6629, 1),
		ARDOUGNE_CLOAK_1(15345, 2),
		ARDOUGNE_CLOAK_2(15347, 4),
		ARDOUGNE_CLOAK_3_4(new int[] { 15349, 19748 }, 6),
		FIRE_CAPE(6570, 2),
		GOD_CLOAK(new int[] { 19372, 19368, 19370, 10448, 10446, 10450 }, 3),
		CAPES_OF_ACHIEVEMENTS_TRIMMED(new int[] { 9748, 9751, 9754, 9757, 9760,
				9763, 9766, 9769, 9772, 9775, 9778, 9781, 9784, 9787, 9790,
				9793, 9796, 9799, 9802, 9805, 9808, 9811, 9949, 12170, 18509 },
				4),
		THIRD_AGE_DRUIDIC_CLOAK(19311, 6),
		MAX_CAPE(new int[] { 20747, 20767 }, 10),
		SOUL_WARS_CAPE(new int[] { 14641, 14642, 14645, 15432, 15433, 15434,
				15435, 15474 }, 12),
		COMPLETIONIST_CAPE(new int[] { 20748, 20749, 20769, 20771 }, 13),
		BRONZE_IRON_MACE(new int[] { 1422, 1420 }, 1),
		STEEL_BLACK_MACE(new int[] { 1424, 1426 }, 2),
		SARADOMIN_SWORD(11730, 2),
		ZAMORAKIAN_SPEAR(11716, 2),
		KERIS(new int[] { 10581, 10582, 10583, 10584 }, 2),
		WHITE_MITH_ADDY_ANCI_MACE(new int[] { 6601, 1428, 1430, 11061 }, 3),
		RUNE_GRAN_MACE(new int[] { 1432, 14679 }, 4),
		DRAGON_MACE(new int[] { 1434, 13985 }, 5),
		IVANDIS_FLAIL(new int[] { 13117, 13118, 13119, 13120, 13121, 13122,
				13123, 13124, 13125, 13126, 13127, 13128, 13129, 13130, 13131,
				13132, 13133, 13134, 13135, 13136, 13137, 13138, 13139, 13140,
				13141, 13142, 13143, 13144, 13145, 13146 }, 5),
		SILVER_SICKLE_B(2963, 5),
		TOKTZ_MEJ_TAL(6526, 5),
		WOLFBANE(2952, 5),
		VOID_KNIGHT_MACE(8841, 6),
		GOD_CROZIER(new int[] { 19366, 19362, 19364, 10442, 10440, 10444 }, 6),
		VERACS_FLAIL(new int[] { 4755, 4982, 4983, 4984, 4985 }, 6),
		THIRD_AGE_DRUIDIC_STAFF(19308, 7),
		GODSWORDS(new int[] { 11696, 11700, 11698, 11694 }, 8),
		ROLLING_PIN(7445, 4),
		WHITE_DAGGER_SWORD_LONGSWORD_WARHAMMER_BATTLEAXE_SCIMMY_CLAWS_2H_HALBERD_STAFF(
				new int[] { 6591, 6599, 6609, 6589, 6587, 6593, 6595, 6597,
						6607, 6603, 6611, 6605, 6613 }, 1),
		RUNE_CANE(13099, 4),
		ADDY_CANE(13097, 3),
		BLACK_CANE(13095, 2);

		private final int[] ids;
		private final int ppoints;

		private Equipment(final int id, final int ppoints) {
			ids = new int[] { id };
			this.ppoints = ppoints;
		}

		private Equipment(final int[] ids, final int ppoints) {
			this.ids = ids;
			this.ppoints = ppoints;
		}

		/**
		 * Gets the prayer equipment item id.
		 * 
		 * @return The item id.
		 */
		public int[] getIds() {
			return ids;
		}

		/**
		 * Gets the prayer points for the item.
		 * 
		 * @return The items prayer points.
		 */
		public int getPrayerPoints() {
			return ppoints;
		}
	}

	/**
	 * Gets the players' current prayer bonus.
	 * 
	 * @return The players' current prayer bonus.
	 */
	public static int getMyPrayerBonus() {
		return getPrayerBonus(Players.getLocal());
	}

	/**
	 * Gets the specified players' prayer bonus.
	 * 
	 * @param player
	 *            The player to get the prayer bonus.
	 * @return The specified players' prayer bonus.
	 */
	public static int getPrayerBonus(final Player player) {
		int prayerBonus = 0;
		outerLoop: for (final int item : player.getEquipment()) {
			for (final Equipment equipment : Equipment.values()) {
				for (final int i : equipment.getIds()) {
					if (item == i) {
						prayerBonus += equipment.getPrayerPoints();
						continue outerLoop;
					}
				}
			}
		}
		return prayerBonus;
	}
}