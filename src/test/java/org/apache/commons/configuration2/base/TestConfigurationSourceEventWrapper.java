/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.configuration2.base;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.EasyMock;

/**
 * Test class for {@code ConfigurationSourceEventWrapper}.
 *
 * @author Commons Configuration team
 * @version $Id$
 */
public class TestConfigurationSourceEventWrapper extends TestCase
{
    /** Constant for a property name. */
    private static final String PROPERTY = "testProperty";

    /** Constant for a test value. */
    private static final Object VALUE = 42;

    /** Stores a mock for the wrapped source. */
    private FlatConfigurationSource wrappedSource;

    /** A test listener. */
    private ConfigurationSourceListenerImpl listener;

    /** The source to be tested. */
    private ConfigurationSourceEventWrapper wrapper;

    @Override
    protected void setUp() throws Exception
    {
        wrappedSource = EasyMock.createMock(FlatConfigurationSource.class);
        wrapper = new ConfigurationSourceEventWrapper(wrappedSource);
        listener = new ConfigurationSourceListenerImpl();
        wrapper.addConfigurationSourceListener(listener);
    }

    /**
     * Tests whether the correct wrapped source is returned.
     */
    public void testGetWrappedSource()
    {
        assertEquals("Wrong wrapped source", wrappedSource, wrapper
                .getWrappedSource());
    }

    /**
     * Tries to create an instance without a wrapped source. This should cause
     * an exception.
     */
    public void testInitNullSource()
    {
        try
        {
            new ConfigurationSourceEventWrapper(null);
            fail("Could create instance without a wrapped source!");
        }
        catch (IllegalArgumentException iex)
        {
            // ok
        }
    }

    /**
     * Tests whether a property can be added and the expected events are
     * generated.
     */
    public void testAddProperty()
    {
        wrappedSource.addProperty(PROPERTY, VALUE);
        EasyMock.replay(wrappedSource);
        wrapper.addProperty(PROPERTY, VALUE);
        EasyMock.verify(wrappedSource);
        listener.checkEvent(ConfigurationSourceEvent.Type.ADD_PROPERTY,
                PROPERTY, VALUE);
        listener.checkDone();
    }

    /**
     * Tests the clear() implementation and the events generated by it.
     */
    public void testClear()
    {
        wrappedSource.clear();
        EasyMock.replay(wrappedSource);
        wrapper.clear();
        EasyMock.verify(wrappedSource);
        listener.checkEvent(ConfigurationSourceEvent.Type.CLEAR_SOURCE, null,
                null);
        listener.checkDone();
    }

    /**
     * Tests whether a property can be removed and the expected events are
     * generated.
     */
    public void testClearProperty()
    {
        wrappedSource.clearProperty(PROPERTY);
        EasyMock.replay(wrappedSource);
        wrapper.clearProperty(PROPERTY);
        EasyMock.verify(wrappedSource);
        listener.checkEvent(ConfigurationSourceEvent.Type.CLEAR_PROPERTY,
                PROPERTY, null);
        listener.checkDone();
    }

    /**
     * Tests the containsKey() implementation.
     */
    public void testContainsKey()
    {
        EasyMock.expect(wrappedSource.containsKey(PROPERTY)).andReturn(
                Boolean.TRUE);
        EasyMock.expect(wrappedSource.containsKey(PROPERTY)).andReturn(
                Boolean.FALSE);
        EasyMock.replay(wrappedSource);
        assertTrue("Wrong result (1)", wrapper.containsKey(PROPERTY));
        assertFalse("Wrong result (2)", wrapper.containsKey(PROPERTY));
        listener.checkDone();
    }

    /**
     * Tests the getKeys() implementation.
     */
    public void testGetKeys()
    {
        List<String> keyList = Collections.singletonList(PROPERTY);
        Iterator<String> it = keyList.iterator();
        EasyMock.expect(wrappedSource.getKeys()).andReturn(it);
        EasyMock.replay(wrappedSource);
        assertSame("Wrong iterator", it, wrapper.getKeys());
        EasyMock.verify(wrappedSource);
        listener.checkDone();
    }

    /**
     * Tests the getKeys() implementation that takes a prefix.
     */
    public void testGetKeysPrefix()
    {
        List<String> keyList = Collections.singletonList(PROPERTY);
        Iterator<String> it = keyList.iterator();
        EasyMock.expect(wrappedSource.getKeys(PROPERTY)).andReturn(it);
        EasyMock.replay(wrappedSource);
        assertSame("Wrong iterator", it, wrapper.getKeys(PROPERTY));
        EasyMock.verify(wrappedSource);
        listener.checkDone();
    }

    /**
     * Tests whether a property can be queried.
     */
    public void testGetProperty()
    {
        EasyMock.expect(wrappedSource.getProperty(PROPERTY)).andReturn(VALUE);
        EasyMock.replay(wrappedSource);
        assertEquals("Wrong value", VALUE, wrapper.getProperty(PROPERTY));
        EasyMock.verify(wrappedSource);
        listener.checkDone();
    }

    /**
     * Tests the isEmpty() implementation.
     */
    public void testIsEmpty()
    {
        EasyMock.expect(wrappedSource.isEmpty()).andReturn(Boolean.TRUE);
        EasyMock.expect(wrappedSource.isEmpty()).andReturn(Boolean.FALSE);
        EasyMock.replay(wrappedSource);
        assertTrue("Wrong result (1)", wrapper.isEmpty());
        assertFalse("Wrong result (2)", wrapper.isEmpty());
        EasyMock.verify(wrappedSource);
        listener.checkDone();
    }

