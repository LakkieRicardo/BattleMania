package net.battle.core.layouts.navinv;

import java.util.List;

/**
 * Inventory data for the NavigatorInvLayout
 */
public record NavigatorInvData(int page, List<INavigatorContentItem> contentList) {
}
