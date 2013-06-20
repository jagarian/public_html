package neo.util.comm;

import java.util.ArrayList;

/**
 * 	@Class Name	: 	ArrayUtil.java
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
public class ArrayUtil {
	/**
	 * Reallocates an array with a new size, and copies the contents of the old
	 * array to the new array.
	 * 
	 * @param oldArray
	 *            the old array, to be reallocated.
	 * @param newSize
	 *            the new array size.
	 * @return A new array with the same contents.
	 */
	public static Object resizeArray(Object oldArray, int newSize) {
		int oldSize = java.lang.reflect.Array.getLength(oldArray);
		Class elementType = oldArray.getClass().getComponentType();
		Object newArray = java.lang.reflect.Array.newInstance(elementType, newSize);
		int preserveLength = Math.min(oldSize, newSize);
		if (preserveLength > 0)
			System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
		return newArray;
	}
	
	public static int arraryKeySearch(ArrayList arr, String findkey) {
		int find_pos = 0;
		String tempStr = "";
		for(int i=0; i<arr.size(); i++) {
			tempStr = (String)arr.get(i);
			//System.out.println(i+" = " + tempStr);
			if (tempStr.equals(findkey)) find_pos=i;
		}
		return find_pos;
	}

	// --------------------------------------------------------------------------------

	// Test routine for resizeArray().
	public static void main(String[] args) {
		int[] a = { 1, 2, 3 };
		a = (int[]) resizeArray(a, 5);
		a[3] = 4;
		a[4] = 5;
		for (int i = 0; i < a.length; i++)
			System.out.println(a[i]);
	}

}

// --------------------------------------------------------------------------------
//
// How to resize a two-dimensional array
//
// Two-dimensional arrays in Java are arrays of arrays. To resize a
// two-dimensional array, the resizeArray function must be applied to the outer
// array and to all the nested arrays.
// Example:

// int a[][] = new int[2][3];
// //...
// a = (int[][])resizeArray(a,20);
// // new array is [20][3]
// for (int i=0; i<a.length; i++) {
// if (a[i] == null)
// a[i] = new int[30];
// else a[i] = (int[])resizeArray(a[i],30); }
// // new array is [20][30]
