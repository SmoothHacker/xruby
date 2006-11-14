package com.xruby.newruntime.lang;

public class RubySymbol extends RubyValue {	
	private long value;
	
	public RubySymbol(long value) {
		this.value = value;
	}
	
	public RubyID toID() {
		return new RubyID(this.value);
	}
}