    /**
     * Tests whether a property can be set and whether the correct events are
     * generated.
     */
    public void testSetProperty()
    {
        wrappedSource.setProperty(PROPERTY, VALUE);
        EasyMock.replay(wrappedSource);
        wrapper.setProperty(PROPERTY, VALUE);
        EasyMock.verify(wrappedSource);
        listener.checkEvent(ConfigurationSourceEvent.Type.MODIFY_PROPERTY,
                PROPERTY, VALUE);
        listener.checkDone();
    }

    /**
     * Tests the size() implementation.
     */
    public void testSize()
    {
        final int size = 112;
        EasyMock.expect(wrappedSource.size()).andReturn(size);
        EasyMock.replay(wrappedSource);
        assertEquals("Wrong size", size, wrapper.size());
        EasyMock.verify(wrappedSource);
        listener.checkDone();
    }

    /**
     * Tests the valueCount() implementation.
     */
    public void testValueCount()
    {
        final int count = 3;
        EasyMock.expect(wrappedSource.valueCount(PROPERTY)).andReturn(count);
        EasyMock.replay(wrappedSource);
        assertEquals("Wrong count", count, wrapper.valueCount(PROPERTY));
        EasyMock.verify(wrappedSource);
        listener.checkDone();
    }

    /**
     * Tries to add a null listener. This should cause an exception.
     */
    public void testAddConfigurationSourceListenerNull()
    {
        try
        {
            wrapper.addConfigurationSourceListener(null);
            fail("Could add a null listener!");
        }
        catch (IllegalArgumentException iex)
        {
            // ok
        }
    }

    /**
     * Tests whether listeners can be removed.
     */
    public void testRemoveConfigurationSourceListener()
    {
        wrappedSource.clearProperty(PROPERTY);
        wrappedSource.addProperty(PROPERTY, VALUE);
        wrappedSource.clear();
        EasyMock.replay(wrappedSource);
        ConfigurationSourceListenerImpl l = new ConfigurationSourceListenerImpl();
        wrapper.addConfigurationSourceListener(l);
        wrapper.clearProperty(PROPERTY);
        assertTrue("Cannot remove listener (1)", wrapper
                .removeConfigurationSourceListener(l));
        wrapper.addProperty(PROPERTY, VALUE);
        assertTrue("Cannot remove listener (2)", wrapper
                .removeConfigurationSourceListener(listener));
        wrapper.clear();
        EasyMock.verify(wrappedSource);
        l.checkEvent(ConfigurationSourceEvent.Type.CLEAR_PROPERTY, PROPERTY,
                null);
        l.checkDone();
        listener.checkEvent(ConfigurationSourceEvent.Type.CLEAR_PROPERTY,
                PROPERTY, null);
        listener.checkEvent(ConfigurationSourceEvent.Type.ADD_PROPERTY,
                PROPERTY, VALUE);
        listener.checkDone();
    }

    /**
     * Tries to remove a listener that was not registered.
     */
    public void testRemoveConfigurationSourceListenerNonExisting()
    {
        assertTrue("Could not remove listener", wrapper
                .removeConfigurationSourceListener(listener));
        assertFalse("Could remove listener again", wrapper
                .removeConfigurationSourceListener(listener));
        assertFalse("Could remove null listener", wrapper
                .removeConfigurationSourceListener(null));
    }

    /**
     * Tests the implementation of getCapability().
     */
    public void testGetCapability()
    {
        final Object cap = new Object();
        EasyMock.expect(wrappedSource.getCapability(Object.class)).andReturn(
                cap);
        EasyMock.replay(wrappedSource);
        assertEquals("Wrong capability", cap, wrapper
                .getCapability(Object.class));
        EasyMock.verify(wrappedSource);
    }

    /**
     * A test configuration source listener implementation used for testing the
     * events generated by the source.
     */
    private class ConfigurationSourceListenerImpl implements
            ConfigurationSourceListener
    {
        /** A list with the received events. */
        private final List<ConfigurationSourceEvent> events = new LinkedList<ConfigurationSourceEvent>();

        /**
         * Records this invocation.
         */
        public void configurationSourceChanged(ConfigurationSourceEvent event)
        {
            events.add(event);
        }

        /**
         * Tests the next pair of events received by this listener. This method
         * expects that there are two identical events in sequence that only
         * differ in their before update flag.
         *
         * @param type the expected event type
         * @param prop the expected property name
         * @param value the expected property value
         */
        public void checkEvent(ConfigurationSourceEvent.Type type, String prop,
                Object value)
        {
            checkEvent(type, prop, value, true);
            checkEvent(type, prop, value, false);
        }

        /**
         * Tests the next event received by this listener. The properties are
         * compared to the event's data.
         *
         * @param type the expected event type
         * @param prop the expected property name
         * @param value the expected property value
         * @param before the before update flag
         */
        public void checkEvent(ConfigurationSourceEvent.Type type, String prop,
                Object value, boolean before)
        {
            assertFalse("Too few events", events.isEmpty());
            ConfigurationSourceEvent event = events.remove(0);
            assertEquals("Wrong source", wrapper, event.getSource());
            assertEquals("Wrong type", type, event.getType());
            assertEquals("Wrong property", prop, event.getPropertyName());
            assertEquals("Wrong value", value, event.getPropertyValue());
            assertEquals("Wrong before update flag", before, event
                    .isBeforeUpdate());
            assertNull("Got additional data", event.getData());
        }

        /**
         * Tests whether all events have been checked. This method should be
         * called to verify that there are no additional events.
         */
        public void checkDone()
        {
            assertTrue("Too many events: " + events, events.isEmpty());
        }
    }
}
