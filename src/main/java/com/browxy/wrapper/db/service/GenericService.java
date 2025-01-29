package com.browxy.wrapper.db.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.browxy.wrapper.db.DuplicatedRecordException;

public interface GenericService<T, E> {
	T getById(E id) throws SQLException;

	List<T> getAll() throws SQLException;

	List<T> getMany(List<String> columns, Map<String, String> joins, Map<String, Map<String, String>> filters, Map<String, String> orderBy, boolean convertNumericToString, int limit,
			int offset) throws SQLException;

    List<T> getByCustom(String query, List<String> columns, boolean convertNumericToString) throws SQLException;

	String create(T entity) throws SQLException, DuplicatedRecordException;

	boolean update(T entity) throws SQLException, NumberFormatException, DuplicatedRecordException;

	boolean delete(E id) throws SQLException;
	
	Long getTotalRows(String query, String key) throws SQLException, NumberFormatException;
 
}
