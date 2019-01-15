package com.alibaba.csp.sentinel.slots;

import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ProcessorSlotChain;
import com.alibaba.csp.sentinel.slots.block.authority.AuthoritySlot;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeSlot;
import com.alibaba.csp.sentinel.slots.block.flow.FlowSlot;
import com.alibaba.csp.sentinel.slots.clusterbuilder.ClusterBuilderSlot;
import com.alibaba.csp.sentinel.slots.logger.LogSlot;
import com.alibaba.csp.sentinel.slots.nodeselector.NodeSelectorSlot;
import com.alibaba.csp.sentinel.slots.statistic.StatisticSlot;
import com.alibaba.csp.sentinel.slots.system.SystemSlot;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for {@link DefaultSlotChainBuilder}.
 *
 * @author cdfive
 * @date 2019-01-12
 */
public class DefaultSlotChainBuilderTest {

    @Test
    public void testBuild() {
        DefaultSlotChainBuilder builder = new DefaultSlotChainBuilder();
        ProcessorSlotChain slotChain = builder.build();

        assertNotNull(slotChain);

        // verify the order of slot
        AbstractLinkedProcessorSlot<?> next = slotChain.getNext();
        assertTrue(next instanceof NodeSelectorSlot);

        next = next.getNext();
        assertTrue(next instanceof ClusterBuilderSlot);

        next = next.getNext();
        assertTrue(next instanceof LogSlot);

        next = next.getNext();
        assertTrue(next instanceof StatisticSlot);

        next = next.getNext();
        assertTrue(next instanceof SystemSlot);

        next = next.getNext();
        assertTrue(next instanceof AuthoritySlot);

        next = next.getNext();
        assertTrue(next instanceof FlowSlot);

        next = next.getNext();
        assertTrue(next instanceof DegradeSlot);

        next = next.getNext();
        assertNull(next);
    }
}
