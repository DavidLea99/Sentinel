package com.alibaba.csp.sentinel.util;

import com.alibaba.csp.sentinel.slotchain.ProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.SlotChainBuilder;
import com.alibaba.csp.sentinel.slots.DefaultSlotChainBuilder;
import com.alibaba.csp.sentinel.slots.block.authority.AuthoritySlot;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeSlot;
import com.alibaba.csp.sentinel.slots.block.flow.FlowSlot;
import com.alibaba.csp.sentinel.slots.clusterbuilder.ClusterBuilderSlot;
import com.alibaba.csp.sentinel.slots.logger.LogSlot;
import com.alibaba.csp.sentinel.slots.nodeselector.NodeSelectorSlot;
import com.alibaba.csp.sentinel.slots.statistic.StatisticSlot;
import com.alibaba.csp.sentinel.slots.system.SystemSlot;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test cases for {@link SpiLoader}.
 *
 * @author cdfive
 * @date 2019-01-12
 */
public class SpiLoaderTest {

    @Test
    public void testLoadFirstInstance() {
        ProcessorSlot processorSlot = SpiLoader.loadFirstInstance(ProcessorSlot.class);
        assertNotNull(processorSlot);

        SlotChainBuilder slotChainBuilder = SpiLoader.loadFirstInstance(SlotChainBuilder.class);
        assertNotNull(slotChainBuilder);
        assertTrue(slotChainBuilder instanceof DefaultSlotChainBuilder);
    }

    @Test
    public void testLoadOrderedInstanceList() {
        List<ProcessorSlot> processorSlots = SpiLoader.loadOrderedInstanceList(ProcessorSlot.class);

        assertNotNull(processorSlots);

        // total 8 default slot in sentinel-core
        assertEquals(8, processorSlots.size());

        // verify the order of slots
        int index = 0;
        assertTrue(processorSlots.get(index++) instanceof NodeSelectorSlot);
        assertTrue(processorSlots.get(index++) instanceof ClusterBuilderSlot);
        assertTrue(processorSlots.get(index++) instanceof LogSlot);
        assertTrue(processorSlots.get(index++) instanceof StatisticSlot);
        assertTrue(processorSlots.get(index++) instanceof SystemSlot);
        assertTrue(processorSlots.get(index++) instanceof AuthoritySlot);
        assertTrue(processorSlots.get(index++) instanceof FlowSlot);
        assertTrue(processorSlots.get(index++) instanceof DegradeSlot);

        // verify the instances are different when loadOrderedInstanceList second time
        List<ProcessorSlot> processorSlots2 = SpiLoader.loadOrderedInstanceList(ProcessorSlot.class);
        assertNotSame(processorSlots, processorSlots2);
        assertEquals(processorSlots.size(), processorSlots2.size());
        for (int i = 0; i < processorSlots.size(); i++) {
            ProcessorSlot slot = processorSlots.get(i);
            ProcessorSlot slot2 = processorSlots2.get(i);
            assertNotSame(slot, slot2);
            assertNotEquals(slot, slot2);
            assertEquals(slot.getClass(), slot2.getClass());
        }
    }
}
