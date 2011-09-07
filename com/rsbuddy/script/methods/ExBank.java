package com.rsbuddy.script.methods;

import com.rsbuddy.script.task.Task;

import org.rsbuddy.tabs.Inventory;
import org.rsbuddy.widgets.Bank;

public class ExBank {

	public static enum BankEvent {

		DEPOSIT_ALL,
		DEPOSIT_ALL_EXCEPT,
		DEPOSIT,
		WITHDRAW,
		WITHDRAW_IF_INVENTORY_DOESNT_CONTAIN,
		WITHDRAW_NOTED,
		WITHDRAW_NOTED_IF_INVENTORY_DOESNT_CONTAIN;
	}

	public static class BankItem {

		private final int amount;
		private final BankEvent event;
		private final int[] ids;

		/**
		 * Used for storing data when banking.
		 * 
		 * @param event
		 *            The bank event to do.
		 * @param amount
		 *            The amount to withdraw/deposit. Use -1 when depositing all
		 *            or depositing all except.
		 * @param ids
		 *            The item ids to bank.
		 */
		public BankItem(final BankEvent event, final int amount,
				final int... ids) {
			this.amount = amount;
			this.event = event;
			this.ids = ids;
		}

		public int getAmount() {
			return amount;
		}

		public BankEvent getEvent() {
			return event;
		}

		public int[] getIds() {
			return ids;
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
			switch (bankItem.getEvent()) {
				case DEPOSIT_ALL:
					depositAll();
					break;
				case DEPOSIT_ALL_EXCEPT:
					depositAllExcept(ids);
					break;
				case DEPOSIT:
					for (final int id : ids) {
						deposit(id, amount);
					}
					break;
				case WITHDRAW:
					for (final int id : ids) {
						withdraw(id, amount, false);
					}
					break;
				case WITHDRAW_IF_INVENTORY_DOESNT_CONTAIN:
					if (Inventory.containsAll(ids)) {
						break;
					}
					for (final int id : ids) {
						if (Inventory.contains(id)) {
							continue;
						}
						withdraw(id, amount, false);
					}
					break;
				case WITHDRAW_NOTED:
					for (final int id : ids) {
						withdraw(id, amount, true);
					}
					break;
				case WITHDRAW_NOTED_IF_INVENTORY_DOESNT_CONTAIN:
					if (Inventory.containsAll(ids)) {
						break;
					}
					for (final int id : ids) {
						if (Inventory.contains(id)) {
							continue;
						}
						withdraw(id, amount, true);
					}
					break;
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