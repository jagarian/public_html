package neo.data;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import neo.exception.TRuntimeException;

/**
 * 	@Class Name	: 	Param.java
 * 	@파일설명		: 	
 * 	@Version		: 	1.0
 *	@Author		: 	hoon09
 * 	@Copyright		: 	All Right Reserved
 **********************************************************************************************
 * 	작업일 		버젼	구분	작업자		내용
 * --------------------------------------------------------------------------------------------
 *	2005-05-01 	1.4		생성	hoon09		source create (삼성전기)
 *	2006-11-23 	1.4		수정	hoon09		code convention apply (멀티캠퍼스)
 *	2009-07-03	1.6		수정	hoon09		code convention apply (국민은행, 펜타시큐리티)
 *	2009-09-23	1.7		수정	hoon09		code valid check (푸르덴샬생명보험,뱅뱅)
 **********************************************************************************************             
 */
public class Param extends TData {

	private static final long serialVersionUID = 1L;

	public Param(String name) {
		this.name = name;
	}

	public Param(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public Param(int initialCapacity) {
		super(initialCapacity);
	}

	public Param() {
		super();
	}

	public Param(Map m) {
		super(m);
	}

	public Param(int initialCapacity, float loadFactor, boolean accessOrder) {
		super(initialCapacity, loadFactor, accessOrder);
	}

	public void set(Object key, Object value) {
		super.put(key, value);
	}

	public void setString(Object key, String value) {
		super.put(key, value);
	}

	public void putString(Object key, String value) {
		super.put(key, value);
	}

	public void setInt(Object key, int value) {
		Integer integer = new Integer(value);
		super.put(key, integer);
	}

	public void putInt(Object key, int value) {
		Integer integer = new Integer(value);
		super.put(key, integer);
	}

	public void setDouble(Object key, double value) {
		Double dou = new Double(value);
		super.put(key, dou);
	}

	public void putDouble(Object key, double value) {
		Double dou = new Double(value);
		super.put(key, dou);
	}

	public void setFloat(Object key, float value) {
		Float flo = new Float(value);
		super.put(key, flo);
	}

	public void putFloat(Object key, float value) {
		Float flo = new Float(value);
		super.put(key, flo);
	}

	public void setLong(Object key, long value) {
		Long lon = new Long(value);
		super.put(key, lon);
	}

	public void putLong(Object key, long value) {
		Long lon = new Long(value);
		super.put(key, lon);
	}

	public void setShort(Object key, short value) {
		Short shor = new Short(value);
		super.put(key, shor);
	}

	public void putShort(Object key, short value) {
		Short shor = new Short(value);
		super.put(key, shor);
	}

	public void setBoolean(Object key, boolean value) {
		Boolean bool = new Boolean(value);
		super.put(key, bool);
	}

	public void putBoolean(Object key, boolean value) {
		Boolean bool = new Boolean(value);
		super.put(key, bool);
	}

	public Object get(Object key) {
		Object o = (Object) super.get(key);
		if (o == null) {
			if (this.nullToInitialize) {
				return "";
			} else {
				return null;
			}
		} else {
			return o;
		}
	}

	public int getInt(Object key) {
		Object o = (Object) super.get(key);

		if (o == null) {
			if (this.nullToInitialize) {
				return 0;
			} else {
				throw new TRuntimeException("Key(" + key + ")'s value is null");
			}
		} else {
			Class classType = o.getClass();
			if (classType == Integer.class) {
				return ((Integer) o).intValue();
			} else if (classType == Short.class) {
				return ((Short) o).shortValue();
			}

			if (classType == String.class) {
				try {
					return Integer.parseInt(o.toString());
				} catch (Exception e) {
					throw new TRuntimeException(
						"Key(" + key + ")'s value is null");
				}
			}
			throw new TRuntimeException("Key(" + key + ")'s value is null");
		}
	}

	public double getDouble(Object key) {
		Object o = (Object) super.get(key);

		if (o == null) {
			if (this.nullToInitialize) {
				return 0.0;
			} else {
				throw new TRuntimeException("Key(" + key + ")'s value is null");
			}
		} else {

			Class classType = o.getClass();

			if (classType == Double.class) {
				return ((Double) o).doubleValue();
			} else if (classType == Float.class) {
				return ((Float) o).floatValue();
			}

			if (classType == String.class) {
				try {
					return Double.parseDouble(o.toString());
				} catch (Exception e) {
					throw new TRuntimeException(
						"Key(" + key + ")'s value is null");
				}
			}
			throw new TRuntimeException("Key(" + key + ")'s value is null");
		}
	}

	public float getFloat(Object key) {
		Object o = (Object) super.get(key);

		if (o == null) {
			if (this.nullToInitialize) {
				return (float) 0.0;
			} else {
				throw new TRuntimeException("Key(" + key + ")'s value is null");
			}
		} else {

			Class classType = o.getClass();

			if (classType == Float.class) {
				return ((Float) o).floatValue();
			}

			if (classType == String.class) {
				try {
					return Float.parseFloat(o.toString());
				} catch (Exception e) {
					throw new TRuntimeException(
						"Key(" + key + ")'s value is null");
				}
			}
			throw new TRuntimeException("Key(" + key + ")'s value is null");
		}
	}

	public long getLong(Object key) {
		Object o = (Object) super.get(key);

		if (o == null) {
			if (this.nullToInitialize) {
				return 0;
			} else {
				throw new TRuntimeException("Key(" + key + ")'s value is null");
			}
		} else {
			Class classType = o.getClass();

			if (classType == Long.class) {
				return ((Long) o).longValue();
			} else if (classType == Integer.class) {
				return ((Integer) o).intValue();
			} else if (classType == Short.class) {
				return ((Short) o).shortValue();
			}

			if (classType == String.class) {
				try {
					return Long.parseLong(o.toString());
				} catch (Exception e) {
					throw new TRuntimeException(
						"Key(" + key + ")'s value is null");
				}
			}
			throw new TRuntimeException("Key(" + key + ")'s value is null");
		}
	}

	public short getShort(Object key) {
		Object o = (Object) super.get(key);

		if (o == null) {
			if (this.nullToInitialize) {
				return 0;
			} else {
				throw new TRuntimeException("Key(" + key + ")'s value is null");
			}
		} else {

			Class classType = o.getClass();

			if (classType == Short.class) {
				return ((Short) o).shortValue();
			}

			if (classType == String.class) {
				try {
					return Short.parseShort(o.toString());
				} catch (Exception e) {
					throw new TRuntimeException(
						"Key(" + key + ")'s value is null");
				}
			}
			throw new TRuntimeException("Key(" + key + ")'s value is null");
		}
	}

	public boolean getBoolean(Object key) {
		Object o = (Object) super.get(key);

		if (o == null) {
			if (this.nullToInitialize) {
				return false;
			} else {
				throw new TRuntimeException("Key(" + key + ")'s value is null");
			}
		} else {
			if (o.getClass().isInstance(new Boolean(true))) {
				return ((Boolean) o).booleanValue();
			}

			if (o.getClass().isInstance(new String())) {
				try {
					return Boolean.getBoolean(o.toString());
				} catch (Exception e) {
					throw new TRuntimeException(
						"Key(" + key + ")'s value is null");
				}
			}
			throw new TRuntimeException("Key(" + key + ")'s value is null");
		}
	}

	public String getString(Object key) {
		Object o = (Object) super.get(key);

		if (o == null) {
			if (this.nullToInitialize) {
				return "";
			} else {
				return null;
			}
		} else {
			return o.toString();
		}
	}

	public synchronized String toString() {
		int max = super.size() - 1;
		StringBuffer buf = new StringBuffer();

		Set vset = super.entrySet();
		Iterator values = vset.iterator();

		buf.append("{");

		for (int i = 0; i <= max; i++) {
			Object o = values.next();

			if (o == null) {
				buf.append("");
			} else {
				buf.append(o.toString());
			}
			if (i < max) {
				buf.append(", ");
			}
		}
		buf.append("}");

		return "LData[" + this.getName() + "]=" + buf.toString();
	}

}
