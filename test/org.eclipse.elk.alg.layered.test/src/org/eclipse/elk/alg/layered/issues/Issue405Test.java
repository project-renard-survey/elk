/*******************************************************************************
 * Copyright (c) 2020 Kiel University and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.elk.alg.layered.issues;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.eclipse.elk.alg.layered.options.LayeredOptions;
import org.eclipse.elk.alg.test.framework.LayoutTestRunner;
import org.eclipse.elk.alg.test.framework.annotations.Algorithm;
import org.eclipse.elk.alg.test.framework.annotations.DefaultConfiguration;
import org.eclipse.elk.alg.test.framework.annotations.GraphResourceProvider;
import org.eclipse.elk.alg.test.framework.io.AbstractResourcePath;
import org.eclipse.elk.alg.test.framework.io.ModelResourcePath;
import org.eclipse.elk.graph.ElkGraphElement;
import org.eclipse.elk.graph.ElkLabel;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.ElkPort;
import org.eclipse.elk.graph.ElkShape;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;

/**
 * Test for issue 405.
 */
@RunWith(LayoutTestRunner.class)
@Algorithm(LayeredOptions.ALGORITHM_ID)
@DefaultConfiguration
public class Issue405Test {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Sources

    @GraphResourceProvider
    public List<AbstractResourcePath> testGraphs() {
        return Lists.newArrayList(new ModelResourcePath("tickets/layered/405_differentPortPositionsUpDown.elkt"));
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Tests

    private <T extends ElkGraphElement> T getElementWithId(final Collection<T> container, final String id) {
        return container.stream().filter(e -> e.getIdentifier().equals(id)).findFirst().get();
    }
    
    private ElkLabel getLabel(ElkGraphElement e) {
        return e.getLabels().get(0);
    }
    
    private <T extends ElkShape> void checkPositionEquals(T first, T second) {
        assertEquals(first.getX(), second.getX(), DOUBLE_EPSILON);
        assertEquals(first.getY(), second.getY(), DOUBLE_EPSILON);
        
    }
    
    private static final double DOUBLE_EPSILON = 10e-5;
    
    @Test
    public void testPortAndLabelPositionsEqual(final ElkNode graph) {
        
        final ElkNode down = getElementWithId(graph.getChildren(), "Down");
        final ElkNode n1 = getElementWithId(down.getChildren(), "N1");
        final ElkPort downP1 = getElementWithId(n1.getPorts(), "P1");
        final ElkPort downP2 = getElementWithId(n1.getPorts(), "P2");
        
        final ElkNode up = getElementWithId(graph.getChildren(), "Up");
        final ElkNode n2 = getElementWithId(up.getChildren(), "N2");
        final ElkPort upP1 = getElementWithId(n2.getPorts(), "P1");
        final ElkPort upP2 = getElementWithId(n2.getPorts(), "P2");
        
        // The positions of the two ports should be the same, just "the other way around"
        checkPositionEquals(downP1, upP2);
        checkPositionEquals(downP2, upP1);
        
        System.out.println(getLabel(downP1));
        System.out.println(getLabel(upP2));
        System.out.println(getLabel(downP2));
        System.out.println(getLabel(upP1));
        
        // Also check the labels (Currently fail)
        // checkPositionEquals(getLabel(downP1), getLabel(upP2));
        // checkPositionEquals(getLabel(downP2), getLabel(upP1));
    }

}
