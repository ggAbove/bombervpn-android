package com.v2ray.ang.ui.main

import com.v2ray.ang.dto.GroupMapItem

/**
 * Group tabs a customer should see. Groups without a single server (the built-in "Default" one
 * right after a subscription import) only confuse people, so they stay hidden while any other
 * group has servers. When nothing has servers the list is returned as is so the UI is never blank.
 */
internal fun visibleGroups(groups: List<GroupMapItem>, serverCount: (String) -> Int): List<GroupMapItem> {
    val nonEmpty = groups.filter { serverCount(it.id) > 0 }
    return if (nonEmpty.isEmpty()) groups else nonEmpty
}

/** The subscription a batch import just created, if any: the first id that was not there before. */
internal fun newlyImportedSubscriptionId(before: Collection<String>, after: List<String>): String? {
    val known = before.toHashSet()
    return after.firstOrNull { it !in known }
}

/**
 * Which server the home screen should select after a group loads: keep a valid current choice,
 * otherwise the first server of the group. The "all servers" group (empty id) accepts any guid.
 */
internal fun resolveSelectedServer(groupId: String, guids: List<String>, current: String?): String? {
    if (!current.isNullOrBlank() && (groupId.isEmpty() || current in guids)) return current
    return guids.firstOrNull()
}
