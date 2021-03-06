/** 
 * Copyright 2005-2007 Xue Yong Zhi
 * Distributed under the Apache License
 */

package com.xruby;

import junit.framework.TestCase;

public class CommandLineOptionsTest extends TestCase {
	public void test_null_array() {
		CommandLineOptions options = new CommandLineOptions(new String[0]);
		assertFalse(options.isCompileOnly());
		assertFalse(options.isHelp());
		assertNull(options.getFilename());
	}

	public void test_compile_only() {
		CommandLineOptions options = new CommandLineOptions(new String[] {"-c"});
		assertTrue(options.isCompileOnly());
		assertFalse(options.isHelp());
		assertNull(options.getFilename());
	}
	
	public void test_help() {
		CommandLineOptions options = new CommandLineOptions(new String[] {"-h"});
		assertFalse(options.isCompileOnly());
		assertTrue(options.isHelp());
		assertNull(options.getFilename());
	}

	public void test_one_file() {
		CommandLineOptions options = new CommandLineOptions(new String[] {"test2.rb"});
		assertFalse(options.isCompileOnly());
		assertFalse(options.isHelp());
		assertEquals("test2.rb", options.getFilename());
		assertEquals(0, options.getArgs().length);
	}
	
	public void test_more_than_one() {
		CommandLineOptions options = new CommandLineOptions(new String[] {"-c", "c:\\test1", "xxx"});
		assertTrue(options.isCompileOnly());
		assertTrue(!options.isHelp());
		assertEquals("c:\\test1", options.getFilename());
		assertEquals(1, options.getArgs().length);
		assertEquals("xxx", options.getArgs()[0]);
	}
	
	public void test_eval_one_line() {
		CommandLineOptions options = new CommandLineOptions(new String[] {"-e", "'print \"foobar\"'"});
		assertTrue(options.isEvalOneLine());
		assertEquals("print \"foobar\"", options.getEvalScript());
	}
	
	public void test_eval_one_line_2() {
		CommandLineOptions options = new CommandLineOptions(new String[] {"-e", "'print", "\"foobar\"'"});
		assertTrue(options.isEvalOneLine());
		assertEquals("print \"foobar\"", options.getEvalScript());
	}
	
	public void test_dash_s() {
		CommandLineOptions options = new CommandLineOptions(new String[] {"-s", "filename", "-zzz", "-yyy=555"});
		assertEquals("filename", options.getFilename());
		assertEquals(2, options.getVars().length);
		assertEquals("zzz", options.getVars()[0]);
		assertEquals("yyy=555", options.getVars()[1]);
		assertEquals(0, options.getArgs().length);
	}
	
	public void test_dash_s_2() {
		CommandLineOptions options = new CommandLineOptions(new String[] {"-s", "filename", "zzz", "-yyy=555"});
		assertEquals("filename", options.getFilename());
		assertEquals(1, options.getVars().length);
		assertEquals("yyy=555", options.getVars()[0]);
		assertEquals(1, options.getArgs().length);
		assertEquals("zzz", options.getArgs()[0]);
	}
	
	public void test_dash_i() {
		CommandLineOptions options = new CommandLineOptions(new String[] {"-i.bak", "-pe", "print 1", "filename"});
		assertEquals(".bak", options.getBackupExtension());
		assertEquals("filename", options.getFilename());
		assertEquals("while gets();print 1;end", options.getEvalScript());
	}
}
