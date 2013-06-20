package neo.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import neo.exception.TRuntimeException;

/**
 * 	@Class Name	: 	MultiParam.java
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
public class MultiParam extends TData {

	private static final long serialVersionUID = 1L;
	private int field_index = 0;

	HashMap entityKey = null;

	public MultiParam(String name) {
		this.name = name;
	}

	public MultiParam(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public MultiParam(int initialCapacity) {
		super(initialCapacity);
	}

	public MultiParam() {
		super();
	}

	public MultiParam(Map m) {
		super(m);
	}

	public MultiParam(int initialCapacity,
								float loadFactor,
		boolean accessOrder) {
		super(initialCapacity, loadFactor, accessOrder);
	}

	public void add(Object key, Object value) {
		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(value);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(value);
		}
	}

	public void addString(Object key, String value) {
		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(value);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(value);
		}
	}

	public void addInt(Object key, int value) {
		Integer valueInt = new Integer(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueInt);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueInt);
		}
	}

	public void addDouble(Object key, double value) {
		Double valueDouble = new Double(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueDouble);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueDouble);
		}
	}

	public void addFloat(Object key, float value) {
		Float valueFloat = new Float(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueFloat);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueFloat);
		}
	}

	public void addLong(Object key, long value) {
		Long valueLong = new Long(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueLong);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueLong);
		}
	}

	public void addShort(Object key, short value) {
		Short valueShort = new Short(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueShort);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueShort);
		}
	}

	public void addBoolean(Object key, boolean value) {
		Boolean valueBoolean = new Boolean(value);

		if (!super.containsKey(key)) {
			ArrayList arrayList = new ArrayList();
			arrayList.add(valueBoolean);
			super.put(key, arrayList);
		} else {
			ArrayList arrayList = (ArrayList) super.get(key);
			arrayList.add(valueBoolean);
		}
	}

	private Object getObject(Object key, int index) {
		Object o = null;
		ArrayList arrayList = (ArrayList) super.get(key);

		if (arrayList == null) {
			if (this.nullToInitialize) {
				return null;
			} else {
				throw new TRuntimeException("[MultiParam] Key(" + key + ")'s value is null");
			}
		}

		try {
			o = arrayList.get(index);
		} catch (IndexOutOfBoundsException ioe) {
			throw new TRuntimeException("[MultiParam] Key(" + key + ")'s value is null");
		}
		return o;
	}

	public Object get(Object key, int index) {
		return getObject(key, index);
	}

	public int getInt(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			return 0;
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
					throw new TRuntimeException("[MultiParam] Key(" + key + ")'s value is null");
				}
			}
			throw new TRuntimeException("[MultiParam] Key(" + key + ")'s value is null");
		}
	}

	public double getDouble(Object key, int index) {
		Object o = getObject(key, index);
		if (o == null) {
			return 0;
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
					throw new TRuntimeException("[MultiParam] Key(" + key + ")'s value is null");
				}
			}
			throw new TRuntimeException("[MultiParam] Key(" + key + ")'s value is null");
		}
	}

	public float getFloat(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			return (float) 0.0;
		} else {
			Class classType = o.getClass();

			if (classType == Float.class) {
				return ((Float) o).floatValue();
			}

			if (classType == String.class) {
				try {
					return Float.parseFloat(o.toString());
				} catch (Exception e) {
					throw new TRuntimeException("[MultiParam] Key(" + key + ")'s value is null");
				}
			}
			throw new TRuntimeException("[MultiParam] Key(" + key + ")'s value is null");
		}
	}

	public long getLong(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			return 0;
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
					throw new TRuntimeException("[MultiParam] Key(" + key + ")'s value is null");
				}
			}
			throw new TRuntimeException("[MultiParam] Key(" + key + ")'s value is null");
		}
	}

	public short getShort(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			return 0;
		} else {
			Class classType = o.getClass();

			if (classType == Short.class) {
				return ((Short) o).shortValue();
			}

			if (classType == String.class) {
				try {
					return Short.parseShort(o.toString());
				} catch (Exception e) {
					throw new TRuntimeException("[MultiParam] Key(" + key + ")'s value is null");
				}
			}
			throw new TRuntimeException("[MultiParam] Key(" + key + ")'s value is null");
		}
	}

	public boolean getBoolean(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			return false;
		} else {
			Class classType = o.getClass();
			if (classType == Boolean.class) {
				return ((Boolean) o).booleanValue();
			}
			if (classType == String.class) {
				try {
					return Boolean.getBoolean(o.toString());
				} catch (Exception e) {
					throw new TRuntimeException("[MultiParam] Key(" + key + ")'s value is null");
				}
			}
			throw new TRuntimeException("[MultiParam] Key(" + key + ")'s value is null");
		}
	}

	public String getString(Object key, int index) {
		Object o = getObject(key, index);

		if (o == null) {
			if (isNullToInitialize())
				return "";
			else
				return null;
		} else {
			return o.toString();
		}
	}

	public Object remove(Object key, int index) {
		if (super.containsKey(key)) {
			return ((ArrayList) super.get(key)).remove(index);
		} else {
			return null;
		}
	}

	public int keySize(Object key) {
		if (super.containsKey(key)) {
			return ((ArrayList) super.get(key)).size();
		} else {
			return 0;
		}
	}

	public int keySize() {
		Set tempSet = super.keySet();
		Iterator iterator = tempSet.iterator();
		if (iterator.hasNext()) {
			String key = iterator.next().toString();
			return ((ArrayList) super.get(key)).size();
		} else {
			return 0;
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

		return "MultiParam[" + this.getName() + "]=" + buf.toString();
	}

	public Param getParam(int index) {
		Param singleData = new Param("");
		Set tempSet = super.keySet();
		Iterator iterator = tempSet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			//Object o = ((ArrayList) super.get(key)).get(index);
			Object o = getObject(key, index);
			singleData.put(key, o);
		}
		return singleData;
	}

	public Param getParam(String dataName, int index) {
		Param singleData = new Param(dataName);
		String prefix = dataName + ".";
		Set tempSet = super.keySet();
		Iterator iterator = tempSet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			int key_index = key.indexOf(".");
			String realKey = key.substring(key_index + 1);
			if (key.startsWith(prefix)) {
				//Object o = ((ArrayList) super.get(key)).get(index);
				Object o = getObject(key, index);
				singleData.put(realKey, o);
			}
		}
		return singleData;
	}

	public void addParam(Param data) {
		Set tempSet = data.keySet();
		Iterator iterator = tempSet.iterator();
		while (iterator.hasNext()) {
			Object key = iterator.next();
			if (this.containsKey(key)) {
				int field_size = ((ArrayList) this.get(key)).size();
				if (field_size != field_index) {
					for (int inx = field_size; inx < field_index; inx++) {
						this.add(key, null);
					}
				}
				this.add(key, data.get(key));
			} else {
				for (int inx = 0; inx < field_index; inx++) {
					this.add(key, null);
				}
				this.add(key, data.get(key));
			}
		}
		field_index++;
	}

	public void addParam(String dataName, Param data) {
		int entitySize = 0;
		if (entityKey == null) {
			entityKey = new HashMap(5);
		} else {
			if (entityKey.containsKey(dataName))
				entitySize = ((Integer) entityKey.get(dataName)).intValue();
		}
		Set tempSet = data.keySet();
		Iterator iterator = tempSet.iterator();
		while (iterator.hasNext()) {
			Object key = iterator.next();
			String dataKey = dataName + "." + key;
			if (this.containsKey(dataKey)) {
				int fieldSize = ((ArrayList) this.get(dataKey)).size();
				if (fieldSize != entitySize) {
					for (int inx = fieldSize; inx < entitySize; inx++) {
						this.add(dataKey, null);
					}
				}
				this.add(dataKey, data.get(key));
			} else {
				for (int inx = 0; inx < entitySize; inx++) {
					this.add(dataKey, null);
				}
				this.add(dataKey, data.get(key));
			}
		}
		entityKey.put(dataName, new Integer(entitySize + 1));
	}
}
