package com.rsbuddy.script.methods;

import com.rsbuddy.script.action.Action;
import com.rsbuddy.script.util.Condition;

import org.rsbuddy.tabs.Inventory;
import org.rsbuddy.tabs.Summoning;
import org.rsbuddy.widgets.Bank;

/**
 * @author Ramus
 */
public class ExBank {

	public static class BankItem {

		public static final int WITHDRAW = 0x1;
		public static final int DEPOSIT = 0x2;
		public static final int ALL = 0x4;
		public static final int ALL_EXCEPT = 0x8;
		public static final int NOTED = 0x10;
		public static final int ALL_FAMILIAR = 0x20;
		public static final int ALL_EQUIPPED = 0X40;

		private final int amount;
		private final int options;
		private final int[] ids;

		/**
		 * Used for storing data when banking.
		 * 
		 * @param options
		 *            The options to do. Example BankItem.WITHDRAW |
		 *            BankItem.ALL | BankItem.NOTED.
		 * @param amount
		 *            The amount to withdraw/deposit. Use -1 when depositing all
		 *            or depositing all except.
		 * @param ids
		 *            The item ids to bank.
		 */
		public BankItem(final int options, final int amount, final int... ids) {
			this.amount = amount;
			this.ids = ids;
			this.options = options;
		}

		public int getAmount() {
			return amount;
		}

		public int[] getIds() {
			return ids;
		}

		public int getOptions() {
			return options;
		}
	}

	/**
	 * Banks the specified bank items.
	 * 
	 * @param bankItems
	 *            The bank items to bank.
	 */
	public static void bank(final BankItem... bankItems) {
		if (bankItems == null || bankItems.length == 0) {
			throw new IllegalArgumentException();
		}
		if (!openBank()) {
			return;
		}
		for (final BankItem bankItem : bankItems) {
			if (bankItem == null) {
				continue;
			}
			final int[] ids = bankItem.getIds();
			if (ids == null || ids.length < 1) {
				continue;
			}
			final int amount = bankItem.getAmount();
			final int options = bankItem.getOptions();
			if ((options & BankItem.WITHDRAW) == BankItem.WITHDRAW) {
				for (final int id : ids) {
					withdraw(id, amount, (options & BankItem.NOTED) == BankItem.NOTED);
				}
			} else if ((options & BankItem.DEPOSIT) == BankItem.DEPOSIT) {
				if ((options & BankItem.ALL) == BankItem.ALL) {
					depositAll();
				} else if ((options & BankItem.ALL_EQUIPPED) == BankItem.ALL_EQUIPPED) {
					depositAllEquipped();
				} else if ((options & BankItem.ALL_EXCEPT) == BankItem.ALL_EXCEPT) {
					depositAllExcept(ids);
				} else if ((options & BankItem.ALL_FAMILIAR) == BankItem.ALL_FAMILIAR) {
					depositAllFamiliar();
				} else {
					for (final int id : ids) {
						deposit(id, amount);
					}
				}
			}
		}
		closeBank();
	}

	private static boolean closeBank() {
		for (int i = 0; Bank.isOpen() && i < 3; i += 1) {
			if (Bank.close()) {
				Action.sleep(2500, new Condition() {

					@Override
					public boolean isValid() {
						return !Bank.isOpen();
					}
				});
			}
		}
		return !Bank.isOpen();
	}

	private static void deposit(final int id, final int amount) {
		if (id < 0 || !Inventory.contains(id) || amount == 0) {
			return;
		}
		final int startCount = Inventory.getCount(id);
		final int endCount = startCount - amount;
		for (int i = 0; Inventory.getCount(id) != endCount && i < 3; i += 1) {
			if (Bank.deposit(id, amount)) {
				Action.sleep(2500, new Condition() {

					@Override
					public boolean isValid() {
						return Inventory.getCount(id) == endCount;
					}
				});
			}
		}
	}

	private static void depositAll() {
		if (Inventory.getCount() < 1) {
			return;
		}
		for (int i = 0; Inventory.getCount() > 0 && i < 3; i += 1) {
			if (Bank.depositAll()) {
				Action.sleep(2500, new Condition() {

					@Override
					public boolean isValid() {
						return Inventory.getCount() == 0;
					}
				});
			}
		}
	}

	private static void depositAllEquipped() {
		if (Players.getLocal().getEquipment().length == 0) {
			return;
		}
		for (int i = 0; Players.getLocal().getEquipment().length > 0 && i < 3; i += 1) {
			if (Bank.depositAllEquipped()) {
				Action.sleep(2500, new Condition() {

					@Override
					public boolean isValid() {
						return Players.getLocal().getEquipment().length == 0;
					}
				});
			}
		}
	}

	private static void depositAllExcept(final int... ids) {
		if (Inventory.getCountExcept(ids) < 1) {
			return;
		}
		for (int i = 0; Inventory.getCountExcept(ids) > 0 && i < 3; i += 1) {
			if (Bank.depositAllExcept(ids)) {
				Action.sleep(2500, new Condition() {

					@Override
					public boolean isValid() {
						return Inventory.getCountExcept(ids) == 0;
					}
				});
			}
		}
	}

	private static void depositAllFamiliar() {
		if (!Summoning.isFamiliarSummoned() || Summoning.getSummonedFamiliar().canStore()) {
			return;
		}
		Bank.depositAllFamiliar();
	}

	private static boolean openBank() {
		for (int i = 0; !Bank.isOpen() && i < 3; i += 1) {
			if (Bank.open()) {
				Action.sleep(2500, new Condition() {

					@Override
					public boolean isValid() {
						return Bank.isOpen();
					}
				});
			}
		}
		return Bank.isOpen();
	}

	private static void withdraw(final int id, final int amount, final boolean noted) {
		if (id < 0 || Inventory.isFull() || amount < 0) {
			return;
		}
		if (noted) {
			Bank.setWithdrawModeToNote();
		} else {
			Bank.setWithdrawModeToItem();
		}
		final int startCount = Inventory.getCount(id);
		final int endCount = startCount + amount;
		for (int i = 0; Inventory.getCount(id) != endCount && i < 3; i += 1) {
			if (Bank.withdraw(id, amount)) {
				Action.sleep(2500, new Condition() {

					@Override
					public boolean isValid() {
						return Inventory.getCount(id) == endCount;
					}
				});
			}
		}
	}
}