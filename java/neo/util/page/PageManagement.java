package neo.util.page;

import java.sql.*;

import neo.util.log.Log;

/**
 * 	@Class Name	: 	PageManagement.java
 * 	@���ϼ���		: 	
 * 	@Version		: 	1.0
 *	@Author		: 	hoon09
 * 	@Copyright		: 	All Right Reserved
 **********************************************************************************************
 * 	�۾��� 		����	����	�۾���		����
 * --------------------------------------------------------------------------------------------
 *	2005-05-01 	1.4		����	hoon09		source create (�Ｚ����)
 *	2006-11-23 	1.4		����	hoon09		code convention apply (��Ƽķ�۽�)
 *	2009-07-03	1.6		����	hoon09		code convention apply (��������, ��Ÿ��ť��Ƽ)
 *	2009-09-23	1.7		����	hoon09		code valid check (Ǫ������������,���)
 **********************************************************************************************             
 */
public class PageManagement {

	public int[] getStartEndNo(int total_cnt, 
								int c_page, 
								int list_num) {
		int[] startEnd = new int[2];
		startEnd[0] = (c_page - 1) * list_num;
		if (total_cnt > list_num) {
			startEnd[1] = c_page * list_num;
		} else {
			startEnd[1] = list_num;
		}

		return startEnd;
	}

	public String[][] getData(	ResultSet rs,
								int total_cnt,
								int c_page,
								int list_num,
								int vec_count) {
		try {
			int k = 0;
			int j = 0;
			int rowCnt = 0;
			int t_page = 0;
			if (total_cnt % list_num == 0) {
				t_page = total_cnt / list_num;
			} else {
				t_page = total_cnt / list_num + 1;
			}

			if (total_cnt < list_num) {
				rowCnt = total_cnt;
			} else if (t_page == c_page) {
				if ((total_cnt % list_num) == 0) {
					rowCnt = list_num;
				} else {
					rowCnt = total_cnt - ((c_page - 1) * list_num);
				}
			} else {
				rowCnt = list_num;
			}

			String[][] r_data = new String[vec_count][rowCnt];

			while (rs.next()) {
				k++;
				if (k <= (c_page * list_num)
					&& k > ((c_page - 1) * list_num)) {
					for (int t = 0; t < vec_count; t++) {
						if (rs.getString(t + 1) == null) {
							r_data[t][j] = "";
						} else {
							r_data[t][j] = rs.getString(t + 1);
						}
					}
					j++;
				}
				if (j == list_num) {
					break;
				}
			}

			return r_data;
		} catch (Exception e) {
			Log.info(e.getMessage(), this);
			return null;
		}
	}

	public int[] getPage(	int total_cnt,
							int c_page,
							int list_num,
							int page_num) {
		int t_page_gubun = 0;
		int c_page_gubun = 0;
		int[] st_end = new int[5];
		int t_page = 0;

		if (total_cnt % list_num == 0) {
			t_page = total_cnt / list_num;
		} else {
			t_page = total_cnt / list_num + 1;
		}

		if ((t_page % page_num) == 0) {
			t_page_gubun = t_page / page_num;
		} else {
			t_page_gubun = (t_page / page_num) + 1;
		}
		if ((c_page % page_num) == 0) {
			c_page_gubun = c_page / page_num;
		} else {
			c_page_gubun = (c_page / page_num) + 1;
		}

		if (total_cnt == 0) {
			st_end[0] = 0; // Start Page
			st_end[1] = 0; // 1 2 3 4 5 ... �� ��Ÿ���� ����(10�̸� 1 2 3 4 5 6 7 8 9 10)
			st_end[2] = 0; // ���� Block Page (�� ���� ���� 10���� �� block 1�̸� 1~10 , 2�̸� 11~20 ...)
			st_end[3] = 0; // ���� Block Page
			st_end[4] = 0; // End Page
		} else if (c_page_gubun == 1 && c_page_gubun == t_page_gubun) {
			st_end[0] = 1;
			st_end[1] = t_page;
			st_end[2] = 0;
			st_end[3] = 0;
			st_end[4] = t_page;
		} else if (c_page_gubun == 1 && c_page_gubun != t_page_gubun) {
			st_end[0] = 1;
			st_end[1] = page_num;
			st_end[2] = 0;
			st_end[3] = c_page_gubun * page_num + 1;
			st_end[4] = t_page;
		} else if (c_page_gubun != 1 && c_page_gubun != t_page_gubun) {
			st_end[0] = (c_page_gubun - 1) * page_num + 1;
			st_end[1] = c_page_gubun * page_num;
			st_end[2] = (c_page_gubun - 1) * page_num - (page_num - 1);
			st_end[3] = c_page_gubun * page_num + 1;
			st_end[4] = t_page;
		} else if (c_page_gubun != 1 && c_page_gubun == t_page_gubun) {
			st_end[0] = (c_page_gubun - 1) * page_num + 1;
			st_end[1] = t_page;
			st_end[2] = (c_page_gubun - 1) * page_num - (page_num - 1);
			st_end[3] = 0;
			st_end[4] = t_page;
		}

		return st_end;
	}
}