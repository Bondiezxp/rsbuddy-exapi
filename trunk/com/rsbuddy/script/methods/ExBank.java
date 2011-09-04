package com.rsbuddy.script.methods;

import com.rsbuddy.script.task.Task;
import com.rsbuddy.script.wrappers.BankItem;

import org.rsbuddy.tabs.Inventory;
import org.rsbuddy.widgets.Bank;

public class ExBank {

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
		if (!Bank.isOpen()) {
			if (Bank.open()) {
				for (int j = 0; j < 250 && !Bank.isOpen(); j += 1) {
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
					Bank.depositAll();
					for (int i = 0; Inventory.getCount() > 0 && i < 250; i += 1) {
						Task.sleep(10);
					}
					break;
				case DEPOSIT_ALL_EXCEPT:
					Bank.depositAllExcept(ids);
					for (int i = 0; Inventory.getCountExcept(ids) > 0
							&& i < 250; i += 1) {
						Task.sleep(10);
					}
					break;
				case DEPOSIT:
					if (amount < 0) {
						break;
					}
					for (final int id : ids) {
						final int startCount = Inventory.getCount(id);
						final int endCount = startCount - amount;
						Bank.deposit(id, amount);
						for (int i = 0; Inventory.getCount(id) != endCount
								&& i < 250; i += 1) {
							Task.sleep(10);
						}
					}
					break;
				case WITHDRAW:
					if (amount < 0) {
						break;
					}
					if (!isWithdrawalModeItem()) {
						Bank.setWithdrawModeToItem();
					}
					Task.sleep(250);
					for (final int id : ids) {
						final int startCount = Inventory.getCount(id);
						final int endCount = startCount + amount;
						Bank.withdraw(id, amount);
						for (int i = 0; Inventory.getCount(id) != endCount
								&& i < 250; i += 1) {
							Task.sleep(10);
						}
					}
					break;
				case WITHDRAW_IF_INVENTORY_DOESNT_CONTAIN:
					if (amount < 0) {
						break;
					}
					if (Inventory.containsAll(ids)) {
						break;
					}
					if (!isWithdrawalModeItem()) {
						Bank.setWithdrawModeToItem();
						Task.sleep(250);
					}
					for (final int id : ids) {
						if (Inventory.contains(id)) {
							continue;
						}
						final int startCount = Inventory.getCount(id);
						final int endCount = startCount + amount;
						Bank.withdraw(id, amount);
						for (int i = 0; Inventory.getCount(id) != endCount
								&& i < 250; i += 1) {
							Task.sleep(10);
						}
					}
					break;
				case WITHDRAW_NOTED:
					if (amount < 0) {
						break;
					}
					if (isWithdrawalModeItem()) {
						Bank.setWithdrawModeToNote();
						Task.sleep(250);
					}
					for (final int id : ids) {
						final int startCount = Inventory.getCount(id);
						final int endCount = startCount + amount;
						Bank.withdraw(id, amount);
						for (int i = 0; Inventory.getCount(id) != endCount
								&& i < 250; i += 1) {
							Task.sleep(10);
						}
					}
					break;
				case WITHDRAW_NOTED_IF_INVENTORY_DOESNT_CONTAIN:
					if (amount < 0) {
						break;
					}
					if (Inventory.containsAll(ids)) {
						break;
					}
					if (isWithdrawalModeItem()) {
						Bank.setWithdrawModeToNote();
						Task.sleep(250);
					}
					for (final int id : ids) {
						if (Inventory.contains(id)) {
							continue;
						}
						final int startCount = Inventory.getCount(id);
						final int endCount = startCount + amount;
						Bank.withdraw(id, amount);
						for (int i = 0; Inventory.getCount(id) != endCount
								&& i < 250; i += 1) {
							Task.sleep(10);
						}
					}
					break;
			}
		}
		if (Bank.isOpen()) {
			if (Bank.close()) {
				for (int i = 0; Bank.isOpen() && i < 250; i += 1) {
					Task.sleep(10);
				}
			}
		}
	}

	private static boolean isWithdrawalModeItem() {
		return Settings.get(115) == 0;
	}
}
