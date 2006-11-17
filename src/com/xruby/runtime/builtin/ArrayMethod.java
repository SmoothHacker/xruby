package com.xruby.runtime.builtin;

import com.xruby.runtime.lang.NoBlockRubyMethod;
import com.xruby.runtime.lang.RubyBlock;
import com.xruby.runtime.lang.RubyConstant;
import com.xruby.runtime.lang.RubyMethod;
import com.xruby.runtime.lang.RubyNoArgMethod;
import com.xruby.runtime.lang.RubyOneArgMethod;
import com.xruby.runtime.lang.RubyRuntime;
import com.xruby.runtime.lang.RubySymbol;
import com.xruby.runtime.lang.RubyAPI;
import com.xruby.runtime.lang.RubyValue;
import com.xruby.runtime.value.RubyArray;
import com.xruby.runtime.value.RubyFixnum;

class ArrayMethod {
	public static RubyMethod alloc = new RubyNoArgMethod() {
		protected RubyValue run(RubyValue receiver) {
			return new RubyArray();
		}
	};
	
	public static RubyMethod initialize = new NoBlockRubyMethod() {
		public RubyValue run(RubyValue receiver, RubyArray args) {
			int args_length = (args == null) ? 0 : args.length();
			
			switch (args_length) {
			case 0:
				return receiver;
				// FIXME: block given
			case 1:
				break;
			case 2:
				break;
			}
			return null;
		}
		
	};
	
	public static RubyMethod length = new RubyNoArgMethod() {
		protected RubyValue run(RubyValue receiver) {
			return RubyFixnum.long2Fix(((RubyArray)receiver).length());
		}		
	};
	
	public static RubyMethod empty = new RubyNoArgMethod() {
		protected RubyValue run(RubyValue receiver) {
			return RubyAPI.test(((RubyArray)receiver).isEmpty());
		}
	};
	
	public static RubyMethod toS = new RubyNoArgMethod() {
		protected RubyValue run(RubyValue receiver) {
			RubyArray array = (RubyArray)receiver;
			return array.toS();
		}
	};
	
	public static RubyMethod toAry = new RubyNoArgMethod() {
		protected RubyValue run(RubyValue receiver) {
			return receiver;
		}
	};
	
	public static RubyMethod aref = new NoBlockRubyMethod() {
		public RubyValue run(RubyValue receiver, RubyArray args) {
			RubyArray array = (RubyArray)receiver;
			switch (args.length()) {
			case 2:
				RubyValue argValue0 = args.get(0);
				if (argValue0 instanceof RubySymbol) {
					RubyAPI.raise(RubyRuntime.typeError, "Symbol as array index");
				}
				
				int begin = RubyAPI.valueToInt(args.get(0));
				int length = RubyAPI.valueToInt(args.get(1));
				return ((RubyArray)receiver).subArray(begin, length);
			default:
				RubyValue argValue = args.get(0);
				if (argValue instanceof RubyFixnum) {
					RubyFixnum index = (RubyFixnum)argValue;
					return array.get(index.intValue());
				}
				
				if (argValue instanceof RubySymbol) {
					RubyAPI.raise(RubyRuntime.typeError, "Symbol as array index");
				}
				
				// FIXME: Range
				
			}
			
			return null;
		}		
	};
	
	public static RubyMethod aset = new NoBlockRubyMethod() {
		public RubyValue run(RubyValue receiver, RubyArray args) {
			RubyArray array = (RubyArray)receiver;
			
			// argc == 3
			if (args.length() == 3) {
				if (args.get(0) instanceof RubySymbol) {
					RubyAPI.raise(RubyRuntime.typeError, "Symbol as array index");
				}
				
				if (args.get(1) instanceof RubySymbol) {
					RubyAPI.raise(RubyRuntime.typeError, "Symbol as subarray length");
				}
				
				// FIXME:splice array
			}
			
			// wrong arg
			if (args.length() != 2) {
				RubyAPI.raise(RubyRuntime.argumentError, "wrong number of arguments (%d for 2)", args.length());
			}
			
			// default
			if (args.get(0) instanceof RubyFixnum) {
				int index = ((RubyFixnum)args.get(0)).intValue(); 
				array.set(index, args.get(1));
			}
			
			// FIXME: other case
			
			return args.get(1);
		}
	};
	
	public static RubyMethod equal = new RubyOneArgMethod() {		
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			if (receiver == arg) {
				return RubyConstant.QTRUE;
			}
			
			if (!(arg instanceof RubyValue)) {
				// FIXME: response to_ary
				return RubyConstant.QFALSE;
			}
			
			return RubyAPI.test(((RubyValue)receiver).equals(arg));
		}		
	};
	
	public static RubyMethod concat = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			RubyArray left = (RubyArray)receiver;
			RubyArray right;
			if (arg instanceof RubyArray) {
				right = (RubyArray)arg;
			} else {
				// FIXME: response to_ary
				right = null;
			}
			
			left.concat(right);
			return left;
		}
	};
	
	public static RubyMethod plus = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			RubyArray left = (RubyArray)receiver;
			RubyArray right;
			if (arg instanceof RubyArray) {
				right = (RubyArray)arg;
			} else {
				// FIXME: response to_ary
				right = null;
			}
			
			return left.plus(right);
		}
	};
	
	public static RubyMethod times = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			RubyArray array = (RubyArray)receiver;
			int times = RubyAPI.valueToInt(arg);
			
			return array.times(times);
		}
	};
	
	public static RubyMethod push = new RubyMethod() {
		protected RubyValue run(RubyValue receiver, RubyArray args,
				RubyBlock block) {
			RubyArray array = (RubyArray)receiver;
			for (RubyValue arg : args) {
				array.add(arg);
			}
			
			return array;
		}
	};
	
	public static RubyMethod pop = new RubyNoArgMethod() {
		protected RubyValue run(RubyValue receiver) {
			RubyArray array = (RubyArray)receiver;
			int index = array.length() - 1;
			return array.remove(index);
		}
	};
	
	public static RubyMethod deleteAt = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			RubyArray array = (RubyArray)receiver;
			int index = RubyAPI.valueToInt(arg);
			
			return array.remove(index);
		}
	};
	
	public static RubyMethod include = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			RubyArray array = (RubyArray)receiver;
			return RubyAPI.test(array.include(arg));
		}
	};
}
