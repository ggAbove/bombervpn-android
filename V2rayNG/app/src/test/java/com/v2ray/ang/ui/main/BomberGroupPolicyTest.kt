package com.v2ray.ang.ui.main

import com.v2ray.ang.dto.GroupMapItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class BomberGroupPolicyTest {
    private val default = GroupMapItem(id = "default", remarks = "Default")
    private val imported = GroupMapItem(id = "sub", remarks = "import sub")

    @Test
    fun emptyDefaultGroupIsHiddenOnceASubscriptionHasServers() {
        val counts = mapOf("default" to 0, "sub" to 27)
        assertEquals(listOf(imported), visibleGroups(listOf(default, imported)) { counts.getValue(it) })
    }

    @Test
    fun allGroupsStayVisibleWhileNoneHasServers() {
        assertEquals(listOf(default, imported), visibleGroups(listOf(default, imported)) { 0 })
    }

    @Test
    fun groupsWithServersAreAllKept() {
        assertEquals(listOf(default, imported), visibleGroups(listOf(default, imported)) { 1 })
    }

    @Test
    fun importedSubscriptionIsTheIdThatWasNotThereBefore() {
        assertEquals("sub", newlyImportedSubscriptionId(before = listOf("default"), after = listOf("default", "sub")))
        assertNull(newlyImportedSubscriptionId(before = listOf("default", "sub"), after = listOf("default", "sub")))
    }

    @Test
    fun selectionKeepsAValidChoiceAndFallsBackToTheFirstServer() {
        assertEquals("b", resolveSelectedServer("sub", listOf("a", "b"), current = "b"))
        assertEquals("a", resolveSelectedServer("sub", listOf("a", "b"), current = null))
        assertEquals("a", resolveSelectedServer("sub", listOf("a", "b"), current = "gone"))
        assertEquals("x", resolveSelectedServer("", listOf("a"), current = "x"))
        assertNull(resolveSelectedServer("sub", emptyList(), current = null))
    }
}
