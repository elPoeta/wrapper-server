package com.browxy.wrapper.db.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.browxy.wrapper.db.DuplicatedRecordException;

public interface GenericRepository<T, E> {
	T findById(E id) throws SQLException;

	List<T> findAll() throws SQLException;

	List<T> findMany(List<String> columns, Map<String, String> joins, Map<String, Map<String, String>> filters, Map<String, String> orderBy, boolean convertNumericToString, int limit,
			int offset) throws SQLException;

    List<T> findByCustom(String query, List<String> columns, boolean convertNumericToString) throws SQLException;
    
	String save(T entity) throws SQLException, DuplicatedRecordException;

	boolean update(T entity) throws SQLException, NumberFormatException, DuplicatedRecordException;

	boolean deleteById(E id) throws SQLException;
	
	Long countRows(String query, String key) throws SQLException, NumberFormatException;
   
}
