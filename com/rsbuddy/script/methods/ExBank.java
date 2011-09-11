package com.rsbuddy.script.methods;

import com.rsbuddy.script.task.Task;

import org.rsbuddy.tabs.Inventory;
import org.rsbuddy.widgets.Bank;

public class ExBank {

	public static class BankItem {

		public static final int NOTED = 0x1;
		public static final int ALL_EXCEPT = 0x2;
		public static final int ALL = 0x4;
		public static final int DEPOSIT = 0x8;
		public static final int WITHDRAW = 0x10;

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
		for (int i = 0; !Bank.isOpen() && i < 3; i += 1) {
			if (Bank.open()) {
				for (int j = 0; !Bank.isOpen() && j < 250; j += 1) {
					Task.sleep(10);
				}
			}
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
				if ((options & BankItem.NOTED) == BankItem.NOTED) {
					for (final int id : ids) {
						withdraw(id, amount, true);
					}
				} else {
					for (final int id : ids) {
						withdraw(id, amount, false);
					}
				}
			} else if ((options & BankItem.DEPOSIT) == BankItem.DEPOSIT) {
				if ((options & BankItem.ALL) == BankItem.ALL) {
					depositAll();
				} else if ((options & BankItem.ALL_EXCEPT) == BankItem.ALL_EXCEPT) {
					depositAllExcept(ids);
				} else {
					System.out.println("We will deposit this item");
					for (final int id : ids) {
						deposit(id, amount);
					}
				}
			}
		}
		for (int i = 0; Bank.isOpen() && i < 3; i += 1) {
			if (Bank.close()) {
				for (int j = 0; Bank.isOpen() && j < 250; j += 1) {
					Task.sleep(10);
				}
			}
		}
	}

	private static void deposit(final int id, final int amount) {
		if (id < 0 || !Inventory.contains(id) || amount == 0) {
			return;
		}
		final int startCount = Inventory.getCount(id);
		final int endCount = startCount - amount;
		for (int i = 0; Inventory.getCount(id) != endCount && i < 3; i += 1) {
			if (Bank.deposit(id, amount)) {
				for (int j = 0; Inventory.getCount(id) != endCount && j < 250; j += 1) {
					Task.sleep(10);
				}
			}
		}
	}

	private static void depositAll() {
		if (Inventory.getCount() < 1) {
			return;
		}
		for (int i = 0; Inventory.getCount() > 0 && i < 3; i += 1) {
			if (Bank.depositAll()) {
				for (int j = 0; Inventory.getCount() > 0 && j < 250; j += 1) {
					Task.sleep(10);
				}
			}
		}
	}

	private static void depositAllExcept(final int... ids) {
		if (Inventory.getCountExcept(ids) < 1) {
			return;
		}
		for (int i = 0; Inventory.getCountExcept(ids) > 0 && i < 3; i += 1) {
			if (Bank.depositAllExcept(ids)) {
				for (int j = 0; Inventory.getCountExcept(ids) > 0 && j < 250; j += 1) {
					Task.sleep(10);
				}
			}
		}
	}

	private static void withdraw(final int id, final int amount,
			final boolean noted) {
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
				for (int j = 0; Inventory.getCount(id) != endCount && j < 250; j += 1) {
					Task.sleep(10);
				}
			}
		}
	}
}