package com.akun.elibrary.bo.imp;

import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;

import com.akun.elibrary.bean.Student;
import com.akun.elibrary.bean.StudentExample;
import com.akun.elibrary.bo.StudentBO;
import com.akun.elibrary.dao.StudentDAO;

public class StudentBOImp implements StudentBO {
	private static final Logger LOGGER = Logger.getLogger(StudentBOImp.class);
	private StudentDAO studentDAO;

	public StudentDAO getStudentDAO() {
		return studentDAO;
	}

	public void setStudentDAO(StudentDAO studentDAO) {
		this.studentDAO = studentDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akun.elibrary.bo.StudentBO#add(com.akun.elibrary.bean.Student)
	 */
	/**
	 * add new stu info 2operation succeeded，-1operation failed. 5 duplicate stunum.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int addStudent(Student item) {
		int result = -1;
		StudentExample snumexample = new StudentExample();
		snumexample.createCriteria().andSnumberEqualTo(item.getSnumber());
		List<Student> snumList = (List<Student>) studentDAO.selectByExample(snumexample);
		if (snumList.size() == 0) {
			try {
				studentDAO.insertSelective(item);
				result = 2;
			}
			catch (Exception e) {
				LOGGER.error(e.toString());
			}
		} else {
			result = 5;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akun.elibrary.bo.StudentBO#countByExample(com.akun.elibrary.bean.StudentExample)
	 */
	@Override
	public int countByExample(StudentExample example) {
		int count = studentDAO.countByExample(example);
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akun.elibrary.bo.StudentBO#deleteByExample(com.akun.elibrary.bean.StudentExample)
	 */
	/**
	 * delete stuinfo 0incorrect data format.1Operation not allowed.2operation succeeded，
	 */
	@Override
	public int deleteByPrimaryKey(String snumbersToDelete) {
		int result = 1;
		if (snumbersToDelete != null && !"".equals(snumbersToDelete)) {
			String[] snumbers = snumbersToDelete.split(",");

			try {
				for (String snumber : snumbers) {
					try {
						StudentExample example = new StudentExample();
						example.createCriteria().andSnumberEqualTo(snumber);
						studentDAO.deleteByExample(example);
					}
					catch (Exception e) {
						LOGGER.error(e.toString());
					}
				}
				result = 2;
			}
			catch (Exception e) {
				result = 0;
			}

		} else {
			result = 0;
		}
		return result;
	}
    /**
     * search
     */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akun.elibrary.bo.StudentBO#selectByCondition(int, int, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Student> selectByCondition(int pageindex, int rows, String key) {
		List<Student> list = null;
		int startIndex = 0;
		int endIndex = 0;
		if (pageindex == 0) {
			endIndex = rows;
		}
		if (pageindex >= 1 && rows >= 1) {
			startIndex = pageindex;
			endIndex = pageindex + rows;
		}
		if (endIndex > startIndex) {
			StudentExample example = new StudentExample();
			example.createCriteria().andSnameLike("%" + key + "%");
			int count = studentDAO.countByExample(example);
			if (endIndex > count) {
				endIndex = count;
			}
			list = studentDAO.selectByExample(example);
			if (list != null && list.size() > 0) {
				list = list.subList(startIndex, endIndex);
			} else {
				list = null;
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akun.elibrary.bo.StudentBO#selectByExample(com.akun.elibrary.bean.StudentExample)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Student> selectByExample(StudentExample example) {
		List<Student> list = (List<Student>)studentDAO.selectByExample(example);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akun.elibrary.bo.StudentBO#update(java.lang.String)
	 */

	/**
	 * update a stuinfo 0incorrect data format. 2operation succeeded，-1operation failed.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int update(String studentJson) {
		int result = 0;
		if (studentJson != null && !"".equals(studentJson)) {
			JSONArray jsonArray = JSONArray.fromObject(studentJson);
			List<Student> studentList = (List<Student>) JSONArray.toCollection(jsonArray,
					Student.class);
			Student a = studentList.get(0);
			System.out.println(a.getSname());
			if (result == 0) {
				int updatecount = 0;
				for (Student item : studentList) {
					StudentExample example = new StudentExample();
					example.createCriteria().andSnumberEqualTo(item.getSnumber());
					try {
						studentDAO.updateByExampleSelective(item, example);
						updatecount++;
					}
					catch (Exception e) {
						LOGGER.error(e.toString());
					}
				}
				if (updatecount == studentList.size()) {
					result = 2;
				} else {
					result = -1;
				}
			} else {
				result = 0;
			}
		} else {
			result = -1;
		}

		return 2;
	}
}
