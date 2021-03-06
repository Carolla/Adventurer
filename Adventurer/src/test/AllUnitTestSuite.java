/**
 * AdventureUnitTestSuite.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test;

import mylib.test.MyLibraryUnitTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import chronos.test.ChronosUnitTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  MyLibraryUnitTestSuite.class,
  ChronosUnitTestSuite.class,
  AdventurerUnitTestSuite.class,
})

public class AllUnitTestSuite
{
}